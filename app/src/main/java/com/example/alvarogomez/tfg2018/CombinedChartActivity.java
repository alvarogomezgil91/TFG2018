package com.example.alvarogomez.tfg2018;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.alvarogomez.ChartFormatter.MyXAxisValueFormatter;
import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CombinedChartActivity extends AppCompatActivity {

    String mMetodo = "GetRemoteGraphicData";
    String mSimbolo;
    List<GraphicData> graphicDataList;
    CombinedChart combinedChart;
    CombinedData combinedData;
    private LineData lineData;
    CandleDataSet cds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());
        setContentView(R.layout.activity_combined_chart);

        combinedChart = (CombinedChart) findViewById(R.id.combined_chart);
        mSimbolo = getIntent().getStringExtra("simbolo");

        CombinedChartActivity.ThreadCreation threadCreation = new CombinedChartActivity.ThreadCreation();
        threadCreation.execute().toString();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private class ThreadCreation extends AsyncTask<Void, Integer, Void> {

        String[] xValues;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

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

            ArrayList<CandleEntry> ceList = new ArrayList<>();
            ArrayList<Entry> yValues = new ArrayList<>();
            List<String> xList = new ArrayList<String>();
            GraphicData graphicData;

            float yAxis = 0;
            float shadowH = 0;
            float shadowL = 0;
            float open = 0;
            float close = 0;
            int cont = 0;

            for (ListIterator<GraphicData> iter = graphicDataList.listIterator(); iter.hasNext(); ){

                graphicData = iter.next();

                open = graphicData.getApertura();
                close = graphicData.getCierre();
                shadowH = graphicData.getMaximo();
                shadowL = graphicData.getMinimo();

                ceList.add(new CandleEntry(cont, shadowH, shadowL, open, close));
                xList.add(graphicData.getFecha().substring(5, 10));

                yAxis = Float.valueOf(graphicData.getMaximo());
                yValues.add(new Entry(cont, yAxis));

                cont++;

            }

            LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
            set1.setDrawCircles(false);
            set1.setFillAlpha(110);
            set1.setValueTextSize(0f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            lineData = new LineData(dataSets);
            xValues = xList.toArray(new String[xList.size()]);

            cds = new CandleDataSet(ceList, "Entries");
            cds.setColor(Color.WHITE);
            cds.setShadowColor(Color.WHITE);
            cds.setShadowWidth(0.9f);
            cds.setDecreasingColor(Color.RED);
            cds.setDecreasingPaintStyle(Paint.Style.FILL);
            cds.setIncreasingColor(Color.GREEN);
            cds.setIncreasingPaintStyle(Paint.Style.FILL);
            cds.setNeutralColor(Color.BLUE);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            combinedData = new CombinedData();

            XAxis xAxis = combinedChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));

            xAxis.setGranularity(1f);

            CandleData cd = new CandleData(cds);
            cd.setValueTextSize(0f);

            combinedData.setData(cd);
            combinedData.setData(lineData);
            combinedChart.setData(combinedData);
            combinedChart.setGridBackgroundColor(Color.LTGRAY);
            combinedChart.setBackgroundColor(Color.DKGRAY);
            combinedChart.setAutoScaleMinMaxEnabled(true);
            combinedChart.notifyDataSetChanged();
            combinedChart.invalidate();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

    }

}
