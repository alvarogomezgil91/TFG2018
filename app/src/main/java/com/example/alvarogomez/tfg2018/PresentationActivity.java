package com.example.alvarogomez.tfg2018;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.example.alvarogomez.remoteDB.RemoteUserDB;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class PresentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        //Utilizamos la clase creada para realizar el proceso de conseguir
        //credenciales en segundo plano, mientras que la actividad se muestra.

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute();





    }


    /**
     * This method is going to take care of the previous preparations of the app
     */
    public String presentationActivity() throws InterruptedException {


        Boolean internetConnection;
        internetConnection = retrieveInternetConnection();

        if (internetConnection != true) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), "No existe conexión a internet", Toast.LENGTH_SHORT).show();
                }
            });
        } else {



        }



        //Miramos si tiene guardadas user:pass y si ha seleccionado que quiere entrar
        //directamente (remember me), esto lo haremos llamando directamente a la base
        //de datos SQL del móvil (GetLocalCredentials.java)

        GetLocalCredentials localcredentials = new GetLocalCredentials();
        Credential credential = localcredentials.getLocalCredentials(this);

        String user = credential.getUser();
        String password = credential.getPass();
        Boolean rememberMe = credential.getRememberMe();

        // Pruebas
        String mensaje = "";

        if(rememberMe){
            //Comprobamos usuario y contraseña con la base de datos
            //e intentamos conectarnos

            Boolean credentialsOK = false;
            RemoteUserDB remoteUserDB = new RemoteUserDB();
            credentialsOK = remoteUserDB.GetRemoteCredentials(user, password);






            //RetrieveRemoteCredentials

            if(credentialsOK){
                //MainActivity
                //Intent intent = new Intent(PresentationActivity.this, MainActivity.class);
                mensaje = "credenciales OK";


            }else{
                //LoginActivity
                //Intent intent = new Intent(PresentationActivity.this, LoginActivity.class);
                mensaje = "Error en las credenciales";

            }

            //Nos metemos directamente en el MainActivity

        }else{
            //Nos metemos en la LoginActivity
            //Intent intent = new Intent(PresentationActivity.this, LoginActivity.class);

            mensaje = "primera ejecucion";

        }

        return mensaje;


    }

    public Boolean retrieveInternetConnection() throws InterruptedException {

        Boolean connectionOK = false;

        //Servicio de consulta de conexión a internet, método ping.php (RetrieveConection.java)
        System.out.println("Llamando al WS PHP de consulta de conexión a internet");
        sleep(1000);
        System.out.println("Conexión a internet correcta");
        connectionOK = true;


        return connectionOK;
    }

    private class ThreadCreation extends AsyncTask<Void, Integer, String>{

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
        protected String doInBackground(Void... voids) {


            String mensaje = null;
            try {
                mensaje = presentationActivity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("****** EL MENSAJE ES " + mensaje + "*******");







            return mensaje;
        }

        //El segundo parametro del AsyncTask (Integer), corresponde con este parametro de entrada
        protected void onProgressUpdate(Integer values){
            super.onProgressUpdate(values);
        }

        //Se ejecuta cuando ya ha acabado la ejecucion del segundo hilo
        //El parametro de salida del doInBackground, coincide con el parametro de entrada (Void)
        @Override
        protected void onPostExecute(String mensaje) {
            super.onPostExecute(mensaje);
            Toast.makeText(getBaseContext(), "Conexión realizada", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PresentationActivity.this, TestActivity.class);
            Bundle b = new Bundle();



            b.putString("mensaje>", mensaje);
            intent.putExtras(b);

            startActivity(intent);

            finish();


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }


    }

}

