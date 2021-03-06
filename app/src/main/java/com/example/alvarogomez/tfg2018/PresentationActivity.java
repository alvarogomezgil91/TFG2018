package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.example.alvarogomez.remoteDB.RemoteUserDB;
import static java.lang.Thread.sleep;

public class PresentationActivity extends AppCompatActivity {

    private static boolean splashLoaded = false;
    private static boolean credentialsOK = false;
    Credential credential;
    Boolean internetConnection;
    private static String mLogOut = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("audit",this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        if (!splashLoaded){

            setContentView(R.layout.activity_presentation);

            //Utilizamos la clase creada para realizar el proceso de conseguir
            //credenciales en segundo plano, mientras que la actividad se muestra.

            ThreadCreation threadCreation = new ThreadCreation();
            threadCreation.execute().toString();

            splashLoaded = true;

        } else if (splashLoaded && credentialsOK){

            //Evitamos que la pantalla de inicio se muestre varias veces

            Intent intent = new Intent(PresentationActivity.this, ViewPagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("user_name", credential.getUser());
            startActivity(intent);
            finish();

        } else {

            Intent intent = new Intent(PresentationActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

        }

    }

    public Boolean presentationActivity() throws InterruptedException {

        internetConnection = retrieveInternetConnection();

        if (internetConnection != true) {
            return credentialsOK;
        }

        //Miramos si tiene guardadas user:pass y si ha seleccionado que quiere entrar
        //directamente (remember me), esto lo haremos llamando directamente a la base
        //de datos SQL del móvil (GetLocalCredentials.java)

        GetLocalCredentials localcredentials = new GetLocalCredentials();
        credential = localcredentials.getLocalCredentials(this);
        credential = localcredentials.getLocalCredentials(this);

        String user = credential.getUser();
        String password = credential.getPass();
        Boolean rememberMe = credential.getRememberMe();

        // Pruebas
        String mensaje = "";

        if(rememberMe){

            //Comprobamos usuario y contraseña con la base de datos
            //e intentamos conectarnos
            RemoteUserDB remoteUserDB = new RemoteUserDB();
            credentialsOK = remoteUserDB.GetRemoteCredentials(user, password);

            //RetrieveRemoteCredentials
            if(credentialsOK){
                mensaje = "credenciales OK";
                System.out.println("************* El mensaje es -> " + mensaje + "************");
            }else{
                mensaje = "Error en las credenciales";
                System.out.println("************* El mensaje es -> " + mensaje + "************");

            }

        }else{

            //Nos metemos en la LoginActivity
            //Intent intent = new Intent(PresentationActivity.this, LoginActivity.class);

            System.out.println("************* El mensaje es -> " + mensaje + "************");

        }

        return credentialsOK;

    }

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean retrieveInternetConnection() throws InterruptedException {

        Boolean connectionOK = false;

        if (isNetDisponible() && isOnlineNet()) {
            connectionOK = true;
        }

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

            try {
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

            if (credentialsOK){

                Toast.makeText(getBaseContext(), "Conexión realizada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                intent.putExtra("user_name", credential.getUser());
                startActivity(intent);
                finish();

            } else if (!internetConnection) {
                Toast.makeText(getBaseContext(), "No existe conexión a internet", Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(), "Conexión cancelada", Toast.LENGTH_SHORT).show();
        }

    }

}

