package com.coding.exercise.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class StockInfoResponse {

    private final String symbol;
    private final BigDecimal price;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public StockInfoResponse(String symbol, BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
        this.error = null;
    }

    public StockInfoResponse(String symbol, BigDecimal price, String error) {
        this.symbol = symbol;
        this.price = price;
        this.error = error;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getError() {
        return error;
    }


}