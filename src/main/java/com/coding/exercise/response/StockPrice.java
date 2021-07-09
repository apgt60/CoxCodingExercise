package com.coding.exercise.response;

import java.math.BigDecimal;

public class StockPrice {

    private final String symbol;
    private final BigDecimal price;

    public StockPrice(String symbol, BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
