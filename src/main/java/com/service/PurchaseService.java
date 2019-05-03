package com.service;

import com.dao.PurchaseDAO;
import com.exceptions.BadRequestEXception;
import com.exceptions.InternalServerError;
import com.models.Purchase;
import com.utils.fixerClient.FixerClient;
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

    @Autowired
    public PurchaseService(PurchaseDAO purchaseDAO, FixerClient fixerClient) {
        this.purchaseDAO = purchaseDAO;
        this.fixerClient = fixerClient;
    }

    public Map<LocalDate, List<String>> addPurchase(Purchase purchase) throws InternalServerError {
        purchaseDAO.addPurchase(purchase);
        return getAll();
    }

    public ArrayList<Purchase> all(String strDate) throws InternalServerError, BadRequestEXception {
        LocalDate localDate = LocalDate.parse(strDate);
        if (localDate == null)
            throw new BadRequestEXception("bad request");
        ArrayList<Purchase> purchases = (ArrayList<Purchase>) purchaseDAO.allPurchases();
        if (purchases == null || purchases.isEmpty())
            throw new BadRequestEXception("bad request");
        return purchases;
    }

    public void clear(String strDate) throws BadRequestEXception, InternalServerError {
        LocalDate localDate = LocalDate.parse(strDate);
        if (localDate == null)
            throw new BadRequestEXception("bad request");
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        purchaseDAO.removePurchasesByDate(date);
    }

    public Map<LocalDate, List<String>> getAll() {
        List<Purchase> purchases = purchaseDAO.allPurchases();
        Map<LocalDate, List<String>> result = new LinkedHashMap<>();

        for (Purchase purchase : purchases) {
            if (!result.containsKey(purchase.getDateOfPurchase())) {
                result.put(purchase.getDateOfPurchase(), new ArrayList<>(
                        Collections.singletonList(purchase.getName() + " "
                                + String.valueOf(purchase.getAmount() + " "
                                + String.valueOf(purchase.getCurrency())))));
            } else {
                result.get(purchase.getDateOfPurchase())
                        .add(purchase.getName() + " "
                                + String.valueOf(purchase.getAmount()) + " "
                                + String.valueOf(purchase.getCurrency()));
            }
        }
        return result;
    }

    public String getReport(String year, String currency) {
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