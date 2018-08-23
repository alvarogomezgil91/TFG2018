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
 * Created by Alvaro Gomez on 20/08/2018.
 */

public class PredictionListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Stock> mStockList;
    private String mStockName;
    private float mApertura;
    private float mCierrePredecido;
    private String mDescripcion;
    private int mEsMercado = 0;
    private float porcentaje;
    private String mPorcentaje;
    private String mStock;
    Boolean comandoOk = false;
    String mMetodo;

    //Constructor

    public PredictionListAdapter(Context mContext, List<Stock> mStockList) {
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
        TextView tvCierrePredecido = (TextView)v.findViewById(R.id.tv_cierre);
        //TextView tvDescription = (TextView)v.findViewById(R.id.tv_description);
        final ImageView ivIcono = (ImageView)v.findViewById(R.id.imageView2);

        mStockName = mStockList.get(position).getDescription();
        mApertura = mStockList.get(position).getApertura();
        mCierrePredecido = mStockList.get(position).getCierre();
        mDescripcion = mStockList.get(position).getDescription();

        tvStock.setText(mDescripcion);
        tvCierrePredecido.setText(String.format(Locale.US, "%.3f", mStockList.get(position).getCierre()));

        mEsMercado = mStockList.get(position).getEsMercado();

        porcentaje = ( mApertura - mCierrePredecido) / 100;
        mPorcentaje = String.format(Locale.US, "%.2f", porcentaje) + " %";

        //tvDescription.setText(mPorcentaje);

        if ( mApertura > mCierrePredecido){
            tvCierrePredecido.setTextColor(Color.RED);
            ivIcono.setImageResource(R.drawable.flecha_baja);
        } else if ( mApertura < mCierrePredecido ){
            tvCierrePredecido.setTextColor(Color.GREEN);
            ivIcono.setImageResource(R.drawable.flecha_alza);
        } else {
            tvCierrePredecido.setTextColor(Color.BLUE);
            ivIcono.setImageResource(R.drawable.igual);
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
