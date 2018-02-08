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
            
            if(db != null) {
                //localUserDB.onInsert2(db, null);
                credential = localUserDB.onSelect(db, null);
                credential.setRememberMe(true);
                System.out.println("**************      ejecutando historias en la BBDD            ****************");
                System.out.println("**************      User:" + credential.getUser() +"            ****************");
                System.out.println("**************      Password:" + credential.getPass() +"            ****************");
                System.out.println("**************      RememberMe:" + credential.getRememberMe() +"            ****************");

                //context.deleteDatabase("LocalUserDB");
            }else {
                System.out.println("***** No existe la BBDD que esta intentando abrir *****");

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