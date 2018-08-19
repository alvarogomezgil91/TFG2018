package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import com.example.alvarogomez.remoteDB.RemoteStocksData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

/**
 * Created by Alvaro Gomez on 18/08/2018.
 */

public class StockPredictionholderFragment extends Fragment {

    private MaterialCalendarView mMCalendarView;
    private Context mContext;
    private View view;
    private ListView lvStock;
    private List<Stock> mStockList;
    public static String mMetodo;
    public static String mURL;
    private List<String> mStockListNames;
    private static final String ARG_SECTION_NUMBER = "section_number2";

    public StockPredictionholderFragment() {
    }

    public static StockPredictionholderFragment newInstance(int sectionNumber, String simbolo) {

        StockPredictionholderFragment fragment = new StockPredictionholderFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        view = inflater.inflate(R.layout.fragment_prediction_calendar, container, false);
        mContext = getActivity();

        mMCalendarView = (MaterialCalendarView) view.findViewById(R.id.prediction_calendar);

        mMCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(1900,1, 1))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();




        final CalendarDay currentDay = CalendarDay.from(2018, 0, 4);

        mMCalendarView.addDecorators(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.equals(currentDay);
            }

            @Override
            public void decorate(DayViewFacade view) {
                //view.addSpan(new ForegroundColorSpan(Color.RED));
                //view.addSpan(new BackgroundColorSpan(Color.GREEN));
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.red_circle);
                view.setBackgroundDrawable(drawable);
            }
        });

        mMCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

            }
        });

        //ThreadCreation threadCreation = new ThreadCreation();
        //threadCreation.execute().toString();

        return view;
    }

    public class ThreadCreation extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mStockList = new ArrayList<>();
            mStockListNames = new ArrayList<>();

            List<Stock> stockDataList = new ArrayList<Stock>();
            java.lang.reflect.Method method = null;

            try {
                method = RemoteStocksData.class.getMethod(mMetodo);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            RemoteStocksData remoteStocksData = new RemoteStocksData();

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
                String simbolo = stockData.getSimbolo();
                float cierre = stockData.getCierre();
                int favorito = stockData.getFavorito();
                int tendencia = stockData.getTendencia();
                int esMercado = stockData.getEsMercado();


                mStockList.add(new Stock(cont, simbolo, cierre, simbolo + " desc", favorito, tendencia, esMercado));
                mStockListNames.add(simbolo);
                cont++;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);






        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }


    }













}
