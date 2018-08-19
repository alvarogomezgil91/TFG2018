package com.example.alvarogomez.tfg2018;

/**
 * Created by Alvaro Gomez on 06/07/2018.
 */

public final class Constants {

    private Constants(){}

    // WebServices URLs
    public static final String REMOTE_URL = "http://algomez.atwebpages.com/WebServicesPhp";
    public static final String GET_INDEX_STOCKS_DATA_URL = "/GetIndexStocksData.php";
    public static final String INSERT_FAVOURITE_STOCK = "/InsertFavouriteStock.php";
    public static final String DELETE_FAVOURITE_STOCK = "/DeleteFavouriteStock.php";
    public static final String GET_FAVOURITE_STOCKS = "/GetFavouriteStocksByUser.php";
    public static final String GET_FAVOURITE_STOCKS_DATA = "/GetFavouriteStocksData.php";
    public static final String RSS_YAHOO_FINANCE = "https://finance.yahoo.com/rss/topstories";
    public static final String RSS_YAHOO_FINANCE_BY_TICKER_START = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=";
    public static final String RSS_YAHOO_FINANCE_BY_TICKER_END = "&region=US&lang=en-US";

    // Servicios
    public static final String GET_REMOTE_STOCKS_DATA = "GetRemoteStocksData";
    public static final String GET_REMOTE_FAVOURITE_STOCKS_DATA = "GetRemoteFavouriteStocksData";
    public static final String GET_REMOTE_GRAPHIC_DATA = "GetRemoteGraphicData";

    // Etiquetas XML para formateo
    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String JSON_APOSTROFE = "&apos;";
    public static final String APOSTROFE = "'";
    public static final String LINK = "link";

    // Constantes
    public static final String SEARCH = "Search";

    // DataSets y Valores de graficas
    public static final String DATA_SET_1 = "Data Set 1";
    public static final String DATA_SET_2 = "Data Set 2";



}
