# EventHub: A Backend Spring Boot Application

![image](https://github.com/user-attachments/assets/c79dbc50-c660-41ea-8900-20f382837a66)

![](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-6A0DAD.svg?style=for-the-badge&logo=keycloak&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-008CDD?style=for-the-badge&logo=Stripe&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

Welcome to EventHub, an application for managing events, venues, offerings, and attendee registrations to have a successful journey.

## Features

- **Event Management**: Create, update, and cancel events with full lifecycle management
- **Venue & Resource Booking**: Book venues and resources with payment integration
- **Event Registration**: Attendee registration system with confirmation and cancellation
- **Dashboard Analytics**: Real-time metrics and statistics for administrators
- **Payment Processing**: Stripe integration for secure payments
- **Authentication**: JWT-based authentication via Keycloak
- **Email Notifications**: Automated email notifications for bookings and registrations

## Technology Stack

- **Framework**: Spring Boot 3.5.3 with Java 21
- **Database**: PostgreSQL with Spring Data JPA
- **Security**: OAuth2/JWT with Keycloak
- **Payments**: Stripe API integration
- **Email**: Spring Boot Mail Starter
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **API Docs**: Swagger/OpenAPI

## Prerequisites

- Docker and Docker Compose
- Java 21 (for local development)
- Maven 3.9+ (for local development)

## Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/zyadtaha/EventHub.git
   cd EventHub
   ```

2. **Set up environment variables**
   Create a `.env` file in the root directory:
   ```env
   EMAIL_USERNAME=your-email@gmail.com
   EMAIL_PASSWORD=your-app-password
   STRIPE_API_KEY=your-stripe-api-key
   ```
3. **Start the application**
   ```bash
   docker-compose up -d
   ```

## Services

The application runs three main services:

- **PostgreSQL Database** (`eventhub-db`): Port 5332 
- **Keycloak Authentication** (`eventhub-auth`): Port 9090
- **Spring Boot Application** (`eventhub-app`): Port 8080

## API Documentation

Once running, access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui/index.html
```
## Postman Collection

A ready-to-use Postman collection for testing authentication and API endpoints is available in the `postman/Keycloak users for testing.postman_collection.json` file.  
Import this collection into Postman to quickly get tokens for different user roles and test secured endpoints.

## Core Domain Models

<img width="788" height="811" alt="image" src="https://github.com/user-attachments/assets/e82f005f-d7be-406b-be9a-9e4ee27a065a" />

The system is built around five primary domain entities that represent the core business concepts:

1. **Event:** Central entity representing scheduled events with pricing and cancellation policies.

2. **Venue:** Represents bookable locations with capacity constraints and hourly pricing.

3. **Offering:** Services like catering and equipment that can be booked for events.

4. **Resource Booking:** Links events with their booked venues and offerings.

5. **Event Registration:** Tracks attendee registrations for events.

## User Roles

The system supports multiple user roles with different permissions:
- **Admin**: Full system access and dashboard statistics
- **Organizer**: Create and manage events, book resources (venues and offerings)
- **Venue Provider**: Manage venue bookings
- **Offering Provider**: Manage service offerings
- **Attendee**: Register for events and make payments

## Automated Features

- **Event Reminders**: Daily scheduled task sends email reminders to attendees for events happening the next day
- **Email Notifications**: Automatic emails for registration confirmations, updates, and cancellations
- **Dashboard Statistics**: Real-time metrics for system usage and event analytics

## Project Structure
```
src/
├── main/java/com/eventsystem/
│   ├── dto/           # Data Transfer Objects
│   ├── mapper/        # Entity-DTO mappers
│   ├── model/         # Domain entities
│   ├── repository/    # Data access layer
│   ├── service/       # Business logic
│   ├── controller/    # REST controllers
│   └── config/        # Configurations
└── main/resources/
    └── application.properties
```

## Future Improvements
- [x] Add Stripe integration for payment processing
- [ ] Implement search and filtering for events, venues and offerings
- [x] Add venue type mapping to an event type for better matching
- [ ] Add more statistics to the dashboard (e.g., revenue per organizer, user breakdown)
- [x] Document APIs with Swagger/OpenAPI
- [ ] Write unit and integration tests 
- [ ] Add frontend application using React

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a new branch for your feature or bug fix
3. Make your changes and commit them
4. Push to your forked repository
5. Create a pull request with a clear description of your changes
