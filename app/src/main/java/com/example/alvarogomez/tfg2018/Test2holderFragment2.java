package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alvarogomez.ChartFormatter.MyXAxisValueFormatter;
import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.example.alvarogomez.remoteDB.RemoteStocksData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 14/07/2018.
 */

public class Test2holderFragment2 extends Fragment {

    public static String mSimbolo;
    private static int mColor;
    public static String mMetodo;

    List<GraphicData> graphicDataList;
    private LineChart mChart;
    private LineChart mChart1;
    private LineData data;

    View view;

    private static final String ARG_SECTION_NUMBER = "section_number2";

    public Test2holderFragment2() {
    }

    public static Test2holderFragment2 newInstance(int sectionNumber, int color, String simbolo) {

        Test2holderFragment2 fragment = new Test2holderFragment2();

        System.out.println("el color es " + color);

        mColor = color;
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

        System.out.println("Paso por el onCreateView");
        view = inflater.inflate(R.layout.fragment_view_test2, container, false);


        mChart = (LineChart) view.findViewById(R.id.linechart);
        mChart.setDragXEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        mChart1 = (LineChart) view.findViewById(R.id.linechart2);
        mChart1.setDragXEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setPinchZoom(true);


        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

                //mChart1.setVisibleXRange(mChart.getHighestVisibleX(), mChart.getLowestVisibleX());
                //mChart1.invalidate();

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                mChart1.setVisibleXRange(mChart.getHighestVisibleX(), mChart.getLowestVisibleX());
                mChart1.invalidate();

            }
        });


        mChart1.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                mChart.setScaleX(scaleX);

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });




        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        System.out.println("Me incio");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Me resumo");

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Me estoy parando" );
    }


    private class ThreadCreation extends AsyncTask<Void, Integer, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            Boolean credentialsOK = false;



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

                yValues.add(new Entry(cont, yAxis, 0));

                cont++;


            }


            xValues = xList.toArray(new String[xList.size()]);

            LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
            set1.setFillAlpha(110);
            set1.setValueTextSize(0f);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));

            xAxis.setGranularity(1f);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            set1.setColors(mColor);

            dataSets.add(set1);
            //dataSets.add(set2);



            data = new LineData(dataSets);





            credentialsOK = true;


            return credentialsOK;
        }


        protected void onProgressUpdate(Integer values){
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Boolean credentialsOK) {


            mChart.setData(data);
            mChart1.setData(data);
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
