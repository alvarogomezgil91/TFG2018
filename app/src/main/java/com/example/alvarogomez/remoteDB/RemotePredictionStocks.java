package com.example.alvarogomez.remoteDB;

import android.util.Log;

import com.example.alvarogomez.tfg2018.Constants;
import com.example.alvarogomez.tfg2018.GraphicData;
import com.example.alvarogomez.tfg2018.Stock;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvaro Gomez on 24/07/2018.
 */

public class RemotePredictionStocks {

    private String REMOTE_URL = Constants.REMOTE_URL;
    String GET_PREDICTIONS_DATA = Constants.GET_PREDICTIONS_DATA;
    private String mFecha;

    public RemotePredictionStocks(String fecha){
        mFecha = fecha;
    }

    public List<Stock> GetRemotePredictionStocks(){

        System.out.println("********* Entrando al comando GetRemotePredictionStocksData **************");

        List<Stock> stockDataList = new ArrayList<Stock>();
        int listSize;

        try {
            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            URL url = new URL(REMOTE_URL + GET_PREDICTIONS_DATA);
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
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("fecha", mFecha);
            OutputStream os = urlConn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonParam.toString());
            writer.flush();
            writer.close();

            int respuesta = urlConn.getResponseCode();

            StringBuilder result = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK) {

                String line;

                InputStream in = new BufferedInputStream(urlConn.getInputStream());  // preparo la cadena de entrada
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                while ((line=br.readLine()) != null) {
                    result.append(line);
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                Log.i("tagconvertstr", "["+result+"]");

                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena

                //Accedemos al vector de resultados
                String resultCode = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultCode.equals("1")) {

                    JSONArray arrayJSON = new JSONArray(respuestaJSON.getString("mensaje"));

                    for (int i = 0; i < arrayJSON.length(); i++){

                        Stock stockData = new Stock();

                        JSONObject jsonStockData = new JSONObject(arrayJSON.getString(i));

                        stockData.setSimbolo(jsonStockData.getString("simbolo"));
                        stockData.setDescription(jsonStockData.getString("nombre_stock"));
                        stockData.setFecha(jsonStockData.getString("fecha"));
                        stockData.setApertura(Float.valueOf(jsonStockData.getString("apertura")));
                        stockData.setCierre(Float.valueOf(jsonStockData.getString("cierre_predecido")));
                        stockData.setEsMercado(Integer.valueOf(jsonStockData.getString("es_mercado")));

                        stockDataList.add(stockData);

                    }


                } else if (resultCode.equals("2")) {

                    System.out.println("!!!!!!!**********    Error al recuperar los datos de los stocks predecidos    **********!!!!!!!");

                }

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("********* Saliendo del comando GetRemotePredictionStocksData **************");

        return stockDataList;

    }
}
