package com.example.alvarogomez.localDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.alvarogomez.tfg2018.Credential;


/**
 * Created by Alvaro Gomez on 27/01/2018.
 */

public class LocalUserDB extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE local_user_credentials (user_name VARCHAR(20), email VARCHAR(30), "
            + "password VARCHAR(16), remember_me BOOLEAN NOT NULL DEFAULT 0, last_sucessfull_login DATE)";

    String sqlUpdateRememberMe = "UPDATE local_user_credentials SET rememberMe = ? WHERE email = ?";

    String sqlUpdateLastSuccesfullLogin = "UPDATE local_user_credentials SET last_sucessfull_login = ? WHERE email = ?";

    String sqlSelect = "SELECT user, password, remember_me FROM local_user_credentials WHERE last_sucessfull_login = "
            + "(SELECT MAX(last_sucessfull_login) FROM local_user_credentials)";

    String sqlInsert = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) "
                        + "VALUES ('alvaro', 'alvaro@correo.com', 'pass', 1, '2018-02-06')";

    String sqlUpdatePassword = "UPDATE local_user_credentials SET password = ? WHERE email = ?";



    //Constructor
    public LocalUserDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(sqlInsert);
        System.out.println("sql -> " + sqlInsert);


    }

    public void onInsert(SQLiteDatabase db, String[] args){

        db.rawQuery(sqlInsert, args);
    }

    public void onUpdatePassword(SQLiteDatabase db, String[] args){

        db.rawQuery(sqlUpdatePassword, args);
    }

    public void onUpdateRememberMe(SQLiteDatabase db, String[] args){

        db.rawQuery(sqlUpdateRememberMe, args);
    }

    public void onUpdateLastSuccesfullLogin(SQLiteDatabase db, String[] args){

        db.rawQuery(sqlUpdateLastSuccesfullLogin, args);
    }

    public Credential onSelect(SQLiteDatabase db, String[] args){

        Credential credential = new Credential();
        Cursor cursor = db.rawQuery(sqlSelect, args);

        if (cursor.moveToFirst()){

            credential.setUser(cursor.getString(0));
            credential.setPass(cursor.getString(1));
            credential.setRememberMe(Boolean.valueOf(cursor.getString(2)));

        }

        return credential;

    }
}
