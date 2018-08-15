package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 18/06/2018.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alvarogomez.remoteDB.RemoteFavouriteStocks;
import com.example.alvarogomez.remoteDB.RemoteFavouriteStocksData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class StockListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Stock> mStockList;
    private int mFavorito = 0;
    private String mStock;
    Boolean comandoOk = false;
    String mMetodo;




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

    public void updateList(List<Stock> list) {
        this.mStockList.clear();
        this.mStockList.addAll(list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.item_stock_list, null);
        TextView tvStock = (TextView)v.findViewById(R.id.tv_stock_name);
        TextView tvCierre = (TextView)v.findViewById(R.id.tv_cierre);
        TextView tvDescription = (TextView)v.findViewById(R.id.tv_description);
        final ImageView ivIcono = (ImageView)v.findViewById(R.id.imageView2);

        tvStock.setText(mStockList.get(position).getStockName());
        tvCierre.setText(String.valueOf(mStockList.get(position).getCierre()) + " $");
        tvDescription.setText(mStockList.get(position).getDescription());
        mFavorito = mStockList.get(position).getFavorito();

        if (mFavorito == 0) {
            ivIcono.setImageResource(R.drawable.corazon_contorno);
        } else {
            ivIcono.setImageResource(R.drawable.corazon_rojo);
        }

        ivIcono.hasOnClickListeners();

        ivIcono.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mStock = mStockList.get(position).getStockName();

                if (mStockList.get(position).getFavorito() == 1) {
                    mMetodo = "DeleteRemoteFavouriteStock";
                    ThreadCreation threadCreation = new ThreadCreation();
                    threadCreation.execute().toString();
                    ivIcono.setImageResource(R.drawable.corazon_contorno);
                    mStockList.get(position).setFavorito(0);
                } else {
                    mMetodo = "InsertRemoteFavouriteStock";
                    ThreadCreation threadCreation = new ThreadCreation();
                    threadCreation.execute().toString();
                    mStockList.get(position).setFavorito(1);
                    ivIcono.setImageResource(R.drawable.corazon_rojo);

                }



                notifyDataSetChanged();

            }
        });

        v.setTag(mStockList.get(position).getId());

        return v;
    }


    public class ThreadCreation extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            List<GraphicData> stockDataList = new ArrayList<GraphicData>();
            java.lang.reflect.Method method = null;

            try {

                method = RemoteFavouriteStocks.class.getMethod(mMetodo);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            String userName = "Alvaro1";
            RemoteFavouriteStocks remoteFavouriteStocks = new RemoteFavouriteStocks(userName, mStock);


            try{
                comandoOk = (Boolean) method.invoke(remoteFavouriteStocks);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

        }

    }





}
