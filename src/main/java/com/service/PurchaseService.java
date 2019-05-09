package com.service;

import com.dao.PurchaseDAO;
import com.exceptions.BadRequestEXception;
import com.exceptions.InternalServerError;
import com.models.Purchase;
import com.utils.fixerClient.FixerClient;
import com.validations.CurrencyValidation;
import com.validations.YearValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static ognl.OgnlOps.doubleValue;


@Service
@Transactional
public class PurchaseService {
    private final PurchaseDAO purchaseDAO;
    private final FixerClient fixerClient;
    private final CurrencyValidation currencyValidation;
    private final YearValidation yearValidation;
    private final Purchase finalPurchase;


    @Autowired
    public PurchaseService(PurchaseDAO purchaseDAO, FixerClient fixerClient, CurrencyValidation currencyValidation, YearValidation yearValidation, Purchase finalPurchase) {
        this.purchaseDAO = purchaseDAO;
        this.fixerClient = fixerClient;
        this.currencyValidation = currencyValidation;
        this.yearValidation = yearValidation;
        this.finalPurchase = finalPurchase;
    }

    public Map<LocalDate, List<Purchase>> addPurchase(Purchase purchase) throws InternalServerError {
        purchaseDAO.addPurchase(purchase);
        return getAll();
    }

    public Map<LocalDate, List<Purchase>> clear(String strDate) throws BadRequestEXception, InternalServerError {
        LocalDate localDate = LocalDate.parse(strDate);
        if (localDate == null)
            throw new BadRequestEXception("bad request");
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println(date);
        purchaseDAO.removePurchasesByDate(date);
        return getAll();
    }

    public Map<LocalDate, List<Purchase>> getAll() {
        List<Purchase> purchases = purchaseDAO.allPurchases();
        Map<LocalDate, List<Purchase>> result = new LinkedHashMap<>();

        for (Purchase purchase : purchases) {
            finalPurchase.setAmount(purchase.getAmount());
            finalPurchase.setCurrency(purchase.getCurrency());
            finalPurchase.setName(purchase.getName());
            if (!result.containsKey(purchase.getDateOfPurchase())) {
                result.put(purchase.getDateOfPurchase(), new ArrayList<>(
                        Collections.singletonList(finalPurchase)));
            } else {
                finalPurchase.setAmount(purchase.getAmount());
                finalPurchase.setCurrency(purchase.getCurrency());
                finalPurchase.setName(purchase.getName());
                result.get(purchase.getDateOfPurchase())
                        .add(finalPurchase);
            }
        }
        return result;
    }

    public String getReport(String year, String currency) {
        yearValidation.validation(year);
        currencyValidation.validation(currency);
        double report;
        Map rates = fixerClient.getRates();
        List<Purchase> purchases = purchaseDAO.allPurchases();
        report = purchases.stream()
                .filter(purchase -> String.valueOf(purchase.getDateOfPurchase()).contains(year))
                .mapToDouble(purchase -> purchase.getAmount()
                        * doubleValue(rates.get(currency))
                        / doubleValue(rates.get(String.valueOf(purchase.getCurrency())))).sum();
        return Math.round(report * 100.0) / 100.0 + " " + currency;
    }
}