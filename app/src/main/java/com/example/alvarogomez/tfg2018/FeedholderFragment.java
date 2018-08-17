package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alvarogomez.remoteDB.RemoteStocksData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 25/07/2018.
 */

public class FeedholderFragment extends Fragment {

    private ListView lvStock;
    private List<Feed> mFeedList;
    private static final String ARG_SECTION_NUMBER = "section_number";
    View view;
    public static String urlConexion;

    public FeedholderFragment() {
    }

    public static FeedholderFragment newInstance(int sectionNumber, String ticker) {

        FeedholderFragment fragment = new FeedholderFragment();

        if (!ticker.equals("")){
            urlConexion = Constants.RSS_YAHOO_FINANCE_BY_TICKER_START + ticker + Constants.RSS_YAHOO_FINANCE_BY_TICKER_END;
        } else {
            urlConexion = Constants.RSS_YAHOO_FINANCE;
        }

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName() + " Req " + urlConexion);

        view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        lvStock = (ListView)view.findViewById(android.R.id.list);
        lvStock.setVerticalScrollBarEnabled(false);

        mFeedList = new ArrayList<>();

        FeedholderFragment.ThreadCreation threadCreation = new FeedholderFragment.ThreadCreation();
        threadCreation.execute().toString();

        lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Feed feed = mFeedList.get(position);
                System.out.println(feed.getHyperlink());

                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(feed.getHyperlink()));
                startActivity(browserIntent);

            }
        });

        return view;

    }

    public class ThreadCreation extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            String mTitle = "";
            String mHyperlink = "";
            String mImageLink = "";
            int mId = 0;
            int i = 0, j = 0;

            try {
                URL url = new URL(urlConexion);

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
                                mTitle = xpp.nextText().replace("&apos;","'");
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem)
                                mHyperlink = xpp.nextText(); //extract the link of article
                        }
                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }

                    if ((!mTitle.equals(""))&(!mHyperlink.equals(""))){

                        mFeedList.add(new Feed(mId, mTitle, mHyperlink, mImageLink));
                        mId++;
                        mTitle = "";
                        mHyperlink = "";
                        mImageLink = "";

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

            return null;
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
        protected void onPostExecute(Void voids){
            super.onPostExecute(voids);

            FeedListAdapter mAdapter = new FeedListAdapter(view.getContext(), mFeedList);
            lvStock.setAdapter(mAdapter);

        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate();
        }

    }

}
