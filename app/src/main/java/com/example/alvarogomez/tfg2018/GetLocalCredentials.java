package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.alvarogomez.localDB.LocalUserDB;

/**
 * Created by Alvaro Gomez on 27/01/2018.
 */

public class GetLocalCredentials {

    private String user;
    private String pass;
    private Boolean rememberMe;


    public Credential getLocalCredentials(Context context) {

        Credential credential = new Credential();

        try {

            //Aquí vendría el método de conseguir los datos
            LocalUserDB localUserDB = new LocalUserDB(context, "LocalUserDB", null, 1);

            SQLiteDatabase db = localUserDB.getReadableDatabase();
            
            String[] prueba = new String[0];

            if(db == null) {
                localUserDB.onCreate(db);
                credential.setRememberMe(false);
            }else {
                //credential = localUserDB.onSelect(db, null);
                //localUserDB.onSelect(db, prueba);
                //credential.setRememberMe(true);
                //context.deleteDatabase("LocalUserDB");
            }

            db.close();

            //credential.setUser("ALVARO");
            //credential.setPass("ALVARO");
            //credential.setRememberMe(true);


        } catch (Exception e) {

        }

        return credential;
    }
}