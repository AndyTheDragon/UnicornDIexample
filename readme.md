# UnicornDIexample Project Overview

## Introduction
UnicornDIexample is a Java application that demonstrates the implementation of the Data Access Object (DAO) pattern and dependency injection principles. The project provides a simple framework for managing Unicorn entities with both in-memory and database-backed storage options.

## Project Structure
The project follows a standard layered architecture with the following components:

### Main Packages
- **config**: Configuration classes for the application, including Hibernate configuration
- **dao**: Data Access Objects for database operations
- **entities**: Entity classes representing database tables
- **exceptions**: Custom exception classes
- **utils**: Utility classes for database population and property reading

### Domain Model
The application manages the following entity:

1. **Unicorn**:
    - Properties: id, name, age, color, powerStrength
    - JPA annotations for database mapping

### DAO Pattern Implementation
The project implements the DAO pattern with:
- **ICrudDAO<T>**: Generic interface defining CRUD operations
- **MemoryDAO<T>**: In-memory implementation using a HashMap

## Technologies Used
- **Java 17**: Programming language
- **Maven**: Build tool
- **Hibernate**: ORM for database operations
- **PostgreSQL**: Database
- **JUnit**: Testing framework
- **TestContainers**: For database testing
- **Lombok**: Reduces boilerplate code
- **SLF4J & Logback**: Logging

## Getting Started
1. Clone the repository
2. Create a `config.properties` file in the resources directory with the following properties:
   ```
   DB_NAME=your_db_name
   DB_URL=jdbc:postgresql://localhost:5432/%s?currentSchema=public
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   ```
3. Build the project with Maven: `mvn clean package`
4. Run the application: `java -jar target/app.jar`

## Dependency Injection
The project demonstrates dependency injection principles by:
1. Using interfaces (ICrudDAO) to define contracts
2. Implementing those interfaces with concrete classes (MemoryDAO)
3. Allowing for different implementations to be injected at runtime

## Testing
The project includes unit tests for the DAO implementations using JUnit and TestContainers. Tests can be run with Maven: `mvn test`

Key test classes:
- **CrudDAOTest**: Tests for the ICrudDAO implementation

## Utilities
- **Populator**: Utility for populating test data
- **PropertyReader**: Utility for reading configuration properties
- **HibernateConfig**: Configuration for Hibernate ORM
- **ConnectionPool**: Singleton for managing database connections

# Simple exercise in Dependency Injection
We will be working with the [Unicorn entity](src/main/java/dat/entities/Unicorn.java). The entity is already created for you, and it is annotated with JPA annotations. You can find the entity in the `src/main/java/dat/entities` package.
Before you start, you should create a `config.properties` file in the `src/main/resources` folder. This file should contain the following properties:
```properties
DB_NAME=your_db_name
DB_URL=jdbc:postgresql://localhost:5432/%s?currentSchema=public
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

## Step 1 : the interface
Create an interface `ICrudDAO` with the following methods:
* `create() throws DaoException;`
* `getAll() throws DaoException;`
* `getById() throws DaoException;`
* `update() throws DaoException;`
* `delete() throws DaoException;`

Decide for yourself if you want it to be generic or not. The tests I've written expects it to be generic, but you can change it if you want.

## Step 2 : the mock DAO
Create a class `MemoryDAO` that implements the `ICrudDAO` interface. This class should be a mock implementation that stores the data in memory. You can use a `Map<Integer, Object>` to store the data, where the key is the ID and the value is the object.

* When creating a new object, you should generate a new ID, and set it on the object. Return the created object.
* When getting all objects, you should return a list of all objects in the map.
* When getting an object by ID, you should return the object with the given ID.
    * If the object does not exist, you should throw a `DaoException` with the message "Error fetching entity by ID: Entity with this ID does not exist".
* When updating an object return the updated object.
* When deleting an object, take the object you want to delete as a parameter.

The Populator class (used in the tests) expects a constructor that looks something like this:
```java
    public MemoryDAO(Map<Integer, Unicorn> memoryDB)
    {
        this.memoryDB.putAll(memoryDB);
    }
```

## Step 3 : the test
Run the tests in `CrudDAOTest`. They should all pass. If they don't, you need to fix your implementation. If you get stuck, look at my solution in the solution branch on GitHub.

## Step 4 : the database
Create a class `DatabaseDAO` that implements the `ICrudDAO` interface. This class should use JDBC to store the data in the database. You can use the `ConnectionPool` class to get a connection to the database.

* When creating a new object, you should insert it into the database and return the created object. The database should handle the ID generation.
* When getting all objects, you should return a list of all objects in the database.
* When getting an object by ID, you should return the object with the given ID.
    * If the object does not exist, you should throw a `DaoException` with the message "Error fetching entity by ID: Entity with this ID does not exist".
* When updating an object, you should update the object in the database and return the updated object.
* When deleting an object, take the object you want to delete as a parameter.

## Step 5 : the test
Fix the @BeforeAll `setup()` method in the `CrudDAOTest` class to use the `DatabaseDAO` class instead of the `MemoryDAO` class.

## Step 6 : the entity manager
Create a class `EntityManagerDAO` that implements the `ICrudDAO` interface. This class should use the `EntityManager` to store the data in the database. You can use the `HibernateConfig.getEntityManagerFactory()` method to get the `EntityManager`.

## Step 7 : the test
Fix the @BeforeAll `setup()` method in the `CrudDAOTest` class to use the `EntityManagerDAO` class instead of the `DatabaseDAO` class.

## Bonus question
Should you inject the ConnectionPool/EntityManager into the DAO classes? Why or why not?