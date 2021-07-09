package com.coding.exercise.request;

public class StockWatchlistRequest {

    private String symbol;
    private String startDate;
    private String endDate;

    public StockWatchlistRequest(){}

    public StockWatchlistRequest(String symbol, String startDate, String endDate) {
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }



}