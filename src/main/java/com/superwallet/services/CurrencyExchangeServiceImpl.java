package com.superwallet.services;

import com.superwallet.services.interfaces.CurrencyExchangeService;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final String baseUrl;

    @Autowired
    public CurrencyExchangeServiceImpl(@Value("${exchangerate.api.key}") String apiUrl,
                                       @Value("#{getBaseUrl}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public BigDecimal getConversionRate(String fromCurrencyCode, String toCurrencyCode) {
        try {
            JSONObject jsonResponse = getJsonObject(fromCurrencyCode, toCurrencyCode);
            if ("success".equals(jsonResponse.getString("result"))) {
                return jsonResponse.getBigDecimal("conversion_rate");
            } else {
                //TODO Needs to catch exceptions better
                throw new RuntimeException("Error fetching exchange rates");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.valueOf(-1);
        }
    }

    @Override
    public BigDecimal convertFundsBetweenCurrencies(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount) {
        BigDecimal conversionRate;

        if (!fromCurrencyCode.equals(toCurrencyCode)) {
            conversionRate = getConversionRate(fromCurrencyCode, toCurrencyCode);
        } else {
            conversionRate = BigDecimal.ONE;
        }
        return convertAmount(amount, conversionRate);
    }

    @NotNull
    private JSONObject getJsonObject(String fromCurrency, String toCurrency) throws IOException {
        String urlString = baseUrl + "pair" + "/" + fromCurrency + "/" + toCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse;
    }

    private BigDecimal convertAmount(BigDecimal amount, BigDecimal conversionRate) {
        return amount.multiply(conversionRate);
    }
}
