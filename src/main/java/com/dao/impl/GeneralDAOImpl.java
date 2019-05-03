package com.dao.impl;

import com.dao.GeneralDAO;
import com.exceptions.InternalServerError;
import com.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public abstract class GeneralDAOImpl<T> implements GeneralDAO<T> {
    private String SQL_DELETE_BY_ID = "DELETE FROM " + getModelClass() + " t WHERE t.id = :id";
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public T save(T t) throws InternalServerError {
        try {
            entityManager.persist(t);

        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
        return t;
    }

    @Override
    public T update(T t) throws InternalServerError {
        try {
            entityManager.merge(t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws InternalServerError {
        int res;
        try {
            res = entityManager.createQuery(SQL_DELETE_BY_ID).setParameter("id", id).executeUpdate();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage(), e.getCause());
        }
        if (res == 0) {

            throw new InternalServerError("Entity " + getModelClass() + " with id: " + id + " was not deleted");
        }
    }

    @Override
    public T findById(Long id) throws InternalServerError {
        T t = null;
        try {
            t = entityManager.find(getModelClass(), id);
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage(), e.getCause());
        }
        if (t == null) {
            throw new NotFoundException("Entity " + getModelClass() + " with id " + id + " was not found");
        }
        return t;
    }

    abstract Class<T> getModelClass();
}


