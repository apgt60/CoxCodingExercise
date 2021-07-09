package com.coding.exercise.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class StockWatchlistResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String symbol;
    private String[] watchList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StockPrice[] watchListWithPrices;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String endDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal averageStockPrice;


    public StockWatchlistResponse(String symbol, String[] watchList, String error) {
        this.symbol = symbol;
        this.watchList = watchList;
        this.error = error;
    }

    public StockWatchlistResponse(String symbol, BigDecimal averageStockPrice, String[] watchList, String error){
        this.symbol = symbol;
        this.averageStockPrice = averageStockPrice;
        this.watchList = watchList;
        this.error = error;
    }

    public StockWatchlistResponse(String symbol, String[] watchList, StockPrice[] watchListWithPrices,
                                  String error) {
        this.symbol = symbol;
        this.watchList = watchList;
        this.watchListWithPrices = watchListWithPrices;
        this.error = error;
    }

    public String getSymbol() {
        return symbol;
    }

    public String[] getWatchList() {
        return watchList;
    }

    public StockPrice[] getWatchListWithPrices() {
        return watchListWithPrices;
    }

    public String getError() {
        return error;
    }

    public BigDecimal getAverageStockPrice() {
        return averageStockPrice;
    }

}