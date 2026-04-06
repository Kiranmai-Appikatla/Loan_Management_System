# SafeHaven

SafeHaven is a full-stack support portal for people facing domestic violence. It combines a React + Vite frontend with a Spring Boot + MySQL backend, role-based access control, resource management, and coordinated support workflows for survivors, counsellors, legal advisors, and administrators.

## Stack

- Frontend: React with Vite
- Backend: Spring Boot 3, Spring Security, JPA, JWT
- Database: MySQL
- Auth: JWT bearer tokens with role-based endpoint protection

## Project Structure

```text
backend/   Spring Boot API and database seed setup
frontend/  Vite React web application
```

## Roles

- `VICTIM_SURVIVOR`
- `COUNSELLOR`
- `LEGAL_ADVISOR`
- `ADMIN`

## Main Features

- Responsive SafeHaven portal with Home, Resources, Support Services, Contact, Login, and Registration flows
- Role-based UI for survivors, counsellors, legal advisors, and admins
- REST APIs for authentication, resources, support requests, legal advice, counselling notes, provider discovery, and admin overview
- Admin resource CRUD and user-role management
- MySQL-backed data model with seeded sample users, resources, and support requests

## Backend API

### Public

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/resources/public`
- `GET /api/users/providers`
- `POST /api/contact`

### Authenticated

- `GET /api/auth/me`
- `GET /api/resources`
- `GET /api/support-requests`
- `POST /api/support-requests`
- `PATCH /api/support-requests/{id}/status`
- `POST /api/support-requests/{id}/legal-advice`
- `POST /api/support-requests/{id}/counselling-notes`

### Admin

- `GET /api/admin/users`
- `PATCH /api/admin/users/{id}/role`
- `GET /api/admin/overview`

## Database Setup

1. Start MySQL.
2. Run the bootstrap SQL in [`backend/database/init.sql`](/Users/kiran/Documents/New project/backend/database/init.sql):

```sql
SOURCE backend/database/init.sql;
```

3. Configure environment variables if needed:

```bash
export SAFEHAVEN_DB_URL="jdbc:mysql://localhost:3306/safehaven_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export SAFEHAVEN_DB_USERNAME="root"
export SAFEHAVEN_DB_PASSWORD="root"
export SAFEHAVEN_JWT_SECRET="SafeHavenSecretKeySafeHavenSecretKey2026!"
```

Default datasource and JWT settings are defined in [`application.properties`](/Users/kiran/Documents/New project/backend/src/main/resources/application.properties).

## Sample Data

On first backend startup, SafeHaven seeds sample records automatically through [`DataSeeder.java`](/Users/kiran/Documents/New project/backend/src/main/java/com/safehaven/config/DataSeeder.java).

Demo accounts:

- `admin@safehaven.org` / `SafeHaven123`
- `survivor@safehaven.org` / `SafeHaven123`
- `counsellor@safehaven.org` / `SafeHaven123`
- `legal@safehaven.org` / `SafeHaven123`

## Run

### Backend

```bash
cd backend
mvn spring-boot:run
```

The API runs on `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The web app runs on `http://localhost:5173`.

## Production Build

Frontend build:

```bash
cd frontend
npm run build
```

## Key Files

- Backend app entry: [`SafeHavenApplication.java`](/Users/kiran/Documents/New project/backend/src/main/java/com/safehaven/SafeHavenApplication.java)
- Security config: [`SecurityConfig.java`](/Users/kiran/Documents/New project/backend/src/main/java/com/safehaven/config/SecurityConfig.java)
- Support workflow service: [`SupportRequestService.java`](/Users/kiran/Documents/New project/backend/src/main/java/com/safehaven/service/SupportRequestService.java)
- Frontend shell: [`App.jsx`](/Users/kiran/Documents/New project/frontend/src/App.jsx)
- Frontend API client: [`api.js`](/Users/kiran/Documents/New project/frontend/src/services/api.js)

## Verification Notes

- `npm run build` completed successfully for the frontend in this workspace.
- Backend compilation could not be executed here because the local environment does not currently have the `mvn` binary or a Maven wrapper checked into the repo.
