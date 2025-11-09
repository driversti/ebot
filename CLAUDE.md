# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Java Spring Boot automation bot** for eRepublik.com - an online strategy game. The bot automates authentication, session management, and data extraction from the game's web interface.

**Tech Stack:**
- Java 25
- Spring Boot 3.5.7
- Maven build system
- Apache HttpClient 5 for HTTP requests
- JSoup for HTML parsing
- Lombok for boilerplate reduction

## Essential Commands

```bash
# Build the project
./mvnw clean compile

# Run tests
./mvnw test

# Run the application
./mvnw spring-boot:run

# Package as JAR
./mvnw clean package

# Run a specific test
./mvnw test -Dtest=CampaignsListDTODeserializerTest
```

## Architecture & Key Components

### Authentication System (`/auth/`)
- **AuthService**: Handles eRepublik login with CSRF token extraction
- **SessionContext**: Manages authentication state and cookies
- **SessionKeepAliveService**: Maintains active sessions via scheduled keep-alive calls
- **StartupRunner**: Initiates authentication on application startup

### Configuration
- **application.yml**: Main configuration (port 8787, logging levels)
- **ErepublikProperties**: Configuration properties for eRepublik integration
- Environment variables required: `EBOT_EMAIL`, `EBOT_PASSWORD`

### Data Processing
- **DTOs** (`/dto/`): Data models for campaigns and battles
- **Deserializers** (`/deserializers/`): Custom JSON processing for game data
- **Models** (`/model/`): Game entities (Country enum, WarType)

### Error Handling
- Custom exceptions: `NotAuthorizedException`, `ServerException`, `TooManyRequestsException`
- Spring Retry configured for automatic retry on failures

## Current Status

✅ **All compilation issues have been resolved**. The codebase now builds and tests successfully.

## Project Structure

```
src/main/java/live/yurii/ebot/
├── auth/                   # Authentication and session management
│   ├── AuthService.java           # Main authentication logic with CSRF handling
│   ├── SessionContext.java        # Session state management (csrfToken, cookies)
│   ├── SessionKeepAliveService.java # Scheduled keep-alive calls
│   └── StartupRunner.java         # Initial authentication on startup
├── config/                 # Configuration classes
│   ├── ErepublikProperties.java   # Configuration properties with @Data lombok
│   └── RestClientConfig.java      # HTTP client configuration
├── deserializer/          # JSON/data deserializers
│   ├── AbstractDeserializer.java  # Base deserializer class
│   └── CampaignsListDTODeserializer.java # Campaign data processing
├── dto/                   # Data Transfer Objects
│   ├── BattleDTO.java     # Battle data model
│   └── CampaignsListDTO.java # Campaign list wrapper
├── exception/             # Custom exceptions
│   ├── NotAuthorizedException.java
│   ├── ServerException.java
│   └── TooManyRequestsException.java
├── model/                 # Game entities
│   ├── Country.java       # Country enum with id, name, currency, emoji
│   └── WarType.java       # War type enumeration
├── utils/                 # Utility classes
│   └── DeserializerUtil.java # JSON parsing utilities (toInstant, toStream)
└── EbotApplication.java   # Main Spring Boot application class
```

## Development Notes

- The application runs on port 8787
- Uses Spring Boot DevTools for hot reload during development
- Authentication is triggered automatically on startup
- Keep-alive calls run every 5 minutes to maintain session
- All eRepublik interactions use a configurable User-Agent string
- The bot scrapes HTML content from eRepublik.com and processes JSON data from their API endpoints

## Authentication Flow

1. **Startup**: `StartupRunner` triggers authentication on application start
2. **CSRF Extraction**: `AuthService` extracts CSRF token from login page HTML
3. **Login**: POST request with credentials and CSRF token to eRepublik login endpoint
4. **Session Storage**: Cookies and CSRF token stored in `SessionContext`
5. **Keep-Alive**: Scheduled service maintains session every 5 minutes
6. **Error Handling**: Automatic retry (3 attempts) on authentication failures


## Development rules

- **Keep it simple**: Keep the code simple and readable
- **Keep it DRY**: Don't repeat yourself
- **Keep it modular**: Break down complex logic into smaller, reusable components
- **Keep it testable**: Write tests for all components
- **Keep it secure**: Use secure HTTP requests and JSON parsing
- **Keep it performant**: Optimize for performance and scalability
- **Keep it maintainable**: Keep the codebase clean and maintainable
- Adhere to SOLID principles
- Use modern Java 25 features
- Follow [.editorconfig](.editorconfig) rules
- Use [Lombok](https://projectlombok.org/) for boilerplate reduction
- Use [JUnit 5](https://junit.org/junit5/) for testing
- Use [Mockito](https://site.mockito.org/) for mocking
