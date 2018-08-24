package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvaro Gomez on 25/07/2018.
 */

public class FeedholderFragment extends Fragment {

    private Context mContext;
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
        mContext = getActivity();

        view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        mContext = getActivity();
        setHasOptionsMenu(true);

        lvStock = (ListView)view.findViewById(android.R.id.list);
        lvStock.setVerticalScrollBarEnabled(true);
        lvStock.setScrollbarFadingEnabled(true);

        mFeedList = new ArrayList<>();

        ThreadCreation threadCreation = new ThreadCreation();
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

    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId) {

        MenuItem item = menu.findItem(menuItemId);
        SpannableStringBuilder builder = new SpannableStringBuilder("   " + getResources().getString(labelId));
        builder.setSpan(new ImageSpan(mContext, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        setIconInMenu(menu, R.id.item_log_out, R.string.log_out, R.drawable.ic_action_log_out);
        menu.removeItem(R.id.item_search);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public void onResume() {
        super.onResume();
        //lock screen to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override public void onPause() {
        super.onPause();
        //set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.item1:
                return true;
            case R.id.item2:
                return true;
            case R.id.item_log_out:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("logOut", "1");
                startActivity(intent);
                getActivity().finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

                        if (xpp.getName().equalsIgnoreCase(Constants.ITEM)) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase(Constants.TITLE)) {
                            if (insideItem) {
                                mTitle = xpp.nextText();
                                mTitle = mTitle.replace(Constants.JSON_APOSTROFE,Constants.APOSTROFE);
                                mTitle = mTitle.replace(Constants.JSON_QUOTES,Constants.QUOTES);
                            }
                        } else if (xpp.getName().equalsIgnoreCase(Constants.LINK)) {
                            if (insideItem) {
                                mHyperlink = xpp.nextText(); //extract the link of article
                            }
                        } else if (xpp.getName().equalsIgnoreCase(Constants.DESCRIPTION)) {
                            if (insideItem) {
                                mImageLink = xpp.nextText(); //extract the link of article
                                mImageLink = getImageLink(mImageLink);
                            }
                        }
                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase(Constants.ITEM)){
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
            lvStock.invalidate();

        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate();
        }

    }

    public String getImageLink (String description) {

        String imageLink = "";
        String aux;
        String imgSrc = "<img src=";

        if (description.contains(imgSrc)){
            String[] stringAux1 = description.split( imgSrc,2);

            if (stringAux1[1].contains("http:")) {
                String[] stringAux2 = stringAux1[1].split("http:",3);

                if (stringAux2[2].contains("width")) {
                    String[] stringAux3 = stringAux2[2].split("width",2);
                    imageLink = "http://" + stringAux3[0].replace("\"","");
                }
            }
        }

        return imageLink;

    }

}
