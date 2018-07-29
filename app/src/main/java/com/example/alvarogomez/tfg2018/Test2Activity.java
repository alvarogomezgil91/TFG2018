package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alvarogomez.ChartFormatter.MyXAxisValueFormatter;
import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Test2Activity extends AppCompatActivity {

    String simbolo = "";
    int color;

    private LineChart mChart;
    private LineChart mChart1;
    private LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        simbolo = getIntent().getStringExtra("simbolo");

        System.out.println("EEEEEEEEEEEAAAAAAAAAAAAAAAAAAOOOOOOOOOOOOOOO");


        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        mChart1 = (LineChart) findViewById(R.id.linechart2);
        mChart1.setDragEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setPinchZoom(true);

        color = Color.RED;

        Test2Activity.ThreadCreation threadCreation = new Test2Activity.ThreadCreation();
        threadCreation.execute().toString();


        Button mLogOut = (Button) findViewById(R.id.button3);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Test2Activity.this, LoginActivity.class);

                startActivity(intent);

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_activity_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        switch (item.getItemId()){
            case R.id.item1:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                }else{
                    // If item is unchecked then checked it
                    item.setChecked(true);
                    simbolo = "AENA.MC";
                    Test2Activity.ThreadCreation threadCreation = new Test2Activity.ThreadCreation();
                    threadCreation.execute().toString();
                }
                break;
            case R.id.item2:
                if(item.isChecked()){
                    // If item already checked then unchecked it
                    item.setChecked(false);
                }else{
                    // If item is unchecked then checked it
                    item.setChecked(true);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }





    private class ThreadCreation extends AsyncTask<Void, Integer, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            Boolean credentialsOK = false;

            List<GraphicData> graphicDataList;
            RemoteGraphicData remoteGraphicData = new RemoteGraphicData(simbolo);
            graphicDataList = remoteGraphicData.GetRemoteGraphicData();

            ArrayList<Entry> yValues = new ArrayList<>();
            ArrayList<Entry> yValues2 = new ArrayList<>();
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
            set1.setFillAlpha(110);
            set1.setValueTextSize(0f);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));

            xAxis.setGranularity(1f);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            set1.setColors(color);

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

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }


    }




}
