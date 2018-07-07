package com.example.alvarogomez.remoteDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.alvarogomez.tfg2018.Constants;
import com.example.alvarogomez.tfg2018.GraphicData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alvaro Gomez on 11/02/2018.
 */

public class RemoteStocksData {

    String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    String GET_INDEX_STOCKS_DATA_URL = Constants.GET_INDEX_STOCKS_DATA_URL;


    public List<GraphicData> GetRemoteStocksData(){

        System.out.println("********* Entrando al comando GetRemoteStocksData **************");


        List<GraphicData> stockDataList = new ArrayList<GraphicData>();
        int listSize;

        try {
            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            URL url = new URL(REMOTE_URL + GET_INDEX_STOCKS_DATA_URL);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0" +
                    " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.connect();
            // Envio los parámetros post.
            OutputStream os = urlConn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.flush();
            writer.close();

            int respuesta = urlConn.getResponseCode();


            StringBuilder result = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK) {

                String line;


                InputStream in = new BufferedInputStream(urlConn.getInputStream());  // preparo la cadena de entrada

                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                //BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    result.append(line);
                    //response+=line;
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                Log.i("tagconvertstr", "["+result+"]");

                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena

                //Accedemos al vector de resultados
                JSONArray prueba = new JSONArray(respuestaJSON.getString("mensaje"));


                String resultCode = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultCode == "1") {      // hay un alumno que mostrar

                    JSONArray arrayJSON = new JSONArray(respuestaJSON.getString("mensaje"));

                    listSize = arrayJSON.length();

                    System.out.println("**********************   Recibimos una lista de " + listSize + " elementos **************");


                    for (int i = 0; i < arrayJSON.length(); i++){

                        GraphicData stockData = new GraphicData();

                        JSONObject jsonStockData = new JSONObject(arrayJSON.getString(i));

                        stockData.setSimbolo(jsonStockData.getString("simbolo"));
                        stockData.setCierre(Float.valueOf(jsonStockData.getString("cierre")));

                        stockDataList.add(stockData);

                        System.out.println("*********** Elemento " + i + " de la lista: ");
                        System.out.println("*********** " + arrayJSON.getString(i) + "*************");

                    }

                } else if (resultCode == "2") {

                    System.out.println("!!!!!!!**********    Error al recuperar los datos de las graficas    **********!!!!!!!");


                }

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("********* Saliendo al comando GetRemoteStocksData **************");

        return stockDataList;

    }


}
