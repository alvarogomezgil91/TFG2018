package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.alvarogomez.ChartFormatter.MyXAxisValueFormatter;
import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 15/08/2018.
 */

public class MainGraphicholderFragment extends Fragment {

    public static String mSimbolo;
    private static int mLineColor = R.color.cyan;
    private static int mAreaColor = R.color.light_blue;
    public static String mMetodo;

    List<GraphicData> graphicDataList;
    private LineChart mChart;
    private LineChart mChart1;
    private LineData data;
    private LineData data2;

    View view;

    private static final String ARG_SECTION_NUMBER = "section_number2";

    public MainGraphicholderFragment() {
    }

    public static MainGraphicholderFragment newInstance(int sectionNumber, String simbolo) {

        MainGraphicholderFragment fragment = new MainGraphicholderFragment();

        mSimbolo = simbolo;
        mMetodo = "GetRemoteGraphicData";

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        view = inflater.inflate(R.layout.fragment_main_graphic, container, false);

        mChart = (LineChart) view.findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        mChart1 = (LineChart) view.findViewById(R.id.linechart2);
        mChart1.setDragEnabled(true);
        mChart1.setAutoScaleMinMaxEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setPinchZoom(true);

        mChart.setOnChartGestureListener(new CoupleChartGestureListener(mChart, new Chart[] { mChart1}));
        mChart1.setOnChartGestureListener(new CoupleChartGestureListener(mChart, new Chart[] { mChart}));

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            System.out.println("Nos ponemos de lao");

            Intent intent = new Intent(view.getContext(), CombinedChartActivity.class);

            intent.putExtra("simbolo", mSimbolo);
            startActivity(intent);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            System.out.println("Nos ponemos de normal");
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Me estoy parando" );
    }


    private class ThreadCreation extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            java.lang.reflect.Method method = null;

            try {
                method = RemoteGraphicData.class.getMethod(mMetodo);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            RemoteGraphicData remoteGraphicData = new RemoteGraphicData(mSimbolo);

            try{
                graphicDataList = (List<GraphicData>) method.invoke(remoteGraphicData);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            ArrayList<Entry> yValues = new ArrayList<>();
            List<String> xList = new ArrayList<String>();
            String[] xValues;

            GraphicData graphicData;
            float yAxis = 0;
            int cont = 0;

            for (ListIterator<GraphicData> iter = graphicDataList.listIterator(); iter.hasNext(); ){

                graphicData = iter.next();
                xList.add(graphicData.getFecha().replace("-","").substring(6, 8));
                yAxis = Float.valueOf(graphicData.getMaximo());

                yValues.add(new Entry(cont, yAxis));

                cont++;

            }

            xValues = xList.toArray(new String[xList.size()]);

            LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
            LineDataSet set2 = new LineDataSet(yValues, "Data Set 2");

            set1.setDrawCircles(false);
            set2.setDrawCircles(false);
            set1.setFillAlpha(110);
            set1.setValueTextSize(0f);
            set2.setFillAlpha(110);
            set2.setValueTextSize(0f);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
            xAxis.setGranularity(1f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            ArrayList<ILineDataSet> dataSets2 = new ArrayList<>();
            set1.setColors(mLineColor);
            set2.setColors(mLineColor);

            set1.setDrawFilled(true);
            set1.setFillColor(mAreaColor);
            set2.setDrawFilled(true);
            set2.setFillColor(mAreaColor);

            dataSets.add(set1);
            dataSets2.add(set2);

            data = new LineData(dataSets);
            data2 = new LineData(dataSets2);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mChart.setData(data);
            mChart1.setData(data2);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
            mChart1.notifyDataSetChanged();
            mChart1.invalidate();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

    }

}
