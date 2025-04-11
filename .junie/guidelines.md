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

## Example Usage
```java
// Create a DAO
ICrudDAO<Unicorn> dao = new MemoryDAO<>();

// Create a unicorn
Unicorn unicorn = new Unicorn("Rainbow", 5, "White", 10.0);
dao.create(unicorn);

// Get all unicorns
List<Unicorn> unicorns = dao.getAll();

// Get unicorn by ID
Unicorn found = dao.getById(unicorn.getId());

// Update unicorn
unicorn.setName("Updated Name");
dao.update(unicorn);

// Delete unicorn
dao.delete(unicorn);
```