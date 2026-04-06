# Loaniverse Loan Management System

Loaniverse is a full-stack Loan Management System built with:

- Frontend: React + Vite
- Backend: Spring Boot + Maven
- Database: MySQL
- API style: REST with JSON

## Project Structure

```text
backend/   Spring Boot REST API
frontend/  React + Vite web app
```

## Backend Endpoints

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`

### Loans

- `POST /api/loans`
- `GET /api/loans`

### Loan Requests

- `POST /api/loan-requests`
- `GET /api/loan-requests/lender/{lenderId}`
- `PUT /api/loan-requests/{id}/approve`

### Payments

- `POST /api/payments`
- `GET /api/payments/{loanId}`

### Admin

- `GET /api/users`

## MySQL Setup

1. Start MySQL.
2. Create the database:

```sql
CREATE DATABASE loan_db;
```

3. Update the password in [`application.properties`](/Users/kiran/Documents/New project/backend/src/main/resources/application.properties) if your local MySQL password is different from `yourpassword`.

Current datasource configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/loan_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

## Run Locally

### Backend

```bash
cd "/Users/kiran/Documents/New project/backend"
mvn spring-boot:run
```

The backend runs on `http://localhost:8080`.

### Frontend

```bash
cd "/Users/kiran/Documents/New project/frontend"
npm install
npm run dev
```

The frontend runs on `http://localhost:5173`.

## Frontend API Base URL

The React app is configured to call:

```text
http://localhost:8080/api
```

This is defined in [`api.js`](/Users/kiran/Documents/New project/frontend/src/services/api.js).

## Example Request Payloads

### Register

```json
{
  "name": "John",
  "email": "john@example.com",
  "password": "123456",
  "role": "BORROWER"
}
```

### Login

```json
{
  "email": "john@example.com",
  "password": "123456"
}
```

### Create Loan

```json
{
  "amount": 100000,
  "interestRate": 8,
  "duration": 2,
  "lenderId": 1,
  "status": "AVAILABLE"
}
```

### Create Loan Request

```json
{
  "loanId": 1,
  "borrowerId": 2,
  "status": "PENDING"
}
```

### Create Payment

```json
{
  "loanId": 1,
  "amountPaid": 25000,
  "paymentDate": "2026-04-06"
}
```

## Business Logic Included

- Interest formula: `(principal * rate * time) / 100`
- Loan status flow: `AVAILABLE -> PENDING -> APPROVED -> COMPLETED`
- Duplicate loan requests are blocked
- Loan status updates automatically through the request and payment flow
- Logged-in user data is stored in `localStorage` on the frontend
