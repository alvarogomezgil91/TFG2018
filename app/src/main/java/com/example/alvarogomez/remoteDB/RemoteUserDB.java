package com.example.alvarogomez.remoteDB;

import android.util.Log;
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

/**
 * Created by Alvaro Gomez on 11/02/2018.
 */

public class RemoteUserDB {

    String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    String GET_REMOTE_CREDENTIALS = "/get_remote_credentials.php";
    //String GET_REMOTE_CREDENTIALS = "/retrieve_users.php";

    public Boolean GetRemoteCredentials(String user, String password){

        System.out.println("********* Entrando al comando GetRemoteCredentials **************");

        Boolean credentialsOk = false;

        //Quitar luego
        String devuelve = "";

        try {

            HttpURLConnection urlConn;
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
            jsonParam.put("user_name", user);
            jsonParam.put("password", password);

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
                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON == "1") {      // hay un alumno que mostrar
                    credentialsOk = true;
                    devuelve = "Credenciales correctas";

                } else if (resultJSON == "2") {
                    credentialsOk = false;
                    devuelve = "Credenciales erroneas";
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("********* Salida del comando GetRemoteCredentials -> " + devuelve +" **************");
        System.out.println("********* Saliendo al comando GetRemoteCredentials **************");

        return credentialsOk;

    }



}
