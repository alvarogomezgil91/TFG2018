package com.example.alvarogomez.tfg2018;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.alvarogomez.remoteDB.RemoteFavouriteStocksData;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 07/07/2018.
 */

public class FavoriteholderFragment extends Fragment{

    private ListView lvStock;
    private List<Stock> mStockList;
    private static final String ARG_SECTION_NUMBER = "section_number";
    View view;
    StockListAdapter mAdapter;

    public FavoriteholderFragment(){
    }

    public static FavoriteholderFragment newInstance(int sectionNumber) {

        FavoriteholderFragment fragment = new FavoriteholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        lvStock = (ListView)view.findViewById(R.id.listview_product);
        lvStock.setVerticalScrollBarEnabled(false);

        mStockList = new ArrayList<>();
        mAdapter = new StockListAdapter(view.getContext(), mStockList);

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();

        lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Stock stock = mStockList.get(position);

                Intent intent = new Intent(getActivity(), ViewPagerTest2Activity.class);
                intent.putExtra("simbolo", stock.getStockName());
                startActivity(intent);

            }
        });

        return view;

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa         onViewStateRestored");

    }

    @Override
    public void onStart(){
        super.onStart();
        System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa         onStart");

    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa         onResume");

        if (mStockList != null) {
            mAdapter.updateList(mStockList);
            mAdapter.notifyDataSetChanged();
        }

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
                String mMetodo = "GetRemoteFavouriteStocksData";
                method = RemoteFavouriteStocksData.class.getMethod(mMetodo);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            String userName = "Alvaro1";
            RemoteFavouriteStocksData remoteStocksData = new RemoteFavouriteStocksData(userName);


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

                mStockList.add(new Stock(cont, simbolo, cierre, simbolo + " desc", 1));
                cont++;

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

            mAdapter = new StockListAdapter(view.getContext(), mStockList);
            lvStock.setAdapter(mAdapter);

        }

    }

}
