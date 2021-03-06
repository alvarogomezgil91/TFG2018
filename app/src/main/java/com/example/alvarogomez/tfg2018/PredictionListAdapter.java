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
    private String mNombreStock;
    private String mFecha;
    private String mCierre;
    private String mPorcentaje;
    private int mTendencia;
    private String mStock;
    private float apertura;
    private float cierre;
    private float diferencia;
    private float porcentaje;
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
        TextView tvCierre = (TextView)v.findViewById(R.id.tv_cierre);
        TextView tvFecha = (TextView)v.findViewById(R.id.tv_fecha);
        TextView tvPorcentaje = (TextView)v.findViewById(R.id.tv_porcentaje);
        final ImageView ivIcono = (ImageView)v.findViewById(R.id.imageView2);

        mNombreStock = mStockList.get(position).getNombreStock();
        String fechaAux = mStockList.get(position).getFecha();
        mFecha = fechaAux.substring(8,10) + "-" + fechaAux.substring(5,7) + "-" + fechaAux.substring(0,4);

        cierre = mStockList.get(position).getCierre();
        apertura = mStockList.get(position).getApertura();
        diferencia = cierre - apertura;
        porcentaje = diferencia / 100;

        mCierre = String.format(Locale.US, "%.3f", cierre);

        tvStock.setText(mNombreStock);
        tvCierre.setText(mCierre);
        tvFecha.setText(mFecha);

        mTendencia = mStockList.get(position).getTendencia();

        if ( cierre < apertura ){
            tvPorcentaje.setTextColor(Color.RED);
            mPorcentaje = String.format(Locale.US, "%.2f", diferencia) + " (" + String.format(Locale.US, "%.2f", porcentaje) + "%)";
            tvPorcentaje.setText(mPorcentaje);
            ivIcono.setImageResource(R.drawable.flecha_baja);
        } else if ( cierre > apertura ){
            tvPorcentaje.setTextColor(Color.GREEN);
            mPorcentaje = "+" + String.format(Locale.US, "%.2f", diferencia) + " (+" + String.format(Locale.US, "%.2f", porcentaje) + "%)";
            tvPorcentaje.setText(mPorcentaje);
            ivIcono.setImageResource(R.drawable.flecha_alza);
        } else if ( cierre == apertura ){
            tvPorcentaje.setTextColor(Color.BLUE);
            mPorcentaje = "~" + String.format(Locale.US, "%.2f", diferencia) + " ~(" + String.format(Locale.US, "%.2f", porcentaje) + "%)";
            tvPorcentaje.setText(mPorcentaje);
            ivIcono.setImageResource(R.drawable.igual);
        } else {
            tvCierre.setTextColor(Color.BLUE);
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
