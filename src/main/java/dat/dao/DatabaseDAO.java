package dat.dao;

import dat.config.ConnectionPool;
import dat.entities.Unicorn;
import dat.exceptions.DaoException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseDAO implements ICrudDAO<Unicorn>
{
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final Logger logger = LoggerFactory.getLogger(DatabaseDAO.class);

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
                logger.error("Failed to create entity, no ID obtained.");
                throw new DaoException("Failed to create entity, no ID obtained.");
            }
            return entity;
        }
        catch (SQLException e)
        {
            logger.error("Error creating entity. ", e);
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
                logger.error("Entity with ID {} not found", id);
                throw new DaoException("Error fetching entity by ID: Entity with this ID does not exist");
            }
        }
        catch (SQLException e)
        {
            logger.error("Error fetching entity by ID. ", e);
            throw new DaoException("Error fetching entity by ID", e);
        }
    }

    @Override
    public List<Unicorn> getAll() throws DaoException
    {
        String sql = "SELECT * FROM unicorns";
        try (Connection db = connectionPool.getConnection();
             PreparedStatement ps = db.prepareStatement(sql))
        {
            var rs = ps.executeQuery();
            List<Unicorn> unicorns = new java.util.ArrayList<>();
            while (rs.next())
            {
                Unicorn unicorn = new Unicorn(
                        rs.getString("unicorn_name"),
                        rs.getInt("age"),
                        rs.getString("color"),
                        rs.getDouble("powerstrength")
                );
                unicorn.setId(rs.getInt("id"));
                unicorns.add(unicorn);
            }
            return unicorns;
        }
        catch (SQLException e)
        {
            logger.error("Error fetching all entities. ", e);
            throw new DaoException("Error fetching all entities", e);
        }
    }

    @Override
    public Unicorn update(Unicorn entity) throws DaoException
    {
        String sql = "UPDATE unicorns SET unicorn_name = ?, age = ?, color = ?, powerstrength = ? WHERE id = ?";
        try (Connection db = connectionPool.getConnection();
             PreparedStatement ps = db.prepareStatement(sql))
        {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getAge());
            ps.setString(3, entity.getColor());
            ps.setDouble(4, entity.getPowerStrength());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
            return entity;
        }
        catch (SQLException e)
        {
            logger.error("Error updating entity. ", e);
            throw new DaoException("Error updating entity", e);
        }
    }

    @Override
    public void delete(Unicorn entity) throws DaoException
    {
        String sql = "DELETE FROM unicorns WHERE id = ?";
        try (Connection db = connectionPool.getConnection();
             PreparedStatement ps = db.prepareStatement(sql))
        {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            logger.error("Error deleting entity. ", e);
            throw new DaoException("Error deleting entity", e);
        }
    }
}
