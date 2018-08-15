package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 18/06/2018.
 */
public class Stock {

    private int id;
    private String stockName;
    private float cierre;
    private String description;
    private int favorito;

    public Stock(){}

    //Constructor

    public Stock(int id, String stockName, float cierre, String description, int favorito) {
        this.id = id;
        this.stockName = stockName;
        this.cierre = cierre;
        this.description = description;
        this.favorito = favorito;
    }

    //Setter, getter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
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
}

