package com.dao;

import com.exceptions.InternalServerError;

public interface GeneralDAO<T> {

    T save(T t) throws InternalServerError;
    T update(T t) throws InternalServerError;
    void delete(Long id) throws InternalServerError;
    T findById(Long id) throws InternalServerError;

}