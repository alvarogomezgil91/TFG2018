package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 06/07/2018.
 */

public final class Constants {

    private Constants(){}

    public static final String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    public static final String GET_INDEX_STOCKS_DATA_URL = "/GetIndexStocksData.php";
    public static final String INSERT_FAVOURITE_STOCK = "/InsertFavouriteStock.php";
    public static final String DELETE_FAVOURITE_STOCK = "/DeleteFavouriteStock.php";
    public static final String GET_FAVOURITE_STOCKS = "/GetFavouriteStocksByUser.php";
    public static final String GET_FAVOURITE_STOCKS_DATA = "/GetFavouriteStocksData.php";
    public static final String RSS_YAHOO_FINANCE = "https://finance.yahoo.com/rss/topstories";
    public static final String RSS_YAHOO_FINANCE_BY_TICKER_START = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=";
    public static final String RSS_YAHOO_FINANCE_BY_TICKER_END = "&region=US&lang=en-US";

}
