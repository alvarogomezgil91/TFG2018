package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.alvarogomez.ChartFormatter.MyXAxisValueFormatter;
import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.example.alvarogomez.remoteDB.RemoteTecnicoStocksData;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Alvaro Gomez on 15/08/2018.
 */

public class MainGraphicholderFragment extends Fragment {

    public static String mSimbolo;
    private static int mLineColor = R.color.cyan;
    private static int mAreaColor = R.color.light_blue;
    public static String mMetodoGraphic;
    public static String mMetodoTecnico;
    private Context mContext;

    static List<GraphicData> graphicDataList;
    static List<TecnicoStock> tecnicoDataList;
    private static LineChart mChartGraphic;
    private static CombinedChart mChartTecnico;
    private static LineData dataGraphic;
    private static CombinedData dataTecnico;

    static LineDataSet setTecnicoEMA26;
    static LineDataSet setTecnicoEMA12;
    static LineDataSet setTecnicoMACD;
    static LineDataSet setTecnicoSENAL;
    static LineData lineDataTecnico;
    static BarData barDataTecnico;

    View view;

    private static final String ARG_SECTION_NUMBER = "section_number2";

    public MainGraphicholderFragment() {
    }

    public static MainGraphicholderFragment newInstance(int sectionNumber, String simbolo) {

        MainGraphicholderFragment fragment = new MainGraphicholderFragment();

        mSimbolo = simbolo;
        mMetodoGraphic = Constants.GET_REMOTE_GRAPHIC_DATA;
        mMetodoTecnico = Constants.GET_REMOTE_TECNICO_STOCKS_DATA;

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
        mContext = getActivity();
        setHasOptionsMenu(true);

        setUpGraphicChart();
        setUpTecnicoCHart();

        mChartGraphic.setOnChartGestureListener(new CoupleChartGestureListener(mChartGraphic, new Chart[] { mChartTecnico}));
        mChartTecnico.setOnChartGestureListener(new CoupleChartGestureListener(mChartTecnico, new Chart[] { mChartGraphic}));

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

            Intent intent = new Intent(view.getContext(), CombinedChartActivity.class);

            intent.putExtra("simbolo", mSimbolo);
            startActivity(intent);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        //lock screen to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void onPause() {
        super.onPause();
        //set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId) {

        MenuItem item = menu.findItem(menuItemId);
        SpannableStringBuilder builder = new SpannableStringBuilder("   " + getResources().getString(labelId));
        builder.setSpan(new ImageSpan(mContext, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_graphic_menu, menu);
        setIconInMenu(menu, R.id.item_log_out, R.string.log_out, R.drawable.ic_action_log_out);
        menu.removeItem(R.id.item_search);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.item_MA26:
                if (item.isChecked()) {
                    item.setChecked(false);
                    dataGraphic.removeDataSet(dataGraphic.getDataSetByLabel(Constants.DATA_SET_EMA26, false));
                } else {
                    item.setChecked(true);
                    dataGraphic.addDataSet(setTecnicoEMA26);
                }
                mChartGraphic.notifyDataSetChanged();
                mChartGraphic.invalidate();
                return true;
            case R.id.item_MA12:
                if (item.isChecked()) {
                    item.setChecked(false);
                    dataGraphic.removeDataSet(dataGraphic.getDataSetByLabel(Constants.DATA_SET_EMA12, false));
                } else {
                    item.setChecked(true);
                    dataGraphic.addDataSet(setTecnicoEMA12);
                }
                mChartGraphic.notifyDataSetChanged();
                mChartGraphic.invalidate();
                return true;
            case R.id.item_MACD:
                if (item.isChecked()) {
                    item.setChecked(false);
                    lineDataTecnico.removeDataSet(lineDataTecnico.getDataSetByLabel(Constants.DATA_SET_MACD, false));
                    dataTecnico.clearValues();
                    dataTecnico.setData(lineDataTecnico);
                    dataTecnico.setData(barDataTecnico);
                } else {
                    item.setChecked(true);
                    lineDataTecnico.addDataSet(setTecnicoMACD);
                    dataTecnico.clearValues();
                    dataTecnico.setData(lineDataTecnico);
                    dataTecnico.setData(barDataTecnico);
                }
                mChartTecnico.notifyDataSetChanged();
                mChartTecnico.invalidate();
                return true;
            case R.id.item_SENAL:
                if (item.isChecked()) {
                    item.setChecked(false);
                    lineDataTecnico.removeDataSet(lineDataTecnico.getDataSetByLabel(Constants.DATA_SET_SENAL, false));
                    dataTecnico.clearValues();
                    dataTecnico.setData(lineDataTecnico);
                    dataTecnico.setData(barDataTecnico);
                } else {
                    item.setChecked(true);
                    lineDataTecnico.addDataSet(setTecnicoSENAL);
                    dataTecnico.clearValues();
                    dataTecnico.setData(lineDataTecnico);
                    dataTecnico.setData(barDataTecnico);
                }
                mChartTecnico.notifyDataSetChanged();
                mChartTecnico.invalidate();
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

    public void setUpGraphicChart() {
        mChartGraphic = (LineChart) view.findViewById(R.id.graphic_chart);
        mChartGraphic.setDrawBorders(true);
        mChartGraphic.setBorderWidth((float) 2.0);
        mChartGraphic.setDragEnabled(true);
        mChartGraphic.setAutoScaleMinMaxEnabled(true);
        mChartGraphic.setScaleEnabled(true);
        mChartGraphic.setPinchZoom(true);
        mChartGraphic.setNoDataText(Constants.NO_DATA_SET);
        mChartGraphic.setDoubleTapToZoomEnabled(false);
        mChartGraphic.setAutoScaleMinMaxEnabled(true);
        mChartGraphic.getDescription().setEnabled(false);
        YAxis rightAxis = mChartGraphic.getAxisRight();
        rightAxis.setEnabled(false);
        Legend legendGraphic = mChartGraphic.getLegend();
        legendGraphic.setEnabled(true);
        LegendEntry legendEntryCierre = new LegendEntry();
        legendEntryCierre.label = Constants.DATA_SET_CIERRE;
        legendEntryCierre.formColor = Color.WHITE;
        LegendEntry legendEntryMA26 = new LegendEntry();
        legendEntryMA26.label = Constants.DATA_SET_MA26;
        legendEntryMA26.formColor = Constants.COLOR_EMA26;
        LegendEntry legendEntryMA12 = new LegendEntry();
        legendEntryMA12.label = Constants.DATA_SET_MA12;
        legendEntryMA12.formColor = Constants.COLOR_EMA12;
        legendGraphic.setCustom(Arrays.asList(legendEntryCierre, legendEntryMA26, legendEntryMA12));
        mChartGraphic.invalidate();
    }

    public void setUpTecnicoCHart() {
        mChartTecnico = (CombinedChart) view.findViewById(R.id.tecnico_chart);
        mChartTecnico.setDrawBorders(true);
        mChartTecnico.setBorderWidth((float) 2.0);
        mChartTecnico.setDragEnabled(true);
        mChartTecnico.setAutoScaleMinMaxEnabled(true);
        mChartTecnico.setScaleEnabled(true);
        mChartTecnico.setPinchZoom(true);
        mChartTecnico.setNoDataText(Constants.NO_DATA_SET);
        mChartTecnico.setDoubleTapToZoomEnabled(false);
        mChartTecnico.setAutoScaleMinMaxEnabled(true);
        YAxis rightAxis = mChartTecnico.getAxisRight();
        rightAxis.setEnabled(false);
        Legend legendTecnicos = mChartTecnico.getLegend();
        legendTecnicos.setEnabled(true);
        LegendEntry legendEntryMACD = new LegendEntry();
        legendEntryMACD.label = Constants.DATA_SET_MACD;
        legendEntryMACD.formColor = Constants.COLOR_MACD;
        LegendEntry legendEntrySENAL = new LegendEntry();
        legendEntrySENAL.label = Constants.DATA_SET_SENAL;
        legendEntrySENAL.formColor = Constants.COLOR_SENAL;
        LegendEntry legendEntryHISTOGRAMA1 = new LegendEntry();
        legendEntryHISTOGRAMA1.label = "";
        legendEntryHISTOGRAMA1.formColor = Constants.COLOR_HISTOGRAMA_POSITIVO;
        LegendEntry legendEntryHISTOGRAMA2 = new LegendEntry();
        legendEntryHISTOGRAMA2.label = Constants.DATA_SET_HISTOGRAMA;
        legendEntryHISTOGRAMA2.formColor = Constants.COLOR_HISTOGRAMA_NEGATIVO;
        legendTecnicos.setCustom(Arrays.asList(legendEntryMACD, legendEntrySENAL, legendEntryHISTOGRAMA1, legendEntryHISTOGRAMA2));
        mChartTecnico.invalidate();
    }

    private static class ThreadCreation extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            java.lang.reflect.Method methodStock = null;

            try {
                methodStock = RemoteGraphicData.class.getMethod(mMetodoGraphic);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            RemoteGraphicData remoteGraphicData = new RemoteGraphicData(mSimbolo);

            try{
                graphicDataList = (List<GraphicData>) methodStock.invoke(remoteGraphicData);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            ArrayList<Entry> yValuesGraphic = new ArrayList<>();
            List<String> xListGraphic = new ArrayList<String>();
            String[] xValuesGraphic;

            GraphicData graphicData;
            float yAxisGraphic = 0;
            int contGraphic = 0;
            String xDia;
            String xMes;

            for (ListIterator<GraphicData> iter = graphicDataList.listIterator(); iter.hasNext(); ){

                graphicData = iter.next();

                xDia = graphicData.getFecha().replace("-","").substring(6, 8);
                xMes = graphicData.getFecha().replace("-","").substring(4, 6);
                xListGraphic.add(xDia + "/" + xMes);
                yAxisGraphic = graphicData.getCierre();

                yValuesGraphic.add(new Entry(contGraphic, yAxisGraphic));

                contGraphic++;

            }

            xValuesGraphic = xListGraphic.toArray(new String[xListGraphic.size()]);

            LineDataSet setGraphic = new LineDataSet(yValuesGraphic, Constants.DATA_SET_CIERRE);

            setGraphic.setDrawCircles(false);
            setGraphic.setFillAlpha(110);
            setGraphic.setValueTextSize(0f);

            XAxis xAxisGraphic = mChartGraphic.getXAxis();
            xAxisGraphic.setValueFormatter(new MyXAxisValueFormatter(xValuesGraphic));
            xAxisGraphic.setGranularity(0.1f);

            ArrayList<ILineDataSet> dataSetsGraphic = new ArrayList<>();
            setGraphic.setColors(mLineColor);

            setGraphic.setDrawFilled(true);

            java.lang.reflect.Method methodTecnico = null;

            try {
                methodTecnico = RemoteTecnicoStocksData.class.getMethod(mMetodoTecnico);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            RemoteTecnicoStocksData remoteTecnicoStocksData = new RemoteTecnicoStocksData(mSimbolo);

            try{
                tecnicoDataList = (List<TecnicoStock>) methodTecnico.invoke(remoteTecnicoStocksData);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }


            ArrayList<Entry> yValuesTecnicoEMA26 = new ArrayList<>();
            ArrayList<Entry> yValuesTecnicoEMA12 = new ArrayList<>();
            ArrayList<Entry> yValuesTecnicoMACD = new ArrayList<>();
            ArrayList<Entry> yValuesTecnicoSENAL = new ArrayList<>();
            ArrayList<BarEntry> yValuesTecnicoHISTOGRAMA = new ArrayList<>();
            List<String> xListTecnico = new ArrayList<String>();
            String[] xValuesTecnico;

            TecnicoStock tecnicoStock;
            float yAxisTecnicoEMA26 = 0;
            float yAxisTecnicoEMA12 = 0;
            float yAxisTecnicoMACD = 0;
            float yAxisTecnicoSENAL = 0;
            float yAxisTecnicoHISTOGRAMA = 0;
            int contTecnico = 0;
            List<Integer> colorTecnicoHISTOGRAMA = new ArrayList<>();

            for (ListIterator<TecnicoStock> iter = tecnicoDataList.listIterator(); iter.hasNext(); ){

                tecnicoStock = iter.next();
                xDia = tecnicoStock.getFecha().replace("-","").substring(6, 8);
                xMes = tecnicoStock.getFecha().replace("-","").substring(4, 6);
                xListTecnico.add(xDia + "/" + xMes);
                yAxisTecnicoEMA26 = tecnicoStock.getEMA26();
                yAxisTecnicoEMA12 = tecnicoStock.getEMA12();
                yAxisTecnicoMACD = tecnicoStock.getMACD();
                yAxisTecnicoSENAL = tecnicoStock.getSENAL();
                yAxisTecnicoHISTOGRAMA = tecnicoStock.getHISTOGRAMA();

                if (yAxisTecnicoHISTOGRAMA > 0){
                    colorTecnicoHISTOGRAMA.add(Constants.COLOR_HISTOGRAMA_POSITIVO);
                } else if (yAxisTecnicoHISTOGRAMA < 0){
                    colorTecnicoHISTOGRAMA.add(Constants.COLOR_HISTOGRAMA_NEGATIVO);
                } else {
                    colorTecnicoHISTOGRAMA.add(Constants.COLOR_HISTOGRAMA_NEUTRO);
                }

                yValuesTecnicoEMA26.add(new Entry(contTecnico, yAxisTecnicoEMA26));
                yValuesTecnicoEMA12.add(new Entry(contTecnico, yAxisTecnicoEMA12));
                yValuesTecnicoMACD.add(new Entry(contTecnico, yAxisTecnicoMACD));
                yValuesTecnicoSENAL.add(new Entry(contTecnico, yAxisTecnicoSENAL));
                yValuesTecnicoHISTOGRAMA.add(new BarEntry(contTecnico, yAxisTecnicoHISTOGRAMA));

                contTecnico++;

            }

            xValuesTecnico = xListTecnico.toArray(new String[xListTecnico.size()]);

            setTecnicoEMA26 = new LineDataSet(yValuesTecnicoEMA26, Constants.DATA_SET_EMA26);
            setTecnicoEMA12 = new LineDataSet(yValuesTecnicoEMA12, Constants.DATA_SET_EMA12);
            setTecnicoMACD = new LineDataSet(yValuesTecnicoMACD, Constants.DATA_SET_MACD);
            setTecnicoSENAL = new LineDataSet(yValuesTecnicoSENAL, Constants.DATA_SET_SENAL);
            BarDataSet setTecnicoHISTOGRAMA = new BarDataSet(yValuesTecnicoHISTOGRAMA, Constants.DATA_SET_HISTOGRAMA);

            setTecnicoEMA26.setDrawCircles(false);
            setTecnicoEMA12.setDrawCircles(false);
            setTecnicoMACD.setDrawCircles(false);
            setTecnicoSENAL.setDrawCircles(false);
            setTecnicoEMA26.setFillAlpha(110);
            setTecnicoEMA12.setFillAlpha(110);
            setTecnicoMACD.setFillAlpha(110);
            setTecnicoSENAL.setFillAlpha(110);
            setTecnicoEMA26.setValueTextSize(0f);
            setTecnicoEMA12.setValueTextSize(0f);
            setTecnicoMACD.setValueTextSize(0f);
            setTecnicoSENAL.setValueTextSize(0f);
            setTecnicoHISTOGRAMA.setValueTextSize(0f);

            XAxis xAxisTecnico = mChartTecnico.getXAxis();
            xAxisTecnico.setValueFormatter(new MyXAxisValueFormatter(xValuesTecnico));
            xAxisTecnico.setGranularity(1f);

            setTecnicoEMA26.setColors(Constants.COLOR_EMA26);
            setTecnicoEMA12.setColors(Constants.COLOR_EMA12);
            setTecnicoMACD.setColors(Constants.COLOR_MACD);
            setTecnicoSENAL.setColors(Constants.COLOR_SENAL);
            setTecnicoHISTOGRAMA.setColors(colorTecnicoHISTOGRAMA);

            dataSetsGraphic.add(setGraphic);
            dataSetsGraphic.add(setTecnicoEMA26);
            dataSetsGraphic.add(setTecnicoEMA12);
            dataGraphic = new LineData(dataSetsGraphic);

            lineDataTecnico = new LineData();
            barDataTecnico = new BarData();

            lineDataTecnico.addDataSet(setTecnicoMACD);
            lineDataTecnico.addDataSet(setTecnicoSENAL);
            barDataTecnico.addDataSet(setTecnicoHISTOGRAMA);


            dataTecnico = new CombinedData();
            dataTecnico.setData(lineDataTecnico);
            dataTecnico.setData(barDataTecnico);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mChartGraphic.setData(dataGraphic);
            mChartTecnico.setData(dataTecnico);
            mChartGraphic.notifyDataSetChanged();
            mChartGraphic.invalidate();
            mChartTecnico.notifyDataSetChanged();
            mChartTecnico.invalidate();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

    }

}
