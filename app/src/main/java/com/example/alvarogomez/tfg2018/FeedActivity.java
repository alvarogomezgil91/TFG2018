package com.example.alvarogomez.tfg2018;

import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    String RSS_YAHOO_FINANCE = Constants.RSS_YAHOO_FINANCE;
    TextView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        setContentView(R.layout.activity_feed);

        tView = (TextView) findViewById(R.id.textView2);

        Conexion hiloConexion = new Conexion();
        hiloConexion.execute();

    }

    public class Conexion extends AsyncTask<Void, Integer, String>{


        @Override
        protected String doInBackground(Void... voids) {

            String salida = "";
            List<String> links = new ArrayList<>();

            int i = 0, j = 0;

            try {
                URL url = new URL(RSS_YAHOO_FINANCE);


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;

                // Returns the type of current event: START_TAG, END_TAG, etc..
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {

                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                //headlines.add(xpp.nextText()); //extract the headline
                                salida += xpp.nextText().replace("&apos;","'");
                                salida += "\n--------------\n";
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem)
                                links.add(xpp.nextText()); //extract the link of article
                        }
                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }

                    eventType = xpp.next(); //move to next element
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return salida;

        }

        public InputStream getInputStream(URL url) {

            try {
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                return null;
            }

        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String salida){
            tView.append(salida);
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate();
        }

    }

}
