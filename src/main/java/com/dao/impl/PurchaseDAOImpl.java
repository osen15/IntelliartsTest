package com.dao.impl;

import com.dao.PurchaseDAO;
import com.exceptions.InternalServerError;
import com.models.Purchase;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;


@Repository
@Transactional
public class PurchaseDAOImpl extends GeneralDAOImpl<Purchase> implements PurchaseDAO {
    private String REMOVE_PURCHASES_BY_DATE =
            "DELETE FROM PURCHASES WHERE TRUNC(DATE_OF_PURCHASE)  = :pDate";
    private String ALL_PURCHASES_BY_DATE =
            "SELECT*FROM PURCHASES ORDER BY DATE_OF_PURCHASE desc";

    @Override
    Class<Purchase> getModelClass() {
        return Purchase.class;
    }

    @Override
    public Purchase addPurchase(Purchase purchase) throws InternalServerError {
        return save(purchase);
    }

    @Override
    public void removePurchasesByDate(Date date) throws InternalServerError {
        entityManager.createNativeQuery(REMOVE_PURCHASES_BY_DATE)
                .setParameter("pDate", date, TemporalType.DATE)
                .executeUpdate();
    }

    @Override
    public ArrayList<Purchase> allPurchases() throws InternalServerError {
        ArrayList<Purchase> purchases = (ArrayList<Purchase>) entityManager
                .createNativeQuery(ALL_PURCHASES_BY_DATE, getModelClass())
                .getResultList();
        return purchases;
    }

    @Override
    public Purchase findById(Long id) throws InternalServerError {
        return super.findById(id);
    }

    @Override
    public Purchase save(Purchase purchase) throws InternalServerError {
        return super.save(purchase);
    }
}
