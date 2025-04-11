package dat.dao;

import dat.config.ConnectionPool;
import dat.entities.Unicorn;
import dat.exceptions.DaoException;
import jakarta.persistence.EntityNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseDAO implements ICrudDAO<Unicorn>
{
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public DatabaseDAO() {
        // Default constructor
    }


    @Override
    public Unicorn create(Unicorn entity) throws DaoException
    {
        String sql = "INSERT INTO unicorns (unicorn_name, age, color, powerstrength) VALUES (?, ?, ?, ?)";
        try (Connection db = connectionPool.getConnection();
             PreparedStatement ps = db.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getAge());
            ps.setString(3, entity.getColor());
            ps.setDouble(4, entity.getPowerStrength());
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            if (rs.next())
            {
                entity.setId(rs.getInt(1));
            }
            else
            {
                throw new DaoException("Failed to create entity, no ID obtained.");
            }
            return entity;
        }
        catch (SQLException e)
        {
            throw new DaoException("Error creating entity", e);
        }
    }

    @Override
    public Unicorn getById(int id) throws DaoException
    {
        String sql = "SELECT * FROM unicorns WHERE id = ?";
        try (Connection db = connectionPool.getConnection();
             PreparedStatement ps = db.prepareStatement(sql))
        {
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            if (rs.next())
            {
                Unicorn unicorn = new Unicorn(
                        rs.getString("unicorn_name"),
                        rs.getInt("age"),
                        rs.getString("color"),
                        rs.getDouble("powerstrength")
                );
                unicorn.setId(rs.getInt("id"));
                return unicorn;
            }
            else
            {
                throw new DaoException("Error fetching entity by ID: Entity with this ID does not exist");
            }
        }
        catch (SQLException e)
        {
            throw new DaoException("Error fetching entity by ID", e);
        }
    }

    @Override
    public List<Unicorn> getAll()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Unicorn update(Unicorn entity) throws DaoException
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void delete(Unicorn entity) throws DaoException
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
