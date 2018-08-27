package com.example.alvarogomez.remoteDB;

import android.util.Log;

import com.example.alvarogomez.tfg2018.Constants;
import com.example.alvarogomez.tfg2018.TecnicoStock;
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
 * Created by Alvaro Gomez on 26/08/2018.
 */

public class RemoteTecnicoStocksData {

    String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    String GET_TECNICO_STOCKS_DATA = Constants.GET_TECNICO_STOCKS_DATA;

    private String mSimbolo;

    public RemoteTecnicoStocksData(String simbolo) { mSimbolo = simbolo; }


    public List<TecnicoStock> GetRemoteTecnicoStocksData(){

        System.out.println("********* Entrando al comando GetRemoteTecnicoStocksData **************");


        List<TecnicoStock> tecnicoStockList = new ArrayList<TecnicoStock>();
        int listSize;




        try {
            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            URL url = new URL(REMOTE_URL + GET_TECNICO_STOCKS_DATA);
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
                    for (int i = 0; i < arrayJSON.length(); i++){

                        TecnicoStock tecnicoStock = new TecnicoStock();
                        JSONObject jsonGrpahicData = new JSONObject(arrayJSON.getString(i));

                        tecnicoStock.setSimbolo(jsonGrpahicData.getString("simbolo"));
                        tecnicoStock.setNombrStock(jsonGrpahicData.getString("nombre_stock"));
                        tecnicoStock.setFecha(jsonGrpahicData.getString("fecha"));
                        tecnicoStock.setEMA26(Float.valueOf(jsonGrpahicData.getString("EMA26")));
                        tecnicoStock.setEMA12(Float.valueOf(jsonGrpahicData.getString("EMA12")));
                        tecnicoStock.setMACD(Float.valueOf(jsonGrpahicData.getString("MACD")));
                        tecnicoStock.setSENAL(Float.valueOf(jsonGrpahicData.getString("SENAL")));
                        tecnicoStock.setHISTOGRAMA(Float.valueOf(jsonGrpahicData.getString("HISTOGRAMA")));

                        tecnicoStockList.add(tecnicoStock);

                    }

                } else if (resultCode.equals("2")) {

                    System.out.println("!!!!!!!**********    Error al recuperar los datos de las graficas    **********!!!!!!!");

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("********* Saliendo al comando GetRemoteTecnicoStocksData **************");

        return tecnicoStockList;

    }

}
