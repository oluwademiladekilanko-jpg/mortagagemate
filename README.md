# MortgageMate

A full-stack mortgage affordability calculator and application tracker —
built with a fintech/home-lending domain in mind (calculate a payment, check
it against an affordability guideline, submit and track an application
through a status pipeline).

**Stack:** Java 17, Spring Boot 3 (Web + Data JPA), H2 in-memory database,
vanilla HTML/CSS/JS frontend served as static files — no build step needed
on the frontend, no external database to install.

## What it does

- Enter income, home price, down payment, rate, and term → get a live
  monthly payment estimate using the standard fixed-rate amortization
  formula, plus a flag for whether it's within the common "28% of gross
  income" affordability guideline.
- Submit an application to persist it, then track it through a status
  pipeline: `SUBMITTED → UNDER_REVIEW → APPROVED/DENIED`.
- View, update, and remove applications from a table on the same page.

## Project layout

```
src/main/java/.../model/          MortgageApplication entity
src/main/java/.../repository/     Spring Data JPA repository
src/main/java/.../service/        MortgageCalculator (pure, unit-tested logic)
src/main/java/.../controller/     REST API (/api/calculate, /api/applications)
src/main/java/.../dto/            Request/response shapes
src/main/resources/static/        Frontend (index.html, style.css, app.js)
src/test/java/.../service/        JUnit tests for the calculator
```

## API

| Method | Path                          | Purpose                         |
|--------|-------------------------------|----------------------------------|
| POST   | `/api/calculate`               | Preview a payment, nothing saved |
| POST   | `/api/applications`             | Submit & persist an application  |
| GET    | `/api/applications`              | List all applications            |
| GET    | `/api/applications/{id}`         | Get one application               |
| PATCH  | `/api/applications/{id}/status`  | Update status (query param `status`) |
| DELETE | `/api/applications/{id}`         | Remove an application             |

## Running it (VS Code)

1. Install the **Extension Pack for Java** in VS Code (includes Maven support).
2. Open this folder in VS Code.
3. Run it either way:
   - Click `MortgageMateApplication.java` → *Run* above the `main` method, or
   - In the terminal: `./mvnw spring-boot:run` (or `mvn spring-boot:run` if
     you have Maven installed globally).
4. Visit **http://localhost:8080** — the calculator UI loads directly.
5. The H2 console (raw DB browser) is at **http://localhost:8080/h2-console**
   — JDBC URL `jdbc:h2:mem:mortgagemate`, user `sa`, blank password.

## Running the tests

```bash
mvn test
```

`MortgageCalculatorTest` checks the amortization math against a known
correct payment, a zero-interest edge case, a fully-paid-in-cash edge case,
and the affordability guideline.

## Possible next steps

- Swap H2 for Postgres/MySQL so data survives a restart.
- Add authentication so each user only sees their own applications.
- Add pagination once the applications list grows.
