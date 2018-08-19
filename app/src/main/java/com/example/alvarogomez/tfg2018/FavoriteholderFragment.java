package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.alvarogomez.remoteDB.RemoteFavouriteStocksData;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 07/07/2018.
 */

public class FavoriteholderFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private ListView lvStock;
    private List<Stock> mStockList;
    private static final String ARG_SECTION_NUMBER = "section_number";
    View view;
    FavoriteStockListAdapter mAdapter;
    private static String mMetodo;

    private List<Stock> filteredStockValues;
    private Context mContext;
    List<String> mStockListNames;

    public FavoriteholderFragment(){
    }

    public static FavoriteholderFragment newInstance(int sectionNumber) {

        mMetodo = Constants.GET_REMOTE_FAVOURITE_STOCKS_DATA;
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
        mContext = getActivity();
        setHasOptionsMenu(true);

        lvStock = (ListView)view.findViewById(android.R.id.list);
        lvStock.setVerticalScrollBarEnabled(false);

        mAdapter = new FavoriteStockListAdapter(view.getContext(), mStockList);

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();

        lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Stock stock = mStockList.get(position);

                Intent intent = new Intent(getActivity(), StockViewPagerActivity.class);
                intent.putExtra("simbolo", stock.getStockName());
                startActivity(intent);

            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();

    }

    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId) {

        MenuItem item = menu.findItem(menuItemId);
        SpannableStringBuilder builder = new SpannableStringBuilder("   " + getResources().getString(labelId));
        builder.setSpan(new ImageSpan(mContext, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        setIconInMenu(menu, R.id.item_log_out, R.string.log_out, R.drawable.ic_action_log_out);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(Constants.SEARCH);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.item_search:
                return true;
            case R.id.item1:
                return true;
            case R.id.item2:
                return true;
            case R.id.item_log_out:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("logOut", "1");
                startActivity(intent);
                getActivity().finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        filteredStockValues = new ArrayList<>();

        int position = 0;
        for (String value : mStockListNames) {

            if (value.toLowerCase().contains(newText.toLowerCase())) {
                filteredStockValues.add(mStockList.get(position));
            }
            position++;
        }

        StockListAdapter mAdapter = new StockListAdapter(mContext, filteredStockValues);
        lvStock.setAdapter(mAdapter);
        lvStock.invalidate();

        return false;
    }

    public void resetSearch() {

        StockListAdapter mAdapter = new StockListAdapter(mContext, mStockList);
        lvStock.setAdapter(mAdapter);
        lvStock.invalidate();

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    public class ThreadCreation extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            List<Stock> stockDataList = new ArrayList<Stock>();
            java.lang.reflect.Method method = null;

            mStockList = new ArrayList<>();
            mStockListNames = new ArrayList<>();

            try {
                method = RemoteFavouriteStocksData.class.getMethod(mMetodo);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            String userName = "Alvaro1";
            RemoteFavouriteStocksData remoteStocksData = new RemoteFavouriteStocksData(userName);


            try{
                stockDataList = (List<Stock>) method.invoke(remoteStocksData);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            int cont = 0;

            for (ListIterator<Stock> iter = stockDataList.listIterator(); iter.hasNext(); ){

                Stock stockData;

                stockData = stockDataList.get(cont);

                stockData = iter.next();
                String simbolo = stockData.getStockName();
                float cierre = stockData.getCierre();
                int tendencia = stockData.getTendencia();

                mStockList.add(new Stock(cont, simbolo, cierre, simbolo + " desc", 1, tendencia));
                mStockListNames.add(simbolo);
                cont++;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            mAdapter = new FavoriteStockListAdapter(getContext(), mStockList);
            lvStock.setAdapter(mAdapter);
            lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Stock stock = mStockList.get(position);

                    Intent intent = new Intent(getActivity(), StockViewPagerActivity.class);
                    intent.putExtra("simbolo", stock.getStockName());
                    startActivity(intent);

                }
            });

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }

    }

}
