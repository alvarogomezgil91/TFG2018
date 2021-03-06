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
 * Created by Alvaro Gomez on 11/02/2018.
 */

public class RemoteStocksData {

    String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    String mURL;
    String mSimbolo;
    String mUserName;

    public List<Stock> GetRemoteStocksData(String inputUrl, String imputSimbolo, String userName){

        System.out.println("********* Entrando al comando GetRemoteStocksData **************");

        mURL = inputUrl;
        mSimbolo = imputSimbolo;
        mUserName = userName;

        List<Stock> stockDataList = new ArrayList<Stock>();
        int listSize;

        try {
            HttpURLConnection urlConn;

            URL url = new URL(REMOTE_URL + mURL);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0" +
                    " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.connect();


            //Creo el Objeto JSON
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("simbolo", mSimbolo);
            jsonParam.put("user_name", mUserName);
            // Envio los parámetros post.
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

                if (resultCode.equals("1")) {      // hay un alumno que mostrar

                    JSONArray arrayJSON = new JSONArray(respuestaJSON.getString("mensaje"));

                    listSize = arrayJSON.length();
                    System.out.println("**********************   Recibimos una lista de " + listSize + " elementos **************");

                    for (int i = 0; i < arrayJSON.length(); i++){

                        Stock stockData = new Stock();

                        JSONObject jsonStockData = new JSONObject(arrayJSON.getString(i));

                        stockData.setSimbolo(jsonStockData.getString("simbolo"));
                        stockData.setNombreStock(jsonStockData.getString("nombre_stock"));
                        stockData.setDescription(jsonStockData.getString("descripcion_stock"));
                        stockData.setFecha(jsonStockData.getString("fecha"));
                        stockData.setApertura(Float.valueOf(jsonStockData.getString("apertura")));
                        stockData.setCierre(Float.valueOf(jsonStockData.getString("cierre")));
                        stockData.setFavorito(Integer.valueOf(jsonStockData.getString("favorito")));
                        stockData.setTendencia(Integer.valueOf(jsonStockData.getString("tendencia")));
                        stockData.setEsMercado(Integer.valueOf(jsonStockData.getString("es_mercado")));

                        stockDataList.add(stockData);

                    }

                } else if (resultCode.equals("2")) {

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
