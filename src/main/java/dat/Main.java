package dat;

import dat.config.HibernateConfig;
import dat.dao.DatabaseDAO;
import dat.dao.ICrudDAO;
import dat.dao.MemoryDAO;
import dat.entities.Unicorn;
import jakarta.persistence.EntityManagerFactory;


public class Main
{
    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static ICrudDAO<Unicorn> dao;

    public static void main(String[] args)
    {
        // Initialize the DAO
        dao = new DatabaseDAO();

        // Create a new Unicorn
        Unicorn unicorn = new Unicorn("Sprinkles", 5, "Pink", 10.0);

        // Save the Unicorn to the database
        try
        {
            Unicorn createdUnicorn = dao.create(unicorn);
            unicorn = createdUnicorn;
            System.out.println("Created Unicorn: " + createdUnicorn);
        }
        catch (Exception e)
        {
            System.err.println("Error creating Unicorn: " + e.getMessage());
        }

        // Retrieve the Unicorn by ID
        try
        {
            Unicorn retrievedUnicorn = dao.getById(unicorn.getId());
            System.out.println("Retrieved Unicorn: " + retrievedUnicorn);
        }
        catch (Exception e)
        {
            System.err.println("Error retrieving Unicorn: " + e.getMessage());
        }

        // Update the Unicorn
        try
        {
            Unicorn unicornToUpdate = new Unicorn("Sparkles", 6, "Blue", 12.0);
            unicornToUpdate.setId(unicorn.getId()); // Set the ID of the Unicorn to update
            Unicorn updatedUnicorn = dao.update(unicornToUpdate);
            System.out.println("Updated Unicorn: " + updatedUnicorn);
        }
        catch (Exception e)
        {
            System.err.println("Error updating Unicorn: " + e.getMessage());
        }

        // Delete the Unicorn
        try
        {
            dao.delete(dao.getById(unicorn.getId()));
            System.out.println("Unicorn deleted successfully.");
        }
        catch (Exception e)
        {
            System.err.println("Error deleting Unicorn: " + e.getMessage());
        }
    }
}