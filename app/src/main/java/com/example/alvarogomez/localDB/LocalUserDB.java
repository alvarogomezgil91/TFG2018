package com.example.alvarogomez.localDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.alvarogomez.tfg2018.Credential;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Alvaro Gomez on 27/01/2018.
 */

public class LocalUserDB extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE local_user_credentials (user_name VARCHAR(20), email VARCHAR(30), "
            + "password VARCHAR(16), remember_me BOOLEAN NOT NULL" +
            " DEFAULT 0, last_sucessfull_login DATE)";


    String sqlUpdateRememberMe = "UPDATE local_user_credentials SET rememberMe = ? WHERE email = ?";

    //String sqlUpdateLastSuccesfullLogin = "UPDATE local_user_credentials SET remember_me = ? last_sucessfull_login = ? WHERE user_name = ?";



    String sqlSelect = "SELECT user_name, password, remember_me, last_sucessfull_login FROM local_user_credentials WHERE last_sucessfull_login = "
            + "(SELECT MAX(last_sucessfull_login) FROM local_user_credentials)";

    String sqlInsert = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) "
                        + "VALUES ('Alvaro1', 'alvaro@correo.com', 'PassA', 1, '2018-08-06')";



    String sqlUpdatePassword = "UPDATE local_user_credentials SET password = ? WHERE email = ?";



    //Constructor
    public LocalUserDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("Ejecutando query SQL -> " + sqlCreate + "\n");
        db.execSQL(sqlCreate);
        //System.out.println("Ejecutando query SQL -> " + sqlInsert + "\n");
        //db.execSQL(sqlInsert);
        //System.out.println("Ejecutando query SQL INSERT2 -> ++++++++" + "\n");
        //onInsert2(db, null);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        //Esto solo se ejecutara cuando el numero de version de nuestra
        //base de datos cambio. Cambiaremos la version cuando sea necesario
        //cambio de esquemas, etc.
        //db.execSQL(sqlInsert);
        //System.out.println("sql -> " + sqlInsert);
        System.out.println("onUpgrade*********************************************");

        System.out.print("Ejecutando query SQL -> " + sqlCreate +"\n");
        //db.execSQL(sqlCreate);
        System.out.print("Ejecutando query SQL -> " + sqlInsert +"\n");
        //db.execSQL(sqlInsert);


    }

    public Credential onSelect(SQLiteDatabase db, String[] args){

        Credential credential = new Credential();
        Cursor cursor = db.rawQuery(sqlSelect, args);

        if (cursor.moveToFirst()){

            credential.setUser(cursor.getString(0));
            credential.setPass(cursor.getString(1));
            credential.setRememberMe(Boolean.valueOf(cursor.getString(2)));

            System.out.println("Fin del select -> Hemos cogido la fecha: " + cursor.getString(3));

        }

        return credential;

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

    public void onUpdateLastSuccesfullLogin(SQLiteDatabase db, Boolean rememberMe, String userName, String pass){

        String deleteSql = "DELETE FROM local_user_credentials WHERE user_name = '" + userName + "'";
        db.execSQL(deleteSql);

        Date sysdate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(sysdate);

        int rememberMeInt = rememberMe ? 1 : 0;

        String sqlStatement = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) " +
                "VALUES ('" + userName + "', '" + userName + "@correo.com', '" + pass + "', '" + rememberMeInt + "', '" + date + "')";

        //String updateSql = "UPDATE local_user_credentials ";
        //String setSql = "SET remember_me = " + rememberMeInt + ", last_sucessfull_login = '" + date + "' ";
        //String whereSql = "WHERE user_name = '" + userName +"'";
        //String sqlStatement = updateSql + setSql + whereSql;

        System.out.println("*************vamos a pintar la SQL --> " + sqlStatement);


        db.execSQL(sqlStatement);
    }




    public void onInsert2(SQLiteDatabase db, String[] args){

        String user1 = "Alvaro1";
        String email = "alvaro@correo";
        String pass1 = "PassA";
        int remMe = 1;
        String last1 = "2018-02-04";

        String user2 = "Alvaro2";
        String pass2 = "PassB";
        int remMe2 = 1;
        String last2 = "2018-02-05";


        String user3 = "Alvaro3";
        String pass3 = "PassC";
        int remMe3 = 1;
        String last3 = "2018-02-06";


        String user4 = "Alvaro4";
        String pass4 = "PassD";
        int remMe4 = 1;
        String last4 = "2018-02-07";


        String user5 = "Alvaro5";
        String pass5 = "PassE";
        int remMe5 = 1;
        String last5 = "2018-02-01";

        String sqlInsert1 = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) "
                + "VALUES ('" + user1 + "', '" + email + "', '" + pass1 + "', '" + remMe + "', '" + last1 + "')";

        String sqlInsert2 = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) "
                + "VALUES ('" + user2 + "', '" + email + "', '" + pass2 + "', '" + remMe + "', '" + last2 + "')";

        String sqlInsert3 = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) "
                + "VALUES ('" + user3 + "', '" + email + "', '" + pass3 + "', '" + remMe + "', '" + last3 + "')";

        String sqlInsert4 = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) "
                + "VALUES ('" + user4 + "', '" + email + "', '" + pass4 + "', '" + remMe + "', '" + last4 + "')";

        String sqlInsert5 = "INSERT INTO local_user_credentials (user_name, email, password, remember_me, last_sucessfull_login) "
                + "VALUES ('" + user5 + "', '" + email + "', '" + pass5 + "', '" + remMe + "', '" + last5 + "')";

        System.out.println("****************Ejecutando query 1 **********" + "\n");
        db.execSQL(sqlInsert1);
        System.out.println("****************Ejecutando query 2 **********" + "\n");
        db.execSQL(sqlInsert2);
        System.out.println("****************Ejecutando query 3 **********" + "\n");
        db.execSQL(sqlInsert3);
        System.out.println("****************Ejecutando query 4 **********" + "\n");
        db.execSQL(sqlInsert4);
        System.out.println("****************Ejecutando query 5 **********" + "\n");
        db.execSQL(sqlInsert5);


    }


}
