package com.example.s00a2032.myapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {


    public Button button;
    public EditText stock_input;
    public TextView stock_info;
    private static final String TAG = "MyActivity";
    private GoogleApiClient client;
    String title="";
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        stock_input = (EditText) findViewById(R.id.editText);
        stock_info = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(runnable).start();
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            try{
                URL url1 = new URL("http://www.wantgoo.com/Stock/"+stock_input.getText().toString().trim());
                Document doc = Jsoup.parse(url1,5*1000);
                String text =doc.title().split("_")[0];
                String Stock_name=doc.title().split("_")[0];
                Element table1 = doc.select("table").get(1);
                Iterator<Element> ite = table1.select("td").iterator();

                Elements rows = table1.select("tr");
                DecimalFormat df=new DecimalFormat("#.##");
                double uprate=0.00;
                for (int i = 0; i < rows.size(); i++) { //first row is the col names so skip it.
                    Element row = rows.get(i);
                    Elements cols = row.select("td");
                    Elements colss = cols.get(1).select("div");
// 				       System.out.println( row.text().toString());
//                                       Log.v(TAG,"mes:"+ cols.toString());
                    ArrayList<String> uplist = new ArrayList<>();
                    ArrayList<String> dnlist = new ArrayList<>();
                    for (int j = 0; j < colss.size(); j++) {

                        if(!colss.get(j).getElementsByClass("up").toString().trim().equals("")){
                            String tempup=colss.get(j).getElementsByClass("up").toString().replaceAll("<span class=\"up\">","").replaceAll("</span>", "").replaceAll(",", "").replaceAll(" ", "").trim();
                            uplist.add(tempup);
                        }
                        if(!colss.get(j).getElementsByClass("dn").toString().trim().equals("")){
                            String tempdn=colss.get(j).getElementsByClass("dn").toString().replaceAll("<span class=\"dn\">","").replaceAll("</span>", "").replaceAll(",", "").replaceAll(" ", "").trim();
                            dnlist.add(tempdn);
                        }
                    }

                    int total=uplist.size()+dnlist.size();
                    double rate=25.00/total*uplist.size();
                    String rate_s=df.format(rate);
//					System.out.print(rate_s);
                    uprate=uprate+rate;
                    title=Stock_name+"\n"+"技術面上漲機率"+df.format(uprate)+"%";
//			    	System.out.println("");
                }
            }catch(Exception e){
                title="請輸入正確股票代號";
            }
            handler.sendEmptyMessage(0);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            stock_info.setText(title);
        }
    };
    @Override
    public void onStart() {
        super.onStart();

        client.connect();

        Action viewAction2 = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.s00a2032.myapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction2);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction2 = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.s00a2032.myapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction2);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.s00a2032.myapp/http/host/path")
        );

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }





}
