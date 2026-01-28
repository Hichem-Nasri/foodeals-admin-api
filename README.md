# Foodeals Admin API (Spring Boot)

## Quick start (local)
By default the app uses the `local` profile (`spring.profiles.default=local`) which runs on an in‑memory H2 database.

Run:

```bash
./mvnw spring-boot:run
```

## Run with Postgres
Set a non-local profile (any name is fine) and provide datasource env vars:

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/foodeals?currentSchema=foodeals"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="postgres"

./mvnw spring-boot:run
```

## JWT config
These can be set via environment variables:

- `APP_JWT_SECRET_KEY`
- `APP_JWT_EXPIRATION_MS`
- `APP_JWT_REFRESH_EXPIRATION_MS`

