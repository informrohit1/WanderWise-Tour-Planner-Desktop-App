# WanderWise Tour Planner (Desktop) — Database & Run Guide

This README lists the database tables the application expects in the `tms` MySQL database, recommended CREATE TABLE statements, notes about inconsistencies found in the code, where to put the MySQL Connector/J JAR, and commands to compile/run the app on Windows PowerShell.

## Summary of required tables

The Java sources in `tms/` reference the following tables:

- `account` — application user accounts (signup/login/forgot password)
- `customer` — personal/customer details entered in Add/Update/Delete flows
- `hotels` — hotel catalog used for booking
- `bookHotel` — hotel booking records
- `bookPackage` — package booking records

Notes:

- The application connects to a database named `tms` (see `tms/Conn.java`).
- The connection in `Conn.java` uses: `jdbc:mysql://localhost:3306/tms` with user `root` and password `rootOjas`. Adjust as needed.

## Recommended CREATE statements

Run these from your MySQL client (or adapt them) to create the minimal tables the app expects. These SQL definitions prioritize compatibility with the current Java code (column names and positional INSERTs).

-- create database

```sql
CREATE DATABASE IF NOT EXISTS tms;
USE tms;
```

-- account table (used by Signup/Login/ForgotPassword)

```sql
CREATE TABLE IF NOT EXISTS account (
  username VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100),
  password VARCHAR(255),
  question VARCHAR(255),
  answer VARCHAR(255)
);
```

-- customer table
-- IMPORTANT: code uses both `number` and `id_number` in different places. The code mostly reads `number`.
-- Suggested column set (use `number` to match most code; update AddCustomer insert if needed)

```sql
CREATE TABLE IF NOT EXISTS customer (
  username VARCHAR(50) PRIMARY KEY,
  id_type VARCHAR(50),
  number VARCHAR(50),        -- ID number (the code sometimes calls this id_number)
  name VARCHAR(100),
  gender VARCHAR(20),
  country VARCHAR(100),
  address VARCHAR(255),
  phone VARCHAR(30),
  email VARCHAR(100)
);
```

-- hotels table (catalog used by BookHotel)

```sql
CREATE TABLE IF NOT EXISTS hotels (
  name VARCHAR(100) PRIMARY KEY,
  location VARCHAR(100),
  cost_per_day INT,
  food_charges INT,
  ac_charges INT,
  availability VARCHAR(50),
  notes TEXT
);
```

-- bookPackage table
-- The Java code executes `INSERT INTO bookPackage VALUES (?, ?, ?, ?, ?, ?, ?)` and expects 7 columns.

```sql
CREATE TABLE IF NOT EXISTS bookPackage (
  username VARCHAR(50),
  package_name VARCHAR(100),
  persons INT,
  id_info VARCHAR(100),
  number VARCHAR(50),
  phone VARCHAR(30),
  price VARCHAR(50)
);
```

-- bookHotel table
-- The Java code executes `INSERT INTO bookHotel VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)` and expects 10 columns.

```sql
CREATE TABLE IF NOT EXISTS bookHotel (
  username VARCHAR(50),
  hotel_name VARCHAR(100),
  persons INT,
  days INT,
  ac VARCHAR(10),
  food VARCHAR(10),
  id_info VARCHAR(100),
  number VARCHAR(50),
  phone VARCHAR(30),
  cost VARCHAR(50)
);
```

## Notes about column-name inconsistencies found in the code

- `AddCustomer.java` inserts using column name `id_number`:
  `INSERT INTO customer (username, id_type, id_number, name, gender, country, address, phone, email) ...`

  but other classes (e.g. `UpdateCustomer`, `ViewCustomers`, `BookHotel`) access this column as `number` (e.g. `rs.getString("number")`).

  Recommendation: update `AddCustomer.java` to use `number` in its INSERT (or change all other code to use `id_number`). The README's suggested schema uses `number` for compatibility with most code paths.

- Price/cost handling: many places in the UI compute a string like `"Rs " + total` and save that into the database (e.g., `bookHotel`/`bookPackage`). If you want numeric queries later, prefer storing numeric values (INT/DECIMAL) and only format the currency in the UI.

## Example seed data

Add a couple of hotels and a test account so the UI can show data immediately:

```sql
USE tms;

INSERT INTO hotels (name, location, cost_per_day, food_charges, ac_charges, availability)
VALUES
  ('Blue Lake Hotel', 'Lakeside', 3000, 500, 700, 'Yes'),
  ('Sunrise Inn', 'City Centre', 2000, 300, 400, 'Yes');

INSERT INTO account (username, name, password, question, answer)
VALUES ('testuser', 'Test User', 'testpass', 'Your NickName?', 'buddy');
```

## Where to put the MySQL Connector/J (JAR)

1. Download the MySQL Connector/J JAR (for example: `mysql-connector-java-8.0.xx.jar`) from https://dev.mysql.com/downloads/connector/j/
2. Create a `lib` folder in the project root (if it doesn't already exist):

   c:\Programming\Javaproject\WanderWise-Tour-Planner-Desktop-App\lib

3. Put the connector JAR file inside `lib`.

## Compile & run (PowerShell)

From the project root (where this README is), run:

```powershell
# compile (include every jar in lib)
javac -cp ".;lib\*" tms\*.java

# run the Splash window (main class in package `tms`)
java -cp ".;lib\*" tms.Splash
```

Notes:

- Windows classpath separator is `;` (used above). If you use an IDE like Eclipse/IntelliJ/VS Code, add the connector JAR to the project's classpath / referenced libraries instead.
- If you still see `java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver` after adding the JAR, verify the jar is in `lib` and that you used `-cp ".;lib\*"` when running `java`.

## Quick troubleshooting checklist

- If you see ClassNotFoundException for the MySQL driver: make sure the connector JAR is present and included on the classpath (see above).
- If you see NullPointerException from `Conn` (connection `c` is null): check earlier printed exception in the console — it's usually the driver not found or bad JDBC URL/credentials.
- If customer fields appear blank or you get SQL errors, check for the `id_number` vs `number` inconsistency described above.

## Suggested small code improvement

In `tms/Conn.java` the constructor swallows exceptions and leaves `c` null. Consider printing the stack trace and/or rethrowing so initialization failures fail fast. Example:

```java
catch (Exception e) {
    e.printStackTrace();
    throw new RuntimeException("DB init failed", e);
}
```

If you want, I can update `AddCustomer.java` to use the `number` column (or update `Conn.java` to fail-fast) and recompile to verify — tell me which you'd prefer.

---

If you want, I can also generate SQL files (`schema.sql` and `seed.sql`) or apply a small code fix for the `id_number`/`number` mismatch. Which next step would you like?
