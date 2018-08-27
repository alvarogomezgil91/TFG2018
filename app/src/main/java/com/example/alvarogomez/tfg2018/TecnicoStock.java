package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 26/08/2018.
 */

public class TecnicoStock {

    private String simbolo;
    private String nombrStock;
    private String fecha;
    private float EMA26;
    private float EMA12;
    private float MACD;
    private float SENAL;
    private float HISTOGRAMA;

    public TecnicoStock(){}

    public String getSimbolo(){
        return simbolo;
    }
    public void setSimbolo(String simbolo){
        this.simbolo = simbolo;
    }

    public String getNombrStock(){
        return nombrStock;
    }
    public void setNombrStock(String nombrStock){
        this.nombrStock = nombrStock;
    }

    public String getFecha(){
        return fecha;
    }
    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public float getEMA26(){
        return EMA26;
    }
    public void setEMA26(float EMA26){
        this.EMA26 = EMA26;
    }

    public float getEMA12(){
        return EMA12;
    }
    public void setEMA12(float EMA12){
        this.EMA12 = EMA12;
    }

    public float getMACD(){
        return MACD;
    }
    public void setMACD(float MACD){
        this.MACD = MACD;
    }

    public float getSENAL(){
        return SENAL;
    }
    public void setSENAL(float SENAL){
        this.SENAL = SENAL;
    }

    public float getHISTOGRAMA(){
        return HISTOGRAMA;
    }
    public void setHISTOGRAMA(float HISTOGRAMA){
        this.HISTOGRAMA = HISTOGRAMA;
    }





}
