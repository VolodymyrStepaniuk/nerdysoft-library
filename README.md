# Library Management REST Service

## Project Overview

This project involves developing a REST service for managing a library's books and members using modern Java technologies and frameworks. The service allows for CRUD operations on books and members, book borrowing and returning functionality, and maintaining borrowing limits.

## Table of Contents

- [Project Requirements](#project-requirements)
- [Setup and Installation](#setup-and-installation)
- [API Endpoints](#api-endpoints)
- [Technologies Used](#technologies-used)

## Project Requirements

### Books

- **Attributes**: Each book has an ID, title, author, and amount.
- **CRUD Operations**: The system supports creating, reading, updating, and deleting books.
- **Duplicate Handling**: If a book with the same name and author exists, its amount is increased by 1. Otherwise, it's treated as a different book.
- **Deletion Restriction**: A book cannot be deleted if it is currently borrowed by a member.

### Members

- **Attributes**: Each member has an ID, name, and membership date (automatically set at creation).
- **Borrowing Limits**: A member can borrow up to 10 books, configurable through an environment variable or property file.
- **Book Borrowing**: Borrowing a book decreases its amount by 1; if the amount reaches 0, the book cannot be borrowed.
- **Book Returning**: Returning a book increases its amount by 1.
- **Deletion Restriction**: A member cannot be deleted if they have borrowed books.

### Validation

- **Book**:
    - Name: Required, must start with a capital letter, minimum length of 3 characters.
    - Author: Required, must be two capital words separated by a space (e.g., Paulo Coelho).

- **Member**:
    - Name: Required.

### Additional Features

- Integration tests for the repository layer.  
- Unit tests for all services.
- Integration tests for the REST controllers.
- Integration of Swagger for API documentation.

## Setup and Installation

### Prerequisites

- **Java 21**: Ensure Java 21 is installed on your machine. You can download it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-downloads.html) or use a distribution like OpenJDK.
- **PostgreSQL**: Ensure PostgreSQL is installed and configured on your machine. You can download it from the [official PostgreSQL website](https://www.postgresql.org/download/).

### Cloning the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/your-username/your-project.git
```

Replace `your-username` and `your-project` with the appropriate GitHub username and project repository name.

### Building and Running the Project

1. Navigate to the project directory:

    ```bash
    cd your-project
    ```

2. Create the database, sequences, and tables using the SQL scripts in the database folder.

3. (Optional) Configure the database URL in the `application.yml` file.

4. Build the project using Gradle:

    ```bash
    ./gradlew build
    ```

5. Start the Spring Boot application:

    ```bash
    ./gradlew bootRun
    ```

   or, if using Docker:

    ```bash
    docker-compose up
    ```

6. The application will be accessible at [http://localhost:8080](http://localhost:8080) in your web browser.

### Running Unit Tests

To run unit tests, execute the following command:

```bash
./gradlew test
```

## API Endpoints

### Books

**Create Book**
- **HTTP Method:** POST
- **Endpoint:** `/books`
- **Consumes:** JSON (application/json)
- **Produces:** JSON (application/json)
- **Request Body:** A JSON object representing book details (`BookCreateRequest`)
- **Validation:** Validates the request body against the `BookCreateRequest` class using `@Valid`.
- **Response:** Returns a `BookResponse` representing the created book.
- **HTTP Status:** 201 Created

**Get All Books**
- **HTTP Method:** GET
- **Endpoint:** `/books`
- **Produces:** JSON (application/json)
- **Response:** Returns a list of `BookResponse` objects.
- **Parameters:** Pageable parameters for pagination.
- **HTTP Status:** 200 OK

**Get Book**
- **HTTP Method:** GET
- **Endpoint:** `/books/{id}`
- **Produces:** JSON (application/json)
- **Response:** Returns a `BookResponse` for the specified book ID.
- **HTTP Status:** 200 OK

**Update Book**
- **HTTP Method:** PATCH
- **Endpoint:** `/books/{id}`
- **Consumes:** JSON (application/json)
- **Produces:** JSON (application/json)
- **Request Body:** A JSON object representing book update details (`BookUpdateRequest`)
- **Validation:** Validates the request body against the `BookUpdateRequest` class using `@Valid`.
- **Response:** Returns a `BookResponse` representing the updated book.
- **HTTP Status:** 200 OK

**Delete Book**
- **HTTP Method:** DELETE
- **Endpoint:** `/books/{id}`
- **Response:** No content is returned.
- **HTTP Status:** 204 No Content

### Library

**Borrow Book**
- **HTTP Method:** POST
- **Endpoint:** `/library/borrowBook`
- **Consumes:** Form Data
- **Parameters:** `bookId` and `memberId` as required query parameters.
- **Validation:** Checks if the book is available for borrowing.
- **Response:** No content is returned if successful; an exception is thrown if the book is not available.
- **HTTP Status:** 200 OK

**Return Book**
- **HTTP Method:** POST
- **Endpoint:** `/library/returnBook`
- **Consumes:** Form Data
- **Parameters:** `bookId` and `memberId` as required query parameters.
- **Response:** No content is returned.
- **HTTP Status:** 200 OK

### Members

**Create Member**
- **HTTP Method:** POST
- **Endpoint:** `/members`
- **Consumes:** JSON (application/json)
- **Produces:** JSON (application/json)
- **Request Body:** A JSON object representing member details (`MemberCreateRequest`)
- **Validation:** Validates the request body against the `MemberCreateRequest` class using `@Valid`.
- **Response:** Returns a `MemberResponse` representing the created member.
- **HTTP Status:** 201 Created

**Get All Members**
- **HTTP Method:** GET
- **Endpoint:** `/members`
- **Produces:** JSON (application/json)
- **Response:** Returns a list of `MemberResponse` objects.
- **Parameters:** Pageable parameters for pagination.
- **HTTP Status:** 200 OK

**Get Member**
- **HTTP Method:** GET
- **Endpoint:** `/members/{id}`
- **Produces:** JSON (application/json)
- **Response:** Returns a `MemberResponse` for the specified member ID.
- **HTTP Status:** 200 OK

**Update Member**
- **HTTP Method:** PATCH
- **Endpoint:** `/members/{id}`
- **Consumes:** JSON (application/json)
- **Produces:** JSON (application/json)
- **Request Body:** A JSON object representing member update details (`MemberUpdateRequest`)
- **Validation:** Validates the request body against the `MemberUpdateRequest` class using `@Valid`.
- **Response:** Returns a `MemberResponse` representing the updated member.
- **HTTP Status:** 200 OK

**Delete Member**
- **HTTP Method:** DELETE
- **Endpoint:** `/members/{id}`
- **Response:** No content is returned.
- **HTTP Status:** 204 No Content

### Swagger

## Technologies Used

- Java 21
- Spring Boot
- Gradle
- Spring Data JPA
- Spring Web
- Spring Validation
- Swagger
- PostgreSQL
- Docker
- Testcontainers
- Spring Boot Hateoas
- Spring Boot Actuator
- Lombok
- MapStruct
- JUnit
- Mockito