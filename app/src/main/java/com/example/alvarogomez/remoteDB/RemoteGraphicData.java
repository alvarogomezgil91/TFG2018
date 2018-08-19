package com.example.alvarogomez.remoteDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

public class RemoteGraphicData {

    String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    String GET_REMOTE_CREDENTIALS = "/GetGeneralData.php";

    private String mSimbolo;

    public RemoteGraphicData(String simbolo) { mSimbolo = simbolo; }

    //String GET_REMOTE_CREDENTIALS = "/retrieve_users.php";


    public List<GraphicData> GetRemoteGraphicData(){

        System.out.println("********* Entrando al comando GetRemoteGraphicData **************");


        List<GraphicData> graphicDataList = new ArrayList<GraphicData>();
        int listSize;




        try {
            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            URL url = new URL(REMOTE_URL + GET_REMOTE_CREDENTIALS);
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

                if (resultCode.equals("1")) {      // hay un alumno que mostrar

                    listSize = Integer.valueOf(respuestaJSON.getString("tamano"));

                    System.out.println("**********************   Recibimos una lista de " + listSize + " elementos **************");

                    JSONArray arrayJSON = new JSONArray(respuestaJSON.getString("mensaje"));



                    for (int i = 0; i < arrayJSON.length(); i++){

                        GraphicData graphicData = new GraphicData();

                        JSONObject jsonGrpahicData = new JSONObject(arrayJSON.getString(i));

                        graphicData.setSimbolo(jsonGrpahicData.getString("simbolo"));
                        graphicData.setFecha(jsonGrpahicData.getString("fecha"));
                        graphicData.setApertura(Float.valueOf(jsonGrpahicData.getString("apertura")));
                        graphicData.setMaximo(Float.valueOf(jsonGrpahicData.getString("maximo")));
                        graphicData.setMinimo(Float.valueOf(jsonGrpahicData.getString("minimo")));
                        graphicData.setCierre(Float.valueOf(jsonGrpahicData.getString("cierre")));
                        graphicData.setAdj_cierre(Float.valueOf(jsonGrpahicData.getString("adj_cierre")));
                        graphicData.setVolume(Float.valueOf(jsonGrpahicData.getString("volume")));

                        graphicDataList.add(graphicData);
                        //graphicDataList.set(i, graphicData);


                        //System.out.println("*********** Elemento " + i + "de la lista: ");
                        //System.out.println("*********** " + arrayJSON.getString(i) + "*************");



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


        System.out.println("********* Saliendo al comando GetRemoteGraphicData **************");

        return graphicDataList;

    }



}
