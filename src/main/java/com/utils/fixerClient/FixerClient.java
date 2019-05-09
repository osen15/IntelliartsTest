package com.utils.fixerClient;

import com.exceptions.BadRequestEXception;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

@Component
public class FixerClient {
    private final String ENDPOINT = "latest";
    private final String ACCESS_KEY = "5d784dd7c6c3d7d4c4525f2d6821f1b2";
    private final String FIXER_URL = "http://data.fixer.io/api/" + ENDPOINT + "?access_key=" + ACCESS_KEY;


    private RestTemplate restTemplate;
    private Map rates;

    public FixerClient() {
        restTemplate = new RestTemplate();
    }

    public Map getRates() {
        try {
            rates = (Map) Objects
                    .requireNonNull(restTemplate.getForObject(new URI(FIXER_URL), Map.class)
                    ).get("rates");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(rates);
        return rates;
    }
   public void supportedCurrencies(String currency){
      if (!getRates().containsKey(currency.toUpperCase()))
          throw new BadRequestEXception(currency + "not supported");

   }
}