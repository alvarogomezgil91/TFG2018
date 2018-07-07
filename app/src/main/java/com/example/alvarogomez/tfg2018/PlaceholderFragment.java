package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alvarogomez.remoteDB.RemoteStocksData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 04/07/2018.
 */

public class PlaceholderFragment extends Fragment {

    private ListView lvStock;
    private StockListAdapter adapter;
    private List<Stock> mStockList;

    public static String mMetodo;
    public static String mURL;
    View view;



    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMainFooterAdapter;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(null, LinearLayoutManager.HORIZONTAL, false);


    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int sectionNumber) {

        PlaceholderFragment fragment = new PlaceholderFragment();

        mMetodo = "GetRemoteStocksData";
        mURL = Constants.GET_INDEX_STOCKS_DATA_URL;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        lvStock = (ListView)view.findViewById(R.id.listview_product);
        lvStock.setVerticalScrollBarEnabled(false);

        mStockList = new ArrayList<>();

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();


        lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Do something
                //Ex: display msg with product id get from view.getTag
                Stock stock = mStockList.get(position);

                Toast.makeText(view.getContext(), "Clicked product id =" + view.getTag(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), TestActivity.class);


                intent.putExtra("simbolo", stock.getStockName());
                startActivity(intent);


            }
        });



        return view;
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
                method = RemoteStocksData.class.getMethod(mMetodo);
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

                GraphicData stockData;

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
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            StockListAdapter mAdapter = new StockListAdapter(view.getContext(), mStockList);
            lvStock.setAdapter(mAdapter);




        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }


    }

}
