package dat.dao;

import dat.config.HibernateConfig;
import dat.entities.Unicorn;
import dat.exceptions.DaoException;
import dat.utils.Populator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class CrudDAOTest
{
    private ICrudDAO<Unicorn> dao;
    private final Populator populator = new Populator();
    private Unicorn u1, u2;

    void setupMemoryDB()
    {
        Map<Integer, Unicorn> memoryDB = populator.populateMemoryDB();
        dao = new MemoryDAO<>(memoryDB);
        u1 = memoryDB.get(1);
        u2 = memoryDB.get(2);
    }

    void setupDatabase()
    {
        List<Unicorn> dbList = populator.populateDB();
        dao = new DatabaseDAO();
        u1 = dbList.get(0);
        u2 = dbList.get(1);
    }

    void setupEntityManager()
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        List<Unicorn> dbList = populator.populateEntityManager(emf);
        //dao = new EntityManagerDAO();
        u1 = dbList.get(0);
        u2 = dbList.get(1);
    }

    @BeforeEach
    void setUp()
    {
        //setupMemoryDB();
        setupDatabase();
        //setupEntityManager();
    }

    @Test
    void testCreate()
    {
        try
        {
            Unicorn u3 = new Unicorn("Unicorn3", 8, "Blue", 15.0);
            Unicorn created = dao.create(u3);
            assertThat(created.getId(), is(u3.getId()));
            assertThat(created.getName(), is(u3.getName()));
            assertThat(created.getAge(), is(u3.getAge()));
            assertThat(created.getColor(), is(u3.getColor()));
            assertThat(created.getPowerStrength(), is(u3.getPowerStrength()));
            assertThat(created, samePropertyValuesAs(u3));
        }
        catch (DaoException e)
        {
            fail();
        }
    }

    @Test
    void testGetById_success()
    {
        try
        {
            Unicorn found = dao.getById(u1.getId());
            assertThat(found, samePropertyValuesAs(u1));
        }
        catch (DaoException e)
        {
            fail();
        }
    }

    @Test
    void testGetById_notFound()
    {
        // Act
        DaoException exception = assertThrows(DaoException.class, () -> dao.getById(999));

        // Assert
        assertThat(exception.getMessage(), is("Error fetching entity by ID: Entity with this ID does not exist"));

    }

    @Test
    void testGetAll()
    {
        try
        {
            // Act
            List<Unicorn> all = dao.getAll();

            // Assert
            assertThat(all, hasSize(2));
            assertThat(all, containsInAnyOrder(u1, u2));
        }
        catch (DaoException e)
        {
            fail();
        }
    }

    @Test
    void testUpdate()
    {
        try
        {
            // Arrange
            u1.setName("UpdatedName");

            // Act
            Unicorn updated = dao.update(u1);

            // Assert
            assertThat(updated, samePropertyValuesAs(u1));
        }
        catch (DaoException e)
        {
            fail();
        }
    }

    @Test
    void testDelete()
    {
        try
        {
            // Act
            dao.delete(u1);

            // Assert
            assertThat(dao.getAll(), hasSize(1));
            DaoException exception = assertThrows(DaoException.class, () -> dao.getById(u1.getId()));
            assertThat(exception.getMessage(), is("Error fetching entity by ID: Entity with this ID does not exist"));
        }
        catch (DaoException e)
        {
            fail();
        }
    }


}
