package com.coding.exercise;

import com.coding.exercise.controller.StocksController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StocksControllerTests extends AbstractConrollerTests {

    @BeforeEach
    public void setUp() throws Exception{
        removeAllStocks();
    }

    @Test
    void addOneValidStockSymbol() throws Exception {
        final String requestSymbol = "goog";
        MvcResult mvcResult = addStock(requestSymbol);

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        String responseSymbol = (String)map.get("symbol");

        assertThat(watchList.size()).isEqualTo(1);
        assertThat(watchList).contains(requestSymbol.toUpperCase());
        assertThat(responseSymbol).isEqualTo(requestSymbol);
    }

    @Test
    void addTwoValidStockSymbols() throws Exception {
        String[] requestSymbols = {"goog", "aapl"};
        addStock(requestSymbols[0]);
        MvcResult mvcResult = addStock(requestSymbols[1]);
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        String symbol = (String)map.get("symbol");

        assertThat(watchList.size()).isEqualTo(2);
        assertThat(watchList.get(0)).isEqualTo(requestSymbols[1].toUpperCase());//watchlist is alphabetized
        assertThat(watchList.get(1)).isEqualTo(requestSymbols[0].toUpperCase());//watchlist is alphabetized
        assertThat(symbol).isEqualTo(requestSymbols[1]);
    }

    @Test
    void addInvalidStockSymbol() throws Exception {
        String requestSymbol = "invalid_symbol";
        MvcResult mvcResult = addStock(requestSymbol);
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        String responseSymbol = (String)map.get("symbol");
        String error = (String)map.get("error");

        assertThat(watchList.size()).isEqualTo(0);
        assertThat(responseSymbol).isEqualTo(requestSymbol);
        assertThat(error).isEqualTo(StocksController.ERROR_SYMBOL_INVALID_SYMBOL);
    }

    @Test
    void addWithoutStockSymbol() throws Exception {
        String requestSymbol = null;
        MvcResult mvcResult = addStock(requestSymbol);
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        String responseSymbol = (String)map.get("symbol");
        String error = (String)map.get("error");

        assertThat(watchList.size()).isEqualTo(0);

        assertThat(responseSymbol).isEqualTo(requestSymbol);
        assertThat(error).isEqualTo(StocksController.ERROR_SYMBOL_INVALID_SYMBOL);
    }

    @Test
    void addStocksAndCheckWatchlist() throws Exception {
        String[] requestSymbols = {"goog", "aapl"};

        addStock(requestSymbols[0]);
        addStock(requestSymbols[1]);

        MvcResult mvcResult = getWatchlist();
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");

        assertThat(watchList.size()).isEqualTo(2);
        assertThat(watchList).contains("GOOG");
        assertThat(watchList).contains("AAPL");

    }

    @Test
    void addStocksAndCheckPrices() throws Exception {
        String[] requestSymbols = {"goog", "aapl"};

        addStock(requestSymbols[0]);
        addStock(requestSymbols[1]);

        MvcResult mvcResult = getPrices();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<Map> watchListWithPrices = (List<Map>) map.get("watchListWithPrices");
        String error = (String)map.get("error");

        Map element1 = watchListWithPrices.get(0);
        Map element2 = watchListWithPrices.get(1);

        String symbol1 = (String)element1.get("symbol");
        Double price1 = (Double)element1.get("price");

        String symbol2 = (String)element2.get("symbol");
        Double price2 = (Double)element2.get("price");

        assertThat(symbol1).isEqualTo(requestSymbols[1].toUpperCase());//watchlist is alphabetized
        assertThat(symbol2).isEqualTo(requestSymbols[0].toUpperCase());//watchlist is alphabetized

        assertThat(price1).isGreaterThan(0.0);
        assertThat(price2).isGreaterThan(0.0);

        assertThat(error).isNull();
    }

    @Test
    void addTwoStocksAndRemoveOne() throws Exception {
        String[] requestSymbols = {"goog", "aapl"};

        addStock(requestSymbols[0]);
        addStock(requestSymbols[1]);

        MvcResult mvcResult = removeStock(requestSymbols[1]);
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        String symbol = (String)map.get("symbol");

        assertThat(watchList.size()).isEqualTo(1);
        assertThat(watchList).contains(requestSymbols[0].toUpperCase());
        assertThat(symbol).isEqualTo(requestSymbols[1]);
    }

    @Test
    void addStocksAndRemoveAllStocks() throws Exception {

        String[] requestSymbols = {"goog", "aapl"};
        addStock(requestSymbols[0]);
        addStock(requestSymbols[1]);

        MvcResult mvcResult = getWatchlist();
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");

        assertThat(watchList.size()).isEqualTo(2);
        assertThat(watchList).contains(requestSymbols[0].toUpperCase());
        assertThat(watchList).contains(requestSymbols[1].toUpperCase());

        removeAllStocks();

        MvcResult mvcResult2 = getWatchlist();
        int status2 = mvcResult.getResponse().getStatus();
        assertThat(status2).isEqualTo(200);
        String content2 = mvcResult2.getResponse().getContentAsString();

        HashMap map2 = mapFromJson(content2, HashMap.class);
        List<String> watchList2 = ((List<String>)map2.get("watchList"));
        assertThat(watchList2.size()).isEqualTo(0);

    }

    @Test
    void getAveragePriceForStockValid() throws Exception {

        String[] requestSymbols = {"goog", "aapl"};
        addStock(requestSymbols[0]);
        addStock(requestSymbols[1]);

        MvcResult mvcResult = getAveragePriceForStock(requestSymbols[0], "2021-06-21", "2021-06-25");
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        Double averagePrice = (Double)map.get("averageStockPrice");
        assertThat(watchList.size()).isEqualTo(2);
        
        //calculated manually with Excel to be 2536.771973
        assertThat(averagePrice).isGreaterThan(2536.771972);
        assertThat(averagePrice).isLessThan(2536.771974);

    }

    @Test
    void getAveragePriceForStockInvalidDateFormat() throws Exception {

        String requestSymbol = "goog";
        String startDate = "ABCD";
        String endDate = "2021-06-25";

        addStock(requestSymbol);

        MvcResult mvcResult = getAveragePriceForStock(requestSymbol, startDate, endDate);
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        Double averagePrice = (Double)map.get("averageStockPrice");
        String error = (String)map.get("error");

        assertThat(watchList.size()).isEqualTo(1);
        assertThat(averagePrice).isNull();
        assertThat(error).isEqualTo(StocksController.ERROR_INVALID_DATE_FORMAT);

    }

    @Test
    void getAveragePriceForStockStartDateAfterEndDate() throws Exception {

        String requestSymbol = "goog";
        //Valid dates, but start date later than end date
        String startDate = "2021-06-25";
        String endDate = "2021-06-21";

        addStock(requestSymbol);

        MvcResult mvcResult = getAveragePriceForStock(requestSymbol, startDate, endDate);
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        Double averagePrice = (Double)map.get("averageStockPrice");
        String error = (String)map.get("error");

        assertThat(watchList.size()).isEqualTo(1);
        assertThat(averagePrice).isNull();
        assertThat(error).isEqualTo(StocksController.ERROR_INVALID_DATE_ORDER);

    }

    @Test
    void getAveragePriceForStockNotInWatchlist() throws Exception {
        //no call to addStock, watchList empty

        String requestSymbol = "goog";
        String startDate = "2021-06-21";
        String endDate = "2021-06-25";

        MvcResult mvcResult = getAveragePriceForStock(requestSymbol, startDate, endDate);
        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
        String content = mvcResult.getResponse().getContentAsString();

        HashMap map = mapFromJson(content, HashMap.class);
        List<String> watchList = (List<String>)map.get("watchList");
        Double averagePrice = (Double)map.get("averageStockPrice");
        String error = (String)map.get("error");

        assertThat(watchList.size()).isEqualTo(0);
        assertThat(averagePrice).isNull();
        assertThat(error).isEqualTo(StocksController.ERROR_STOCK_NOT_IN_WATCHLIST);

    }

}
