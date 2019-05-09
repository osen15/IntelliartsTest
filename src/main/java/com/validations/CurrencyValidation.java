package com.validations;


import com.exceptions.BadRequestEXception;
import com.utils.fixerClient.FixerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyValidation {
    private final FixerClient fixerClient;

    @Autowired
    public CurrencyValidation(FixerClient fixerClient) {
        this.fixerClient = fixerClient;
    }

    public void validation(String currency) throws BadRequestEXception {
        if (currency == null || currency.isEmpty()) {
            throw new BadRequestEXception("currency can't be null");
        }
        fixerClient.supportedCurrencies(currency);
    }

}
