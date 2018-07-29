package com.example.alvarogomez.tfg2018;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.example.alvarogomez.remoteDB.RemoteStocksData;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {


    private ListView lvStock;
    private StockListAdapter adapter;
    public List<Stock> mStockList;

    String metodo = "GetRemoteStocksData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        lvStock = (ListView)findViewById(R.id.listview_product);
        lvStock.setVerticalScrollBarEnabled(false);


        MainActivity.ThreadCreation threadCreation = new MainActivity.ThreadCreation();
        threadCreation.execute().toString();


        //Init adapter
        MainActivity mainActivity = new MainActivity();
        Context context = mainActivity.mainActivityContext();

        StockListAdapter mAdapter = new StockListAdapter(context, mStockList);
        lvStock.setAdapter(mAdapter);



        lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Do something
                //Ex: display msg with product id get from view.getTag
                Stock stock = mStockList.get(position);
                Toast.makeText(getApplicationContext(), "Clicked product id =" + view.getTag(), Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getBaseContext(), TestActivity.class);
                Intent intent = new Intent(getBaseContext(), Test2Activity.class);


                intent.putExtra("simbolo", stock.getStockName());
                startActivity(intent);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.item1:
                Toast.makeText(this, "Item 1", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item 2", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Item 3", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }





    public class ThreadCreation extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            List<GraphicData> stockDataList = new ArrayList<GraphicData>();
            //RemoteStocksData remoteStocksData = new RemoteStocksData();
            //stockDataList = remoteStocksData.GetRemoteStocksData();



            MainActivity mainActivity = new MainActivity();
            mainActivity.lanzaView();


            mStockList = new ArrayList<>();

            java.lang.reflect.Method method = null;
            try {
                method = RemoteStocksData.class.getMethod(metodo);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            RemoteStocksData remoteStocksData = new RemoteStocksData();


            try{
                stockDataList = (List<GraphicData>) method.invoke(remoteStocksData);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }


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

    public Context mainActivityContext () {
        return this;
    }

    public void showData () throws NoSuchMethodException {

        lvStock = (ListView)findViewById(R.id.listview_product);
        lvStock.setVerticalScrollBarEnabled(false);

        java.lang.reflect.Method method = MainActivity.class.getMethod("llamaThread");
        MainActivity mainActivity = new MainActivity();


        try{

            method.invoke(mainActivity);

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }



    }


    public void lanzaView (){

        lvStock = (ListView)findViewById(R.id.listview_product);
        lvStock.setVerticalScrollBarEnabled(false);

    }






}
