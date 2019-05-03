package com.dao;

import com.exceptions.InternalServerError;
import com.models.Purchase;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PurchaseDAO extends GeneralDAO<Purchase> {

    Purchase addPurchase(Purchase purchase) throws InternalServerError;
    void removePurchasesByDate(Date date) throws InternalServerError;
    List allPurchases() throws InternalServerError;





}
