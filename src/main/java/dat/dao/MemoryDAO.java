package dat.dao;

import dat.entities.Unicorn;
import dat.exceptions.DaoException;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryDAO<T extends Unicorn> implements ICrudDAO<T>
{
    private final Map<Integer,T> memoryDB = new HashMap<>();

    public MemoryDAO() {
        // Default constructor
    }

    public MemoryDAO(Map<Integer, T> memoryDB)
    {
        this.memoryDB.putAll(memoryDB);
    }

    @Override
    public T create(T entity) throws DaoException
    {
        try
        {
            if (entity.getId() == null || entity.getId() == 0)
            {
                entity.setId(memoryDB.keySet().stream().max(Integer::compareTo).orElse(0) + 1);
            }
            if (memoryDB.containsKey(entity.getId()))
            {
                throw new IllegalArgumentException("Entity with this ID already exists");
            }
            memoryDB.put(entity.getId(), entity);
            return entity;
        }
        catch (IllegalArgumentException e)
        {
            throw new DaoException("Error creating entity: " + e.getMessage(), e);
        } catch (Exception e)
        {
            throw new DaoException("Error creating entity", e);
        }
    }

    @Override
    public T getById(int id) throws DaoException
    {
        try
        {
            if (!memoryDB.containsKey(id))
            {
                throw new EntityNotFoundException("Entity with this ID does not exist");
            }
            return memoryDB.get(id);
        }
        catch (EntityNotFoundException e)
        {
            throw new DaoException("Error fetching entity by ID: " + e.getMessage(), e);
        }
        catch (Exception e)
        {
            throw new DaoException("Error fetching entity by ID", e);
        }
    }

    @Override
    public List<T> getAll()
    {
        return memoryDB.values().stream().toList();
    }

    @Override
    public T update(T entity) throws DaoException
    {
        try
        {
            memoryDB.put(entity.getId(), entity);
            return entity;
        } catch (Exception e)
        {
            throw new DaoException("Error updating entity", e);
        }
    }

    @Override
    public void delete(T entity) throws DaoException
    {
        try
        {
            memoryDB.remove(entity.getId());
        }
        catch (Exception e)
        {
            throw new DaoException("Error deleting entity", e);
        }
    }
}
