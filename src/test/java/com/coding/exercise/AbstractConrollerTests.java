package com.coding.exercise;

import com.coding.exercise.request.StockWatchlistRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractConrollerTests {

    @Autowired
    MockMvc mockMvc;

    protected MvcResult addStock(String symbol) throws Exception{
        String uri = "/stocks/addStock";
        StockWatchlistRequest stockWatchlistRequest = new StockWatchlistRequest(symbol, null, null);

        String inputJson = mapToJson(stockWatchlistRequest);
        return mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

    }

    protected MvcResult removeStock(String symbol) throws Exception{
        String uri = "/stocks/removeStock";
        StockWatchlistRequest stockWatchlistRequest = new StockWatchlistRequest(symbol, null, null);

        String inputJson = mapToJson(stockWatchlistRequest);
        return mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

    }

    protected MvcResult removeAllStocks() throws Exception{
        String uri = "/stocks/removeAllStocks";
        return mockMvc.perform(MockMvcRequestBuilders.post(uri))
                .andReturn();

    }

    protected MvcResult getPrices() throws Exception{
        String uri = "/stocks/getLatestPricesForWatchlist";
        return mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
    }

    protected MvcResult getAveragePriceForStock(String symbol, String startDate, String endDate) throws Exception{
        String uri = "/stocks/getAveragePriceForStock";
        StockWatchlistRequest stockWatchlistRequest = new StockWatchlistRequest(symbol, startDate, endDate);

        String inputJson = mapToJson(stockWatchlistRequest);
        return mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

    }

    protected MvcResult getWatchlist() throws Exception{
        String uri = "/stocks/getWatchList";
        return mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

}
