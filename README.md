// TODO: update README
# EventHub: A Backend Spring Boot Application

![image](https://github.com/user-attachments/assets/c79dbc50-c660-41ea-8900-20f382837a66)


![](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-6A0DAD.svg?style=for-the-badge&logo=keycloak&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

Welcome to EventHub, an application for managing events, venues, offerings, and attendee registrations to have a successful journey.

## Features

- **Event Management**: Create, update, and manage events with full lifecycle support
- **Venue Management**: Venue providers can list and manage their venues for event bookings
- **Service Offerings**: Providers can offer catering, decoration, photography, and security services
- **Event Registration**: Attendees can register for events with automated email notifications
- **Role-Based Security**: Comprehensive authentication and authorization using OAuth2/JWT
- **Email Notifications**: Automated email system for registrations and event reminders

## Technology Stack

- **Framework**: Spring Boot 3.5.3 with Java 21
- **Database**: PostgreSQL with Spring Data JPA
- **Security**: OAuth2/JWT with Keycloak
- **Email**: Spring Boot Mail Starter
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven

## Prerequisites

- Java 21
- Docker and Docker Compose
- Maven

## Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/zyadtaha/EventHub.git
   cd EventHub
   ```

2. **Start infrastructure services**
   ```bash
   docker-compose up -d
   ```
   This starts:
    - PostgreSQL database on port 5332
    - Keycloak identity server on port 9090

3. **Create a `.env` file in the root directory** with the following content:
   ```env
   EMAIL_USERNAME=your_email
   EMAIL_PASSWORD=your_password
   ```
    Replace `your_email` and `your_password` with your actual email credentials for sending notifications.

4. **Build and run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`.

## Core Domain Models

![image](https://github.com/user-attachments/assets/5869eced-0b8a-4cca-a3f6-1d648ed7974f)

The system is built around five primary domain entities that represent the core business concepts:

1. **Event:** Central entity representing scheduled events with pricing and cancellation policies.

2. **Venue:** Represents bookable locations with capacity constraints and hourly pricing.

3. **Offering:** Services like catering and equipment that can be booked for events.

4. **Resource Booking:** Links events with their booked venues and offerings.

5. **Event Registration:** Tracks attendee registrations for events.

## API Endpoints

The system provides RESTful APIs for:
- Event management (`/events`)
- Venue management (`/venues`)
- Offering management (`/offerings`)
- Resource booking (`/resource-bookings`)
- Event registration (`/events-registrations`)

## User Roles

- ADMIN: System administrators
- ORGANIZER: Event organizers
- ATTENDEE: Event attendees
- VENUE_PROVIDER: Venue providers
- OFFERING_PROVIDER: Service providers

## Automated Features

- **Event Reminders**: Daily scheduled task sends email reminders to attendees for events happening the next day
- **Email Notifications**: Automatic emails for registration confirmations, updates, and cancellations

## Project Structure
```
src/
src/
├── main/java/com/eventsystem/
│   ├── dto/           # Data Transfer Objects
│   ├── mapper/        # Entity-DTO mappers
│   ├── model/         # Domain entities
│   ├── repository/    # Data access layer
│   ├── service/       # Business logic
│   ├── controller/    # REST controllers
│   ├── utils/         # Email notification utilities
│   └── config/        # Security and app configurations
└── main/resources/
    └── application.properties
```


## Future Improvements
- Add Stripe integration for payment processing
- Implement search and filtering for events, venues and offerings
- Add venue type mapping to an event type for better matching
- Document APIs with Swagger/OpenAPI
- Write unit and integration tests
- Add frontend application using React

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a new branch for your feature or bug fix
3. Make your changes and commit them
4. Push to your forked repository
5. Create a pull request with a clear description of your changes
