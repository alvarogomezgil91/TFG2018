package com.example.alvarogomez.remoteDB;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvaro Gomez on 07/07/2018.
 */

public class RemoteFavouriteStocks {
    String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    String INSERT_FAVOURITE_STOCK = Constants.INSERT_FAVOURITE_STOCK;
    String DELETE_FAVOURITE_STOCK = Constants.DELETE_FAVOURITE_STOCK;
    String mUserName;
    String mStock;
    String mFavouriteStocks;
    View v;
    Boolean comandoOk = false;


    public RemoteFavouriteStocks(String userName){
        mUserName = userName;
    }
    public RemoteFavouriteStocks(String userName, String stock){
        mUserName = userName;
        mStock = stock;
    }



    public Boolean InsertRemoteFavouriteStock(){

        System.out.println("********* Entrando al comando InsertRemoteFavouriteStock **************");


        try {
            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            URL url = new URL(REMOTE_URL + INSERT_FAVOURITE_STOCK);
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

            //Creo el Objeto JSON
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("user_name", mUserName);
            jsonParam.put("stock", mStock);
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
                    //response+=line;
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                Log.i("tagconvertstr", "["+result+"]");

                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena

                //Accedemos al vector de resultados
                String resultCode = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultCode == "1") {      // hay un alumno que mostrar

                    comandoOk = true;
                    System.out.println("**********************   Insercion realizada con exito " + mUserName + " -> " + mStock + " **************");

                } else if (resultCode == "2") {

                    comandoOk = false;
                    //Toast.makeText(v.getContext(), "No se ha podido añadir a favoritos", Toast.LENGTH_SHORT).show();
                    System.out.println("!!!!!!!**********    Error al realizar la insercion    **********!!!!!!!");
                    System.out.println("**********************  " + mUserName + " -> " + mStock + " **************");

                }

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("********* Saliendo del comando GetRemoteFavouriteStocks **************");

        return comandoOk;

    }


    public Boolean DeleteRemoteFavouriteStock(){

        System.out.println("********* Entrando al comando DeleteRemoteFavouriteStock **************");



        try {
            HttpURLConnection urlConn;

            DataOutputStream printout;
            DataInputStream input;
            URL url = new URL(REMOTE_URL + DELETE_FAVOURITE_STOCK);
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

            //Creo el Objeto JSON
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("user_name", mUserName);
            jsonParam.put("stock", mStock);
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
                    //response+=line;
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                Log.i("tagconvertstr", "["+result+"]");

                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena

                //Accedemos al vector de resultados
                String resultCode = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultCode == "1") {      // hay un alumno que mostrar

                    comandoOk = true;
                    System.out.println("**********************   Insercion realizada con exito " + mUserName + " -> " + mStock + " **************");

                } else if (resultCode == "2") {

                    comandoOk = false;
                    //Toast.makeText(v.getContext(), "No se ha podido añadir a favoritos", Toast.LENGTH_SHORT).show();
                    System.out.println("!!!!!!!**********    Error al realizar la insercion    **********!!!!!!!");
                    System.out.println("**********************  " + mUserName + " -> " + mStock + " **************");

                }

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        System.out.println("********* Saliendo del comando GetRemoteFavouriteStocks **************");

        return comandoOk;

    }


    public String favouriteStockFromat(String cadena){
        String resultado = "'" + cadena.replace(",","', '") + "'";
        return resultado;
    }

}
