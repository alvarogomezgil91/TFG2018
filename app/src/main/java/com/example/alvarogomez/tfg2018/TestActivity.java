package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alvarogomez.ChartFormatter.MyXAxisValueFormatter;
import com.example.alvarogomez.ChartFormatter.MyYAxisValueFormatter;
import com.example.alvarogomez.localDB.LocalUserDB;
import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TestActivity extends AppCompatActivity {

    String simbolo = "";

    private LineChart mChart;
    private LineChart mChart1;
    private LineData data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        simbolo = getIntent().getStringExtra("simbolo");

        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setBorderColor(Color.BLACK);

        mChart1 = (LineChart) findViewById(R.id.linechart2);
        mChart1.setDragEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setPinchZoom(true);

        TestActivity.ThreadCreation threadCreation = new TestActivity.ThreadCreation();
        threadCreation.execute().toString();


        Button mLogOut = (Button) findViewById(R.id.button3);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TestActivity.this, LoginActivity.class);

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
                    TestActivity.ThreadCreation threadCreation = new TestActivity.ThreadCreation();
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

        //Ponemos las ejecuciones por orden de ejecución

        //Se ejecuta en el hilo principal. Se ejecuta antes de lo que se ejecuta en segundo plano
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //Se ejecuta en segundo plano.
        //El primer parametro del AsyncTask (Void), corresponde con este parametro de entrada
        //El tercer parametro del AsyncTask (Void), correponde con el parametro de salida
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
                //yValues2.add(new Entry(cont, yAxis-40));

                //System.out.println("######### Pares de punto: " + iter.toString() + xList.get(Integer.valueOf(iter.toString())) + "---" + yValues.get(Integer.valueOf(iter.toString())) + "---" + yValues2.get(Integer.valueOf(iter.toString())));

                cont++;




            }


            xValues = xList.toArray(new String[xList.size()]);

            LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
            set1.setFillAlpha(110);
            set1.setValueTextSize(0f);
            set1.setColors(Color.YELLOW);
            set1.setCircleColor(Color.GREEN);


            /*LineDataSet set2 = new LineDataSet(yValues2, "Data Set 2");
            set1.setFillAlpha(110);
            set1.setValueTextSize(10f);
            */

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));

            xAxis.setGranularity(1f);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            //dataSets.add(set2);


            data = new LineData(dataSets);


            credentialsOK = true;


            return credentialsOK;
        }

        //El segundo parametro del AsyncTask (Integer), corresponde con este parametro de entrada
        protected void onProgressUpdate(Integer values){
            super.onProgressUpdate(values);
        }

        //Se ejecuta cuando ya ha acabado la ejecucion del segundo hilo
        //El parametro de salida del doInBackground, coincide con el parametro de entrada (Void)
        @Override
        protected void onPostExecute(Boolean credentialsOK) {

            mChart.setData(data);
            mChart1.setData(data);







            /*Intent intent = new Intent(getApplicationContext(), TestActivity.class);
            Bundle b = new Bundle();





            b.putString("mensaje>", mensaje);
            intent.putExtras(b);

            finish();

            startActivity(intent);*/


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }


    }


}
