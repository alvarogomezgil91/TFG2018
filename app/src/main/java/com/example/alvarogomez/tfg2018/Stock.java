package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 18/06/2018.
 */
public class Stock {

    private int id;
    private String simbolo;
    private String nombreStock;
    private String description;
    private String fecha;
    private float apertura;
    private float cierre;
    private int favorito;
    private int tendencia;
    private int esMercado;

    public Stock(){}

    //Constructor

    public Stock(int id, String simbolo, float apertura, float cierre, String description, int favorito, int tendencia, int esMercado) {
        this.id = id;
        this.simbolo = simbolo;
        this.apertura = apertura;
        this.cierre = cierre;
        this.description = description;
        this.favorito = favorito;
        this.tendencia = tendencia;
        this.esMercado = esMercado;
    }

    public Stock(int id, String simbolo, float apertura, float cierre, String description, int esMercado) {
        this.id = id;
        this.simbolo = simbolo;
        this.apertura = apertura;
        this.cierre = cierre;
        this.description = description;
        this.esMercado = esMercado;
    }

    public Stock(int id, String simbolo, float cierre, String description, int tendencia, int esMercado) {
        this.id = id;
        this.simbolo = simbolo;
        this.cierre = cierre;
        this.description = description;
        this.tendencia = tendencia;
        this.esMercado = esMercado;
    }





    //Setter, getter

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getSimbolo() {
        return simbolo;
    }
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getNombreStock() {
        return nombreStock;
    }
    public void setNombreStock(String nombreStock) {
        this.nombreStock = nombreStock;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getApertura() {
        return apertura;
    }
    public void setApertura(float apertura) {
        this.apertura = apertura;
    }

    public float getCierre() {
        return cierre;
    }
    public void setCierre(float cierre) {
        this.cierre = cierre;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getFavorito(){
        return favorito;
    }
    public void setFavorito(int favorito) {
        this.favorito = favorito;
    }

    public int getTendencia(){
        return tendencia;
    }
    public void setTendencia(int tendencia) {
        this.tendencia = tendencia;
    }
    public int getEsMercado(){
        return esMercado;
    }
    public void setEsMercado(int esMercado) {
        this.esMercado = esMercado;
    }
}

