<div align="center">

[![Generic badge](https://img.shields.io/badge/STATUS-FINISHED-<COLOR>.svg)](https://shields.io/) [![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/Eduardo-Salvador)

# Products API: RESTful Java Spring Boot

> RESTful API for product management, built with Java 21 and Spring Boot 3.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-brightgreen?style=flat-square&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker)
![Maven](https://img.shields.io/badge/Maven-build-C71A36?style=flat-square&logo=apachemaven)

</div>

---

## About

RESTful API developed for study purposes, focused on back-end development best practices. The project includes a complete product CRUD with dynamic filtering, pagination, HATEOAS hypermedia, global exception handling, and coverage with unit and integration tests.

---

## Features

- Complete product CRUD (Create, Read, Update, Delete)
- Paginated listing with configurable sorting
- Dynamic filters by name, minimum price, and maximum price via JPA Specifications
- HATEOAS with hypermedia links in responses
- Input validation with Bean Validation (`@NotBlank`, `@NotNull`)
- Global exception handling with `@RestControllerAdvice`
- Entity mapping with MapStruct
- API documentation with Swagger UI and OpenAPI 3
- Unit tests with JUnit 5 + Mockito
- Integration tests with H2 in-memory database
- Containerization with Docker + PostgreSQL

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Main language |
| Spring Boot | 3.4.5 | Base framework |
| Spring Data JPA | - | Persistence and Specifications |
| Spring HATEOAS | - | Hypermedia in responses |
| Spring Validation | - | DTO validation |
| SpringDoc OpenAPI | 2.8.8 | Swagger UI and API documentation |
| PostgreSQL | - | Production database |
| H2 Database | - | Test database |
| MapStruct | 1.6.3 | Object mapping |
| Lombok | - | Boilerplate reduction |
| JUnit 5 | - | Unit and integration tests |
| Mockito | - | Mocks in unit tests |
| Docker | - | Containerization |
| Maven | - | Dependency management |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/eduardo_salvador/api_restful_java_springboot/
│   │   ├── config/            # OpenAPI and Swagger configuration
│   │   ├── controllers/       # REST endpoints
│   │   ├── dtos/              # Data Transfer Objects (record + response)
│   │   ├── exceptions/        # Custom exceptions
│   │   ├── infra/             # GlobalExceptionHandler + RestErrorMessage
│   │   ├── mappers/           # MapStruct interfaces
│   │   ├── models/            # JPA entity (ProductModel)
│   │   ├── repositories/      # Spring Data JPA + JpaSpecificationExecutor
│   │   ├── services/          # Service interface + implementation
│   │   └── specifications/    # Dynamic filters (JPA Specifications)
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/eduardo_salvador/api_restful_java_springboot/
        ├── ProductServiceImplTest.java        # Unit tests
        └── ProductServiceIntegrationTest.java # Integration tests
Dockerfile
docker-compose.yml
.env.example
```

---

## Getting Started

### Prerequisites

- [Docker + Docker Compose](https://www.docker.com/)

### 1. Clone the repository

```bash
git clone https://github.com/your-username/api-restful-java-springboot.git
cd api-restful-java-springboot
```

### 2. Configure environment variables

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

`.env.example`:

```env
POSTGRES_DB=products-api
POSTGRES_USER=postgres
POSTGRES_PASSWORD=
```

### 3. Run everything with Docker Compose

```bash
docker-compose up -d
```

This single command builds the application image, starts the PostgreSQL database, and runs the API. No local Java or Maven installation required.

The API will be available at `http://localhost:8080`.

The Swagger UI will be available at `http://localhost:8080/swagger-ui.html`.

---

### Useful Docker commands

```bash
# Follow logs in real time
docker-compose logs -f

# Stop all containers without removing volumes
docker-compose down

# Stop all containers and remove volumes (resets the database)
docker-compose down -v

# Rebuild the application image after code changes
docker-compose up -d --build
```

---

### Running locally without Docker

If you prefer to run the application directly:

**Prerequisites:** Java 21+, Maven 3.9+, PostgreSQL running on port 5432.

Configure `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/products-api
spring.datasource.username=postgres
spring.datasource.password=postgres123
```

Then run:

```bash
./mvnw spring-boot:run
```

---

## API Reference

Base URL: `http://localhost:8080`

| Method | Route | Description |
|---|---|---|
| `POST` | `/products` | Create a product |
| `GET` | `/products` | List products (paginated + filters) |
| `GET` | `/products/{id}` | Get product by ID |
| `PUT` | `/products/{id}` | Update a product |
| `DELETE` | `/products/{id}` | Delete a product |

---

### POST /products

**Request body:**

```json
{
  "name": "Notebook",
  "price": 3000.00
}
```

**Response 201 Created:**

```json
{
  "idProduct": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "name": "Notebook",
  "price": 3000.00,
  "_links": {}
}
```

---

### GET /products

**Optional query parameters:**

| Parameter | Type | Description |
|---|---|---|
| `name` | `String` | Partial name filter (case-insensitive) |
| `minPrice` | `BigDecimal` | Minimum price |
| `maxPrice` | `BigDecimal` | Maximum price |
| `page` | `int` | Page number (default: 0) |
| `size` | `int` | Items per page (default: 10) |
| `sort` | `String` | Sort field (default: `name`) |

**Example:**

```
GET /products?name=note&minPrice=1000&maxPrice=5000&page=0&size=5
```

**Response 200 OK:**

```json
{
  "content": [
    {
      "idProduct": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "name": "Notebook",
      "price": 3000.00,
      "_links": {
        "self": { "href": "http://localhost:8080/products/f47ac10b-..." }
      }
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

---

### GET /products/{id}

**Response 200 OK:**

```json
{
  "idProduct": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "name": "Notebook",
  "price": 3000.00,
  "_links": {
    "Products List": { "href": "http://localhost:8080/products" }
  }
}
```

---

### PUT /products/{id}

**Request body:**

```json
{
  "name": "Notebook Pro",
  "price": 4500.00
}
```

**Response 200 OK:** updated product object.

---

### DELETE /products/{id}

**Response 200 OK:**

```json
"Product deleted successfully."
```

---

## Error Responses

| Status | Situation |
|---|---|
| `400 Bad Request` | Invalid fields, malformed JSON, or wrong ID format |
| `404 Not Found` | Product not found or route does not exist |
| `500 Internal Server Error` | Unexpected server error |

**Validation error 400:**

```json
{
  "error": "Validation failed",
  "name": "must not be blank",
  "price": "must not be null"
}
```

**Not found error 404:**

```json
{
  "status": "NOT_FOUND",
  "message": "Product not found."
}
```

---

## Tests

The project has two levels of test coverage.

**Unit tests** (`ProductServiceImplTest`) use JUnit 5 + Mockito, isolating the service layer with mocks for the repository and mapper. Scenarios covered: `save`, `findAll`, `findById`, `update`, and `delete`, including both success and failure cases (`NoFindException`).

**Integration tests** (`ProductServiceIntegrationTest`) use `@SpringBootTest` with an H2 in-memory database, testing the complete flow including real persistence. Additional scenarios include filtering by name and filtering by price range.

### Run the tests

```bash
./mvnw test
```

---

## Data Model

**Table:** `TB_PRODUCTS`

| Column | Type | Description |
|---|---|---|
| `idProduct` | `UUID` | Primary key (auto-generated) |
| `name` | `VARCHAR` | Product name |
| `price` | `NUMERIC` | Product price |

---

## Author

**Eduardo Salvador** - developed for study and learning purposes.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.