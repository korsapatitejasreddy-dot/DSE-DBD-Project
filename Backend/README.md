# MediConnect Backend

Java 17 + Spring Boot + MongoDB REST backend.

Run:
1. Start MongoDB.
2. Open this backend folder in VS Code.
3. Run `mvn spring-boot:run`.
4. API: http://localhost:8080

MongoDB database: `mediconnect`
Collections created automatically: `users`, `appointments`.

Endpoints:
POST /api/auth/register
POST /api/auth/login
POST /api/appointments
GET /api/appointments
GET /api/appointments/patient/{email}
DELETE /api/appointments/{id}
GET /api/health

For MongoDB Atlas, replace `spring.data.mongodb.uri` in application.properties.
