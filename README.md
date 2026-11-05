# Smart Parking Slot Booking API

A Spring Boot REST API for booking parking slots. Users can register, log in, view available slots, and book or cancel them. Admins can manage parking slots and view all bookings. Security is handled using JWT authentication and role-based authorization (USER / ADMIN), with protection against double booking.

## Tech Stack

- Java 25
- Spring Boot 4.1.1
- Spring Web (REST API)
- Spring Data JPA (Hibernate)
- MySQL
- Spring Security
- JWT Authentication (jjwt)
- BCrypt Password Hashing
- Bean Validation (Hibernate Validator)
- Swagger / OpenAPI (springdoc)
- Maven
- Lombok

## Features

### User

- Register and log in (receives a JWT token).
- View all parking slots.
- View available parking slots.
- View a parking slot by its number.
- Book an available parking slot.
- View own bookings.
- Cancel own bookings.

### Admin

- Add, update, block/unblock, and delete parking slots.
- View all bookings.
- View bookings by user or by parking slot.
- Cancel any booking.
- Manage registered users.

### Other Features

- A booking can only be viewed or cancelled by its owner or an admin.
- The user associated with a booking is taken from the JWT token, never from the request.
- Registered users always receive the USER role. Nobody can self-register as an admin.
- An admin account is created automatically on the first startup.
- Clear JSON error responses (400, 401, 403, 404, 409) through a global exception handler.
- Double booking is prevented using optimistic locking.

## Project Structure

```text
src/main/java/com/pradeep/smart_parking_api
│
├── Controller    -> REST endpoints
├── Service       -> Business logic (@Transactional booking and cancellation)
├── Repository    -> Spring Data JPA repositories
├── model         -> JPA entities: Users, ParkingSlot, Booking
├── dto           -> LoginRequest, LoginResponse, RegisterRequest
├── security      -> JwtUtil, JwtAuthFilter
├── config        -> SecurityConfig, OpenApiConfig, AdminSeeder
└── exception     -> Custom exceptions and GlobalExceptionHandler
```

**Request Flow:**

```text
Client -> JWT Filter -> Controller -> Service -> Repository -> MySQL
```

## How Double Booking Is Prevented

1. The service checks whether the parking slot is AVAILABLE inside a `@Transactional` method. If it is not available, the API returns `409 Conflict`.
2. The `ParkingSlot` entity uses a `@Version` field for optimistic locking.
3. If two requests attempt to book the same parking slot simultaneously, only one update succeeds. The other request receives a conflict response (`409`).

## Getting Started

### Prerequisites

- JDK 25 (or the version specified in `pom.xml`).
- Maven (or use the included `mvnw`).
- MySQL running on `localhost:3306`.

### 1. Create the Database

```sql
CREATE DATABASE springboot;
```

Tables are created automatically by Hibernate using `ddl-auto=update`.

### 2. Set Environment Variables

The database password is read from an environment variable and is not stored in the source code.

**Windows (Command Prompt):**

```cmd
setx DB_PASSWORD "your_mysql_password"
```

Restart your IDE or terminal after setting the environment variable.

**Linux / macOS:**

```bash
export DB_PASSWORD=your_mysql_password
```

**Optional:** Set `JWT_SECRET` to your own long secret key. A development default is used if it is not configured.

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

On Windows:

```cmd
mvnw.cmd spring-boot:run
```

You can also run `SmartParkingApiApplication` directly from Eclipse or Spring Tool Suite (STS).

The API starts at:

```text
http://localhost:7575
```

### 4. Open Swagger UI

Access the API documentation using:

[http://localhost:7575/swagger-ui/index.html](http://localhost:7575/swagger-ui/index.html)

## Default Admin (Local Use Only)

| Email | Password |
|---|---|
| admin@parking.com | Admin@123 |

The admin account is created automatically on the first startup.

This account is intended for local testing only. Change the credentials using the `admin.email` and `admin.password` properties, and configure your own `JWT_SECRET` before deploying the application.

## API Endpoints

### Authentication and Users

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/users` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Log in and receive a JWT token |
| GET | `/api/users/**` | Admin | Manage users |
| PUT | `/api/users/**` | Admin | Update user details |
| DELETE | `/api/users/**` | Admin | Delete users |

### Parking Slots

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/slots` | Logged in | Get all parking slots |
| GET | `/api/slots/available` | Logged in | Get available parking slots |
| GET | `/api/slots/{id}` | Logged in | Get a slot by ID |
| GET | `/api/slots/number/{slotNumber}` | Logged in | Get a slot by number |
| POST | `/api/slots` | Admin | Create a parking slot |
| PUT | `/api/slots/{id}` | Admin | Update a parking slot |
| PUT | `/api/slots/{id}/status?status=` | Admin | Change parking slot status |
| DELETE | `/api/slots/{id}` | Admin | Delete a parking slot |

### Bookings

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/bookings/create?slotId=` | Logged in | Book a parking slot |
| GET | `/api/bookings/my` | Logged in | Get own bookings |
| GET | `/api/bookings/{id}` | Owner or Admin | Get a booking |
| PUT | `/api/bookings/{id}/cancel` | Owner or Admin | Cancel a booking |
| GET | `/api/bookings` | Admin | Get all bookings |
| GET | `/api/bookings/user/{userId}` | Admin | Get bookings by user |
| GET | `/api/bookings/slot/{slotId}` | Admin | Get bookings by slot |

## Using the JWT Token

Send the JWT token received after login in the Authorization header for every protected request.

```http
Authorization: Bearer <your_token>
```

In Swagger UI:

1. Log in using the login endpoint.
2. Copy the JWT token from the response.
3. Click the **Authorize** button.
4. Paste your token and authorize the requests.

## Error Responses

| Status Code | Meaning |
|---|---|
| 400 | Validation failed (e.g., invalid email or short password) |
| 401 | Token missing or invalid, or incorrect login credentials |
| 403 | You do not have permission to perform this action |
| 404 | User, parking slot, or booking not found |
| 409 | Slot not available, duplicate email, or booking conflict |


## Future Improvements

- Use enums for parking slot and booking status instead of Strings.
- Add pagination for parking slots and bookings.
- Introduce booking start and end times.
- Deploy the application using Docker, Render, or Railway.
- Develop a React frontend to interact with the Spring Boot REST API.

## Author

**Gunupuru Pradeep Kumar**

- GitHub: [pradeepgunupuru17](https://github.com/pradeepgunupuru17)
- LinkedIn: [Pradeep Gunupuru](https://linkedin.com/in/pradeep-gunupuru-bb56b72ba)
