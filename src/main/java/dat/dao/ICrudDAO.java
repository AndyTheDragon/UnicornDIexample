package dat.dao;

import dat.exceptions.DaoException;

import java.util.List;

public interface ICrudDAO<T>
{
    T create(T entity) throws DaoException;
    T getById(int id) throws DaoException;
    List<T> getAll() throws DaoException;
    T update(T entity) throws DaoException;
    void delete(T entity) throws DaoException;
}
