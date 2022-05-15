# Bikes Anonymous

## Description

Bikes Anonymous is a practice project made in Spring Boot which implements solutions for cyclists.

## Table of Contents

## Installation

#### Requirements
- JDK 17
- Docker

#### Steps

1. Clone this project
2. `cd` into the new folder
3. `./mvnw clean install`
4. `docker run -p 27017:27017 --name ba-mongo -v /home/ba-mongo:/data/db -d mongo`

## Usage

To run the application for development, use `./mvnw spring-boot:run`

To access the other endpoints, you must first login in the endpoint `localhost:8080/login` or register at `localhost:8080/register`, using the following credentials:

#### USER
- Username: user@ba.com
- Password: pass

#### ADMIN
- Username: admin@ba.com
- Password: pass

You can see the exposed APIs at [localhost:8080/swagger-ui.html]()
