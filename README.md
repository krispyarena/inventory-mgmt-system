# Inventory Management System (IMS)

## Overview

The **Inventory Management System (IMS)** is a Spring Boot-based application designed to manage inventory records, including products, suppliers, and orders. The application allows users to manage their inventory through a secure REST API.

This project is built with Spring Boot, Spring Security, JWT authentication, Hibernate, MySQL, and Swagger for API documentation.

## Features

- **User Authentication & Authorization**: Secure login and role-based access control using JWT.
- **Product Management**: Manage products with CRUD operations (Create, Read, Update, Delete).
- **Supplier Management**: Add, update, and view supplier details.
- **Order Management**: Create and manage orders.
- **Swagger Documentation**: Auto-generated API documentation for easy testing and exploration.
- **Security**: Role-based access control to ensure only authorized users can access specific resources.

## Tech Stack

- **Backend**: Java, Spring Boot, Spring Security, JWT, Hibernate, JPA
- **Database**: MySQL
- **API Documentation**: Swagger 2
- **Build Tool**: Maven
- **Other Libraries**: Lombok, BCryptPasswordEncoder, H2 Database (for testing)

## Installation

Follow these steps to set up and run the Inventory Management System locally:

### 1. Clone the repository

```bash
git clone https://github.com/krispyarena/inventory-management-system.git
cd inventory-management-system
```
## 2. Configure Database
Ensure you have MySQL installed and create a database for the project (e.g., ivms).

```bash
CREATE DATABASE ivms;
```

In application.properties, update the database credentials:

```bash

spring.datasource.url=jdbc:mysql://localhost:3306/ivms
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## API Endpoints : 
[Click here for API documentation](https://docs.google.com/document/d/1sSzEAdXWvHfOnMsfdNdbddxw2ULgtucxD3SKmKWquYw/edit?usp=sharing)

## Authentication

The system uses JWT (JSON Web Token) for authentication and authorization. Upon successful login, a JWT token is generated and must be included in the Authorization header as Bearer <token> for accessing protected endpoints.


## Swagger UI

Swagger UI is available at http://localhost:8080/swagger-ui.html. Use it for easy testing of API endpoints and checking the API documentation.

## Security

### Roles:

- ADMIN: Full access to all resources.

- SELLER: Access to product management and order creation.

- USER: Access to viewing orders.

- JWT Authentication: All protected endpoints require a valid JWT token in the Authorization header.

### Contribution

Contributions are welcome! Please follow these steps to contribute to this project:

- Fork the repository.
- Create a new branch (git checkout -b feature-name).
- Commit your changes (git commit -am 'Add new feature').
- Push to the branch (git push origin feature-name).
- Open a pull request.

### License

This project is licensed under the MIT License - see the LICENSE file for details.

### Acknowledgments

Special thanks to Spring Boot, Spring Security, MySQL, and Swagger for making this project possible.
All contributions to the project are appreciated!

### Key Features:

- The `README.md` provides detailed instructions on setting up the project, running it locally, and interacting with the API.
- It includes information about each endpoint, authentication, and the role-based access control used in the system.
- The **Swagger UI** section is included to guide users to use the Swagger documentation effectively.
