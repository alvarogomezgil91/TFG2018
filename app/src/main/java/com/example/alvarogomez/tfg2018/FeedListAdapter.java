package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Alvaro Gomez on 25/07/2018.
 */

public class FeedListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Feed> mFeedList;



    public FeedListAdapter (Context mContext, List<Feed> mFeedList){

        this.mContext = mContext;
        this.mFeedList = mFeedList;

    }

    public void addListItemToAdapter(List<Feed> list) {
        mFeedList.addAll(list);
        this.notifyDataSetChanged();;
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

        View v = View.inflate(mContext, R.layout.item_feed_list, null);
        TextView tvFeed = (TextView)v.findViewById(R.id.tv_feed);
        ImageView ivIcono = (ImageView)v.findViewById(R.id.imageView2);

        tvFeed.setText(mFeedList.get(position).getTitle());
        ivIcono.setImageResource(R.drawable.imagen_prueba);
        ivIcono.hasOnClickListeners();

        ivIcono.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "El stock es " + mFeedList.get(position).getHyperlink(), Toast.LENGTH_SHORT).show();
            }
        });

        v.setTag(mFeedList.get(position).getId());

        return v;
    }
}
