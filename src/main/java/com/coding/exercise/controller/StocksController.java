package com.coding.exercise.controller;

import com.coding.exercise.exception.InvalidDatesException;
import com.coding.exercise.exception.InvalidStockSymbolException;
import com.coding.exercise.exception.StockNotInWatchlistException;
import com.coding.exercise.request.StockWatchlistRequest;
import com.coding.exercise.response.StockPrice;
import com.coding.exercise.response.StockWatchlistResponse;
import com.coding.exercise.service.StockService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;


@RestController
@RequestMapping("/stocks")
public class StocksController {

    public static String ERROR_INVALID_DATE_FORMAT = "startDate and endDate must by specified in YYYY-MM-DD format";
    public static String ERROR_INVALID_DATE_ORDER = "startDate must not be later than the endDate specified";
    public static String ERROR_STOCK_NOT_IN_WATCHLIST = "Stock must be in watch list to perform this function";
    public static final String ERROR_SYMBOL_INVALID_SYMBOL = "Invalid symbol";


    final StockService stockService;

    public StocksController(StockService stockService) {
        this.stockService = stockService;
    }
    
    @PostMapping("/addStock")
    public StockWatchlistResponse addStock(@RequestBody StockWatchlistRequest stockWatchlistRequest, HttpServletResponse response) {
        try {
            String[] watchList = stockService.addStock(stockWatchlistRequest.getSymbol());
            return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), watchList, null);
        } catch (InvalidStockSymbolException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), stockService.getWatchlist(),
                    ERROR_SYMBOL_INVALID_SYMBOL);
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @PostMapping("/removeStock")
    public StockWatchlistResponse removeStock(@RequestBody StockWatchlistRequest stockWatchlistRequest, HttpServletResponse response) {
        try {
            String[] watchList = stockService.removeStock(stockWatchlistRequest.getSymbol());
            return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), watchList, null);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @PostMapping("/removeAllStocks")
    public ResponseEntity removeAllStocks() {
        try {
            stockService.removeAllStocks();
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getWatchList")
    public StockWatchlistResponse getWatchList(HttpServletResponse response) {
        try {
            String[] watchList = stockService.getWatchlist();
            return new StockWatchlistResponse(null, watchList, null);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @GetMapping("/getLatestPricesForWatchlist")
    public StockWatchlistResponse getLatestPricesForWatchlist(HttpServletResponse response) {
        try {
            StockPrice[] watchlistWithPrices = stockService.getLatestPricesForWatchList();
            String[] watchList = stockService.getWatchlist();
            return new StockWatchlistResponse(null, watchList, watchlistWithPrices, null);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new StockWatchlistResponse(null, null, null);
        }
    }

    @PostMapping("/getAveragePriceForStock")
    public StockWatchlistResponse getAveragePriceForStock(@RequestBody StockWatchlistRequest stockWatchlistRequest, HttpServletResponse response) {
        String[] watchList = stockService.getWatchlist();

        BigDecimal averageStockPrice;

        try {
            averageStockPrice = stockService.getAverageStockPrice(stockWatchlistRequest.getSymbol(), stockWatchlistRequest.getStartDate(),
                    stockWatchlistRequest.getEndDate());
        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), null, watchList, ERROR_INVALID_DATE_FORMAT);
        } catch (InvalidDatesException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), null, watchList, ERROR_INVALID_DATE_ORDER);
        } catch (StockNotInWatchlistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), null, watchList, ERROR_STOCK_NOT_IN_WATCHLIST);
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), null, watchList, null);
        }

        return new StockWatchlistResponse(stockWatchlistRequest.getSymbol(), averageStockPrice, watchList, null);
    }

}