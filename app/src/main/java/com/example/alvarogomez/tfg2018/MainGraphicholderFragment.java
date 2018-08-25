package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
    private Context mContext;

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
        mMetodo = Constants.GET_REMOTE_GRAPHIC_DATA;

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

        mChart = (LineChart) view.findViewById(R.id.linechart);
        mChart.setDrawBorders(true);
        mChart.setBorderWidth((float) 2.0);
        mChart.setDragEnabled(true);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setNoDataText(Constants.NO_DATA_SET);
        mChart.setDoubleTapToZoomEnabled(false);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        mChart1 = (LineChart) view.findViewById(R.id.linechart2);
        mChart1.setDrawBorders(true);
        mChart1.setBorderWidth((float) 2.0);
        mChart1.setDragEnabled(true);
        mChart1.setAutoScaleMinMaxEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setPinchZoom(true);
        mChart1.setNoDataText(Constants.NO_DATA_SET);
        mChart1.setDoubleTapToZoomEnabled(false);

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
        inflater.inflate(R.menu.main_menu, menu);
        setIconInMenu(menu, R.id.item_log_out, R.string.log_out, R.drawable.ic_action_log_out);
        menu.removeItem(R.id.item_search);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

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

            LineDataSet set1 = new LineDataSet(yValues, Constants.DATA_SET_1);
            LineDataSet set2 = new LineDataSet(yValues, Constants.DATA_SET_2);

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
