# Aplazo BNPL API

## 📌 Overview
Aplazo BNPL API is a RESTful service that enables customer registration and purchase management through a Buy Now, Pay Later (BNPL) system. The API provides endpoints to:
- **Register customers**
- **Retrieve customer details**
- **Register purchases**
- **Retrieve purchase details**

The API follows **OpenAPI 3.0** standards and is secured using **JWT-based authentication**.

---

## 📦 Prerequisites
- **Java 17**
- **Maven**
- **Docker & Docker Compose**

---

## 🚀 Running the Application

### 1️⃣ **Run with Docker Compose**
To spin up the application along with a PostgreSQL database, run:
```bash
docker-compose up --build
```
This will start:
- PostgreSQL database (`bnpl`) on **port 5432**
- BNPL API on **port 8080**

To stop the services, use:
```bash
docker-compose down
```

### 2️⃣ **Run Locally (Without Docker)**
Ensure PostgreSQL is running with the following credentials:
```plaintext
DB: bnpl
Username: bnpl_user
Password: password
```
Then, build and run the project:
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🔑 Authentication
### **Obtain JWT Token**
Before making requests to protected endpoints, obtain a JWT token by logging in:
```bash
curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "password"}'
```
Response:
```json
{
  "message": "Login successful",
  "data": {
    "token": "your.jwt.token.here"
  }
}
```
Use this token for authorization in subsequent requests.

---

## 📌 API Endpoints & Usage
### **1️⃣ Register a Customer**
```bash
curl -X POST http://localhost:8080/api/clients/register \
     -H "Content-Type: application/json" \
     -d '{
          "name": "John Doe",
          "birthDate": "1990-05-15"
         }'
```
Response:
```json
{
  "id": 1,
  "name": "John Doe",
  "birthDate": "1990-05-15",
  "creditLine": 5000
}
```

---

### **2️⃣ Retrieve Customer Details**
```bash
curl -X GET http://localhost:8080/api/clients/{clientId} \
     -H "Authorization: Bearer your.jwt.token.here"
```

---

### **3️⃣ Register a Purchase**
```bash
curl -X POST http://localhost:8080/api/purchases/register \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer your.jwt.token.here" \
     -d '{
          "clientId": 1,
          "amount": 3000.00
         }'
```
Response:
```json
{
  "id": 1,
  "clientId": 1,
  "purchaseAmount": 3000.00,
  "commissionAmount": 390.00,
  "totalAmount": 3390.00,
  "numberOfPayments": 5,
  "purchaseDate": "2025-02-28"
}
```

---

### **4️⃣ Retrieve Purchase Details**
```bash
curl -X GET http://localhost:8080/api/purchases/{purchaseId} \
     -H "Authorization: Bearer your.jwt.token.here"
```

---

## 📚 OpenAPI Documentation
To access API documentation, navigate to:
```plaintext
http://localhost:8080/swagger-ui.html
```

---

## ✅ Test Requirements Fulfilled
This project meets the following technical test requirements:
- ✅ Developed using **Java 17** and **Spring Boot 3**.
- ✅ Implements a **REST API** for customer and purchase management.
- ✅ Uses **JWT-based authentication** for security.
- ✅ Stores data in **PostgreSQL** using **Spring Data JPA**.
- ✅ Implements **business rules** for credit line assignment.
- ✅ Implements **business rules** for purchase eligibility and payment schemes.
- ✅ Provides **logging** for debugging and monitoring.
- ✅ Supports **Docker and Docker Compose** for deployment.
- ✅ Includes **unit tests** for core business logic.
- ✅ Provides **OpenAPI documentation** for API exploration.

---

## 🛠️ Troubleshooting
### **Common HTTP Errors & Solutions**
#### **🔗 HTTP 403 - Forbidden**
**Cause:**
- Missing or invalid JWT token.
- Attempting to access a protected resource without authentication.

**Solution:**
- Ensure you have obtained a valid JWT token.
- Include the token in the `Authorization` header of your request.

#### **🔗 HTTP 400 - Bad Request**
**Cause:**
- Invalid input data (e.g., missing required fields, incorrect format).

**Solution:**
- Verify that all required fields are included and correctly formatted in the request.

#### **🔗 HTTP 404 - Not Found**
**Cause:**
- The requested resource (client or purchase) does not exist.

**Solution:**
- Check that the provided `clientId` or `purchaseId` is correct and exists in the database.

#### **🔗 HTTP 500 - Internal Server Error**
**Cause:**
- Unexpected errors, such as database connectivity issues.

**Solution:**
- Ensure the database is running and accessible.
- Check server logs for more details.

#### **Database Issues**
**Issue:** `relation "clients" does not exist`
- Ensure the database tables have been created (`clients`, `purchases`).
- Run migrations if necessary.

Thanks and Regards Cesar!! 