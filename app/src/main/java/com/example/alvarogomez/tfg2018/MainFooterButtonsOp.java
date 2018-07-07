package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;

import com.example.alvarogomez.remoteDB.RemoteStocksData;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 30/06/2018.
 */

public class MainFooterButtonsOp extends AppCompatActivity {

    ListView lvStock = (ListView) findViewById(R.id.listview_product);
    StockListAdapter adapter;
    List<Stock> mStockList;
    String buttonIdActive;

    String mURL = Constants.GET_INDEX_STOCKS_DATA_URL;


    public void setCheckedSetting(Context context, String value)
    {
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("MainFooterButtonIdActive", value);
        editor.commit();
    }

    public String getCheckedSetting(Context context)
    {
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        return settings.getString("MainFooterButtonIdActive", "");
    }

    public void indicesButtonOp (){



        System.out.println("Boton índices");
        mStockList = new ArrayList<>();
        String metodo = "GetRemoteStocksData";

        MainActivity.ThreadCreation threadCreation = new MainActivity().new ThreadCreation();
        threadCreation.execute().toString();
        StockListAdapter mAdapter = new StockListAdapter(this,mStockList);
        lvStock.setAdapter(mAdapter);


        /*

        if (!buttonIdActive.equals("0")) {

            System.out.println("Boton índices");


            lvStock.setVerticalScrollBarEnabled(false);



            mStockList = new ArrayList<>();
            //Add sample data for list
            //We can get data from DB, webservice here

            MainFooterButtonsOp.IndicesThreadCreation threadCreation = new MainFooterButtonsOp.IndicesThreadCreation();
            threadCreation.execute().toString();



            //Init adapter
            adapter = new StockListAdapter(getApplicationContext(), mStockList);
            lvStock.setAdapter(adapter);

            //setCheckedSetting(context, "0");


        }
        */


    }
    public void prediccionesButtonOp (){

        Context context = new MainActivity().getApplicationContext();

        buttonIdActive = "a"; //getCheckedSetting(context);

        if (!buttonIdActive.equals("1")) {
            System.out.println("Boton predicciones");

            //setCheckedSetting(context, "1");
        }

    }
    public void favoritosButtonOp (){

        Context context = new MainActivity().getApplicationContext();

        buttonIdActive = "a"; //getCheckedSetting(context);

        if (!buttonIdActive.equals("2")) {
            System.out.println("Boton favoritos");

            //setCheckedSetting(context, "2");
        }
    }
    public void noticiasButtonOp (){

        Context context = new MainActivity().getApplicationContext();

        buttonIdActive = "a"; //getCheckedSetting(context);

        if (!buttonIdActive.equals("3")) {
            System.out.println("Boton noticias");

            //setCheckedSetting(context, "3");
        }
    }




    private class IndicesThreadCreation extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            List<GraphicData> stockDataList = new ArrayList<GraphicData>();
            RemoteStocksData remoteStocksData = new RemoteStocksData();
            stockDataList = remoteStocksData.GetRemoteStocksData();


            int cont = 0;



            for (ListIterator<GraphicData> iter = stockDataList.listIterator(); iter.hasNext(); ){

                GraphicData stockData = new GraphicData();

                stockData = stockDataList.get(cont);

                stockData = iter.next();
                String simbolo = stockData.getSimbolo();
                float cierre = stockData.getCierre();

                mStockList.add(new Stock(cont, simbolo, cierre, simbolo + " desc"));
                cont++;

            }


            return null;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }


    }







}
