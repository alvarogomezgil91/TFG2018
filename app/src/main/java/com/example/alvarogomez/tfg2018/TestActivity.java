package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    TextView texto;

    Button mLogOut;

    String simbolo = "";

    private LineChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        simbolo = getIntent().getStringExtra("simbolo");

        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        texto = (TextView)findViewById(R.id.testTextView);

        texto.setText("pa dentro");

        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        TestActivity.ThreadCreation threadCreation = new TestActivity.ThreadCreation();
        threadCreation.execute().toString();





        /*

        LocalUserDB localUserDB = new LocalUserDB(this, "LocalUserDB", null, 1);

        SQLiteDatabase db = localUserDB.getReadableDatabase();

        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<Entry> yValues2 = new ArrayList<>();
        List<String> xList = new ArrayList<String>();
        String[] xValues;


        if(db != null) {

            String querySQL = "";

            String sqlSelect = "SELECT date, close_value FROM stocks WHERE symbol = 'AAPL' order by date asc";

            Cursor cursor = db.rawQuery(sqlSelect, null);

            float yAxis = 0;

            cursor.moveToFirst();

            int cont = 0;

            while (cursor.isAfterLast() == false){

                //xAxis = Integer.valueOf(cursor.getString(0).replace("-", "").substring(6));
                xList.add(cursor.getString(0).replace("-", ""));
                yAxis = Float.valueOf(cursor.getString(1));

                yValues.add(new Entry(cont, yAxis));
                yValues2.add(new Entry(cont, yAxis-40));
                cursor.moveToNext();

                cont++;

            }
            
            xValues = xList.toArray(new String[xList.size()]);

            LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
            set1.setFillAlpha(110);
            set1.setValueTextSize(10f);

            LineDataSet set2 = new LineDataSet(yValues2, "Data Set 2");
            set1.setFillAlpha(110);
            set1.setValueTextSize(10f);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));


            xAxis.setGranularity(1f);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);


            LineData data = new LineData(dataSets);

            mChart.setData(data);


        }else {
            System.out.println("***** No existe la BBDD que esta intentando abrir *****");

        }


        db.close();
        */









/*        LocalUserDB localUserDB = new LocalUserDB(this, "LocalUserDB", null, 1);

        SQLiteDatabase db = localUserDB.getWritableDatabase();

        if(db != null) {

            System.out.println("Vamos a guardar la tabla nueva***************************************************************************");

            String querySQL = "DROP TABLE stocks";
            db.execSQL(querySQL);

            querySQL="CREATE TABLE stocks ( symbol VARCHAR(8) NOT NULL , association_name VARCHAR(30) NOT NULL , date DATE NOT NULL , open_value FLOAT NOT NULL , high_value FLOAT NOT NULL , low_value FLOAT NOT NULL , close_value FLOAT NOT NULL , adj_close_value FLOAT NOT NULL , volume FLOAT NOT NULL )";
            db.execSQL(querySQL);

            querySQL="INSERT INTO stocks (symbol, association_name, date, open_value, high_value, low_value, close_value, adj_close_value, volume) VALUES ('AAPL', 'AAPL INC', '2018-02-16', '172.360001', '174.820007', '160.770004', '172.429993', '192.429993', '37823080')";
            db.execSQL(querySQL);

            querySQL="INSERT INTO stocks (symbol, association_name, date, open_value, high_value, low_value, close_value, adj_close_value, volume) VALUES ('AAPL', 'AAPL INC', '2018-02-15', '171.360001', '173.820007', '181.770004', '152.429993', '172.429993', '37823080')";
            db.execSQL(querySQL);

            querySQL="INSERT INTO stocks (symbol, association_name, date, open_value, high_value, low_value, close_value, adj_close_value, volume) VALUES ('AAPL', 'AAPL INC', '2018-02-14', '170.360001', '175.820007', '141.770004', '162.429993', '192.429993', '37823080')";
            db.execSQL(querySQL);

            querySQL="INSERT INTO stocks (symbol, association_name, date, open_value, high_value, low_value, close_value, adj_close_value, volume) VALUES ('AAPL', 'AAPL INC', '2018-02-13', '169.360001', '170.820007', '171.770004', '192.429993', '152.429993', '37823080')";
            db.execSQL(querySQL);

        }else {
            System.out.println("***** No existe la BBDD que esta intentando abrir *****");

        }

        db.close();*/



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
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.item1:
                Toast.makeText(this, "Item 1", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item 2", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Item 3", Toast.LENGTH_LONG).show();
                return true;
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


            //mChart.setOnChartGestureListener(TestActivity.this);
            //mChart.setOnChartValueSelectedListener(TestActivity.this);



            List<GraphicData> graphicDataList = new ArrayList<GraphicData>();
            RemoteGraphicData remoteGraphicData = new RemoteGraphicData();
            graphicDataList = remoteGraphicData.GetRemoteGraphicData(simbolo);

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


            LineData data = new LineData(dataSets);

            mChart.setData(data);
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
