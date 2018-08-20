package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.alvarogomez.remoteDB.RemoteFavouriteStocks;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alvaro Gomez on 15/08/2018.
 */

public class FavoriteStockListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Stock> mStockList;
    private String mStockName;
    private String mCierre;
    private String mDescripcion;
    private int mFavorito = 0;
    private int mTendencia;
    private String mStock;
    Boolean comandoOk = false;
    String mMetodo;

    //Constructor

    public FavoriteStockListAdapter(Context mContext, List<Stock> mStockList) {
        this.mContext = mContext;
        this.mStockList = mStockList;
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

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        View v = View.inflate(mContext, R.layout.item_stock_list, null);
        TextView tvStock = (TextView)v.findViewById(R.id.tv_stock_name);
        TextView tvCierre = (TextView)v.findViewById(R.id.tv_cierre);
        TextView tvDescription = (TextView)v.findViewById(R.id.tv_description);
        final ImageView ivIcono = (ImageView)v.findViewById(R.id.imageView2);

        mStockName = mStockList.get(position).getDescription();
        mCierre = String.format(Locale.US, "%.3f", mStockList.get(position).getCierre());
        mDescripcion = mStockList.get(position).getDescription();

        tvStock.setText(mStockName);
        tvCierre.setText(mCierre);
        tvDescription.setText(mDescripcion);
        ivIcono.setImageResource(R.drawable.corazon_rojo);


        ivIcono.hasOnClickListeners();

        ivIcono.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mStock = mStockList.get(position).getSimbolo();

                mMetodo = "DeleteRemoteFavouriteStock";
                ThreadCreation threadCreation = new ThreadCreation();
                threadCreation.execute().toString();
                ivIcono.setImageResource(R.drawable.corazon_contorno);
                mStockList.get(position).setFavorito(0);

                mStockList.remove(position);
                notifyDataSetChanged();

            }
        });

        mTendencia = mStockList.get(position).getTendencia();

        if ( mTendencia == 1001 ){
            tvCierre.setTextColor(Color.RED);
        } else if ( mTendencia == 1002 ){
            tvCierre.setTextColor(Color.GREEN);
        } else if ( mTendencia == 1003 ){
            tvCierre.setTextColor(Color.BLUE);
        } else {
            tvCierre.setTextColor(Color.BLUE);
        }

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
