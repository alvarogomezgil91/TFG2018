package com.example.alvarogomez.tfg2018;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.example.alvarogomez.remoteDB.RemoteGraphicData;
import com.example.alvarogomez.remoteDB.RemoteUserDB;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class PresentationActivity extends AppCompatActivity {

    private static boolean splashLoaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!splashLoaded){

            setContentView(R.layout.activity_presentation);


            //Utilizamos la clase creada para realizar el proceso de conseguir
            //credenciales en segundo plano, mientras que la actividad se muestra.

            ThreadCreation threadCreation = new ThreadCreation();
            threadCreation.execute().toString();

            splashLoaded = true;


        } else {

            //Evitamos que la pantalla de inicio se muestre varias veces

            Intent intent = new Intent(PresentationActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

        }





    }


    /**
     * This method is going to take care of the previous preparations of the app
     */
    public Boolean presentationActivity() throws InterruptedException {


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

        Boolean credentialsOK = false;

        if(rememberMe){
            //Comprobamos usuario y contraseña con la base de datos
            //e intentamos conectarnos


            RemoteUserDB remoteUserDB = new RemoteUserDB();

            credentialsOK = remoteUserDB.GetRemoteCredentials(user, password);






            //RetrieveRemoteCredentials

            if(credentialsOK){
                //MainActivity
                //Intent intent = new Intent(PresentationActivity.this, MainActivity.class);
                mensaje = "credenciales OK";
                System.out.println("************* El mensaje es -> " + mensaje + "************");


            }else{
                //LoginActivity
                //Intent intent = new Intent(PresentationActivity.this, LoginActivity.class);
                mensaje = "Error en las credenciales";
                System.out.println("************* El mensaje es -> " + mensaje + "************");

            }

            //Nos metemos directamente en el MainActivity

        }else{
            //Nos metemos en la LoginActivity
            //Intent intent = new Intent(PresentationActivity.this, LoginActivity.class);

            mensaje = "primera ejecucion";

        }

        return credentialsOK;


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

    private class ThreadCreation extends AsyncTask<Void, Integer, Boolean>{

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
            try {
                //List<GraphicData> graphicDataList = new ArrayList<GraphicData>();
                //RemoteGraphicData remoteGraphicData = new RemoteGraphicData();
                //graphicDataList = remoteGraphicData.GetRemoteGraphicData("ABE.MC");
                credentialsOK = presentationActivity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
            super.onPostExecute(credentialsOK);
            Toast.makeText(getBaseContext(), "Conexión realizada", Toast.LENGTH_SHORT).show();

            if (credentialsOK){

                //Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                startActivity(intent);
                finish();

            } else {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                finish();

                startActivity(intent);


            }



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

