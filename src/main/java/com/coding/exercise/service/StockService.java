package com.coding.exercise.service;

import com.coding.exercise.exception.InvalidDatesException;
import com.coding.exercise.exception.InvalidStockSymbolException;
import com.coding.exercise.exception.StockNotInWatchlistException;
import com.coding.exercise.response.StockPrice;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * Provides methods to add / remove stocks from a watch list and query information
 * about watched stocks.
 *
 * @author amit
 * @version 0.1
 */
public interface StockService {

    /**
     * Adds a stock to the watch list.
     *
     * @param symbol Stock symbol
     * @return A String array containing all of the stocks currently watched.
     * @throws InvalidStockSymbolException Is thrown when the stock symbol is not valid.
     * @throws IOException Is thrown when the end date is sooner than the start date
     */
    public String[] addStock(String symbol) throws InvalidStockSymbolException, IOException ;

    /**
     * Removes a stock from the watch list.
     *
     * @param symbol
     * @return  Stock symbol
     */
    public String[] removeStock(String symbol);

    /**
     * Removes all stocks from the watch list.
     */
    public void removeAllStocks();

    /**
     * Gets the current watch list.
     *
     * @return A String array containing all of the stocks currently watched
     */
    public String[] getWatchlist();

    /**
     * Gets the latest prices for all stocks in the current watch list.
     *
     * @return An array of StockPrice objects, each containing the stock symbol and
     * latest price.
     * @throws Exception
     */
    public StockPrice[] getLatestPricesForWatchList() throws Exception;

    /**
     * Provides the average closing price of a stock over the specified date range.
     *
     * @param symbol Stock symbol
     * @param startDateString Start date in YYYY-MM-DD format
     * @param endDateString End date in YYYY-MM-DD format
     * @return A BigDecimal object with the average closing price for the specified date range.
     * @throws StockNotInWatchlistException Is thrown when the symbol specified is not in the watch list.
     * @throws ParseException Is thrown when the date format for start or end date is not in the proper format.
     * @throws IOException Is thrown when there is an error retrieving the stock price from an external system.
     * @throws InvalidDatesException Is thrown when the end date is sooner than the start date
     */
    public BigDecimal getAverageStockPrice(String symbol, String startDateString, String endDateString)
            throws StockNotInWatchlistException, ParseException, IOException, InvalidDatesException;

}
