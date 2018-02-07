package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 27/01/2018.
 */

public class Credential {

    String user = "";
    String pass = "";
    Boolean rememberMe = false;

    //Constructor

    public void setUser(String user){
        this.user = user;
    }

    public void setPass(String pass){
        this.pass = pass;
    }

    public void setRememberMe(Boolean rememberMe){
        this.rememberMe = rememberMe;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }
}
