package com.coding.exercise.service;

import com.coding.exercise.exception.InvalidDatesException;
import com.coding.exercise.exception.InvalidStockSymbolException;
import com.coding.exercise.exception.StockNotInWatchlistException;
import com.coding.exercise.response.StockPrice;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Provides an implementation to the StockService interface backed by the Yahoo
 * Finance API
 */
@Service
public class YahooFinanceStockService implements StockService {

    private static final Set<String> watchList = new TreeSet<>();

    @Override
    public String[] addStock(String symbol) throws InvalidStockSymbolException, IOException {
        symbol = symbol.toUpperCase();
        if(watchList.contains(symbol)) {
            return watchList.toArray(new String[0]);
        }

        Stock stock = YahooFinance.get(symbol);
        if(stock == null){
            throw new InvalidStockSymbolException();
        } else {
            watchList.add(symbol);
            return watchList.toArray(new String[0]);
        }
    }

    @Override
    public String[] removeStock(String symbol) {
        if(symbol == null){
            return watchList.toArray(new String[0]);
        }
        //add private methods that do the uppercase and then add/remove
        symbol = symbol.toUpperCase();
        watchList.remove(symbol);
        return watchList.toArray(new String[0]);
    }

    @Override
    public void removeAllStocks(){
        watchList.clear();
    }

    @Override
    public String[] getWatchlist() {
        return watchList.toArray(new String[0]);
    }

    @Override
    public StockPrice[] getLatestPricesForWatchList() throws Exception {
        List<StockPrice> stockPricesList = new ArrayList<>();
        String[] watchListArray = getWatchlist();

        Map<String, Stock> stocks = YahooFinance.get(watchListArray);

        for (String symbol : watchListArray) {
            Stock stock = stocks.get(symbol);
            stockPricesList.add(new StockPrice(symbol, stock.getQuote().getPrice()));
        }

        return stockPricesList.toArray(new StockPrice[0]);
    }

    @Override
    public BigDecimal getAverageStockPrice(String symbol, String startDateString, String endDateString)
            throws StockNotInWatchlistException, ParseException, IOException, InvalidDatesException {

        //Check if stock exists in watchlist
        String[] watchList = getWatchlist();
        if(Arrays.stream(watchList).noneMatch(symbol.toUpperCase()::equals)){
            throw new StockNotInWatchlistException();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateString);
        Date endDate = dateFormat.parse(endDateString);
        
        //Add a day to end date to make the end date inclusive in range
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.HOUR, 24);
        endDate = calendar.getTime();


        Calendar from = Calendar.getInstance();
        from.setTime(startDate);
        Calendar to = Calendar.getInstance();
        to.setTime(endDate);

        Stock stock = YahooFinance.get(symbol, from, to, Interval.DAILY);
        List<HistoricalQuote> history = stock.getHistory();

        if(history.size() == 0){
            //Yahoo returns history of size zero when the endDate comes before the startDate
            throw new InvalidDatesException();
        }

        double sum = 0;
        for(HistoricalQuote quote : history){
            sum += quote.getClose().doubleValue();
        }

        double average = sum / history.size();
        
        return new BigDecimal(average);

    }

}
