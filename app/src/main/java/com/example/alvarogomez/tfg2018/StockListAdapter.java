package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 18/06/2018.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StockListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Stock> mStockList;

    //Constructor

    public StockListAdapter(Context mContext, List<Stock> mStockList) {
        this.mContext = mContext;
        this.mStockList = mStockList;
    }

    public void addListItemToAdapter(List<Stock> list) {
        mStockList.addAll(list);
        this.notifyDataSetChanged();;
    }

    @Override
    public int getCount() {
        return mStockList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_stock_list, null);
        TextView tvStock = (TextView)v.findViewById(R.id.tv_stock_name);
        TextView tvCierre = (TextView)v.findViewById(R.id.tv_cierre);
        TextView tvDescription = (TextView)v.findViewById(R.id.tv_description);
        ImageView ivIcono = (ImageView)v.findViewById(R.id.imageView2);

        tvStock.setText(mStockList.get(position).getStockName());
        tvCierre.setText(String.valueOf(mStockList.get(position).getCierre()) + " $");
        tvDescription.setText(mStockList.get(position).getDescription());
        ivIcono.setImageResource(R.drawable.imagen_prueba);
        ivIcono.hasOnClickListeners();
        ivIcono.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "El stock es " + mStockList.get(position).getStockName(), Toast.LENGTH_SHORT).show();
            }
        });


        v.setTag(mStockList.get(position).getId());

        return v;
    }
}
