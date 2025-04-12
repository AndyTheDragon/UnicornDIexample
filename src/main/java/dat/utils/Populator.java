package dat.utils;

import dat.config.ConnectionPool;
import dat.entities.Unicorn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Populator
{
    private Logger logger = LoggerFactory.getLogger(Populator.class);

    private Unicorn u1, u2;

    public Populator()
    {
        u1 = new Unicorn("Unicorn1", 5, "White", 10.0);
        u2 = new Unicorn("Unicorn2", 7, "Black", 12.0);


    }

    public Map<Integer,Unicorn> populateMemoryDB()
    {
        u1.setId(1);
        u2.setId(2);
        Map<Integer, Unicorn> memoryDB = new HashMap<>();
        memoryDB.put(u1.getId(), u1);
        memoryDB.put(u2.getId(), u2);
        return memoryDB;
    }

    public List<Unicorn> populateDB()
    {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        String sql = "INSERT INTO unicorns (unicorn_name, age, color, powerstrength) VALUES (?, ?, ?, ?)";
        try (Connection db = connectionPool.getConnection();
             PreparedStatement ps = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            db.createStatement().executeUpdate("DELETE FROM unicorns");
            ps.setString(1, u1.getName());
            ps.setInt(2, u1.getAge());
            ps.setString(3, u1.getColor());
            ps.setDouble(4, u1.getPowerStrength());
            ps.addBatch();

            ps.setString(1, u2.getName());
            ps.setInt(2, u2.getAge());
            ps.setString(3, u2.getColor());
            ps.setDouble(4, u2.getPowerStrength());
            ps.addBatch();

            ps.executeBatch();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) u1.setId(rs.getInt(1));
            if (rs.next()) u2.setId(rs.getInt(1));


            }
        catch (Exception e)
        {
            logger.error("Error populating database", e);
        }
        return List.of(u1, u2);
    }

    public List<Unicorn> populateEntityManager(EntityManagerFactory emf)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Unicorn ").executeUpdate();

            em.persist(u1);
            em.persist(u2);

            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            logger.error("Error populating database", e);
        }
        return List.of(u1, u2);
    }


}
