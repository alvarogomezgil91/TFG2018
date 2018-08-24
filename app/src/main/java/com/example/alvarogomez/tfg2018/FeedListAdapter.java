package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Alvaro Gomez on 25/07/2018.
 */

public class FeedListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Feed> mFeedList;
    private String mImageUrl;

    public FeedListAdapter (Context mContext, List<Feed> mFeedList){

        this.mContext = mContext;
        this.mFeedList = mFeedList;

    }

    @Override
    public int getCount() {
        return mFeedList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFeedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        View v = View.inflate(mContext, R.layout.item_feed_list, null);
        TextView tvFeed = (TextView)v.findViewById(R.id.tv_feed);
        ImageView ivIcono = (ImageView)v.findViewById(R.id.imageView2);

        tvFeed.setText(mFeedList.get(position).getTitle());

        mImageUrl = mFeedList.get(position).getImageLink();

        if (!mImageUrl.equals("")) {
            new DownloadImageTask((ImageView) v.findViewById(R.id.imageView2))
                    .execute(mImageUrl);

        } else {
            ivIcono.setImageResource(R.drawable.presentation_image);
        }

        v.setTag(mFeedList.get(position).getId());

        return v;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
