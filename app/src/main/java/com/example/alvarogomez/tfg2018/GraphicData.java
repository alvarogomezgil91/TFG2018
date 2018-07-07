package com.example.alvarogomez.tfg2018;

import java.util.Date;

/**
 * Created by Alvaro Gomez on 17/03/2018.
 */

public class GraphicData {

    String simbolo = "";
    String fecha;
    float apertura = 0;
    float maximo = 0;
    float minimo = 0;
    float cierre = 0;
    float adj_cierre = 0;
    float volume = 0;

    public void setSimbolo (String simbolo) {this.simbolo = simbolo;}

    public void setFecha (String fecha) {this.fecha = fecha;}

    public void setApertura (float apertura) {this.apertura = apertura;}

    public void setMaximo (float maximo) {this.maximo = maximo;}

    public void setMinimo (float minimo) {this.minimo = minimo;}

    public void setCierre (float cierre) {this.cierre = cierre;}

    public void setAdj_cierre (float adj_cierre) {this.adj_cierre = adj_cierre;}

    public void setVolume (float volume) {this.volume = volume;}

    public String getSimbolo (){
        return simbolo;
    }

    public String getFecha() {
        return fecha;
    }

    public float getApertura() {
        return apertura;
    }

    public float getMaximo() {
        return maximo;
    }

    public float getMinimo() {
        return minimo;
    }

    public float getCierre() {
        return cierre;
    }

    public float getAdj_cierre() {
        return adj_cierre;
    }

    public float getVolume() {
        return volume;
    }
}
