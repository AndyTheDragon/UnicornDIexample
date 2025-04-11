package dat.utils;

import dat.entities.Unicorn;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        u1.setId(1);
        u2 = new Unicorn("Unicorn2", 7, "Black", 12.0);
        u2.setId(2);


    }

    public Map<Integer,Unicorn> populateMemoryDB()
    {
        Map<Integer, Unicorn> memoryDB = new HashMap<>();
        memoryDB.put(u1.getId(), u1);
        memoryDB.put(u2.getId(), u2);
        return memoryDB;
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
