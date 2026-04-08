# Client-Centric Software Project Planning System

## Overview

The Client-Centric Software Project Planning System is a web-based application developed using Spring Boot MVC architecture with MySQL as the backend database.

The system is designed to assist software teams in planning development projects based on structured client inputs. In real-world scenarios, clients often struggle to clearly define their requirements. This system addresses that gap by capturing client preferences and automatically generating a phase-wise project plan using rule-based logic.

The system evaluates client constraints, budget, timeline expectations, risk tolerance, and requirement clarity to generate a complete project plan along with risk analysis and alignment evaluation.


## Key Features

- User registration and role-based access (Client / Business Analyst)
- Client profile creation with structured inputs
- Automated project plan generation using rule-based logic
- Risk identification and classification
- Client alignment score calculation
- Decision analysis with conflict detection
- Versioned planning history tracking
  

## System Architecture

The application follows the Model-View-Controller (MVC) architecture:

### Model
- Entity classes mapped to database tables using JPA
- Repository interfaces for database operations
- Service layer containing business logic

### View
- Thymeleaf templates for dynamic UI rendering
- Organized into modules such as auth, client, plan, risk, alignment, and history
- Shared styling using CSS

### Controller
- Handles HTTP requests
- Connects frontend views with backend services
- Manages application flow


## Technologies Used

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot (Java 17) |
| Frontend | HTML, CSS, Thymeleaf |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Build Tool | Maven |


## Design Patterns Implemented

### Template Method Pattern
Used in `PlanningEngineService` to define a fixed workflow for project plan generation:
methodology → phases → timeline → execution → review frequency

### Strategy Pattern
Used in `ClientProfileService` to dynamically select execution strategies based on risk tolerance

### Factory Method Pattern
Used in `RiskService` to centralize the creation of Risk objects

### Singleton Pattern
Achieved using Spring's `@Service` annotation


## GRASP Principles Applied

| Principle | Class |
|-----------|-------|
| Controller | PlanningEngineService |
| Creator | ClientProfileService |
| Information Expert | UserService |
| Low Coupling | RiskService |


## Database Design

The system uses nine relational tables managed by Spring Data JPA:

| Table |
|-------|
| users |
| clients |
| business_analysts |
| client_profiles |
| preferences |
| project_plans |
| risks |
| alignment_evaluations |
| planning_records |

All tables are connected using foreign key relationships and support full CRUD operations.


## Application Workflow

1. User registers and logs in
2. Client profile is created with project constraints
3. System generates a project plan using a rule-based engine
4. Risks are automatically identified and flagged
5. Alignment score is calculated
6. Planning records are stored for tracking and history


## How to Run

### 1. Create Database

```sql
CREATE DATABASE client_planning_db;
```

### 2. Configure Database

Edit the file `src/main/resources/application.properties` and update credentials:

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Run Application

```bash
mvn spring-boot:run
```

### 4. Access Application

```
http://localhost:8080
```

## Project Structure

```
src/main/java/com/clientplanning/
├── controller/
├── service/
├── model/
├── repository/
├── enums/
└── resources/
    ├── templates/
    └── static/
```


## Team Members

| Name | SRN | Contributions |
|------|-----|---------------|
| Diya Bhat | PES2UG23CS183 | PlanningEngine, DecisionAnalysis, ProjectPlan, Basic setup |
| Dhanya Prabhu | PES2UG23CS169 | ClientProfile, Preference, Enums |
| Delisha Dsouza | PES2UG23CS166 | User, Client, BusinessAnalyst |
| Eshwar R A | PES2UG23CS188 | Risk, AlignmentEvaluator, PlanningRecord |


## Conclusion

This project demonstrates the application of structured software engineering principles, MVC architecture, and design patterns to build a scalable and maintainable system. It bridges the gap between ambiguous client requirements and structured project planning through rule-based decision making.
