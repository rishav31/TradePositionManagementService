# Backend API Endpoints

## Position Management API (Spring Boot)

### 1. Get All Positions
- **Endpoint:** `GET /positions`
- **Description:** Returns a list of all positions (securityCode, netQuantity).
- **Response Example:**
```
[
  {
    "securityCode": "REL",
    "netQuantity": 100
  },
  ...
]
```

### 2. Process Transaction
- **Endpoint:** `POST /transactions`
- **Description:** Processes a new transaction (BUY/SELL/CANCEL/UPDATE) and updates positions accordingly.
- **Request Body Example:**
```
{
  "tradeId": 1,
  "version": 1,
  "securityCode": "REL",
  "quantity": 50,
  "action": "BUY"
}
```
- **Response:** `200 OK` on success, validation or error message on failure.

### 3. Get Latest Version for Trade
- **Endpoint:** `GET /transactions/latest-version?tradeId={tradeId}`
- **Description:** Returns the latest version number for a given trade ID. Returns 0 if not found.
- **Response Example:**
```
2
```

### 4. Health and Monitoring
- **Endpoint:** `GET /actuator/health`
- **Description:** Health check endpoint (enabled by Spring Boot Actuator).
- **Endpoint:** `GET /actuator/info`, `GET /actuator/metrics`
- **Description:** Application info and metrics (enabled by Spring Boot Actuator).

### 5. H2 Database Console
- **Endpoint:** `/h2-console`
- **Description:** Web UI for in-memory H2 database (enabled in dev mode).

---

## Notes
- All endpoints support CORS (`@CrossOrigin(origins = "*")`).
- Validation and error handling are implemented with custom exceptions and global handler.
- See `application.properties` for DB and actuator configuration.
