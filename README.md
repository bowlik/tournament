# Tournament System — Semestrální práce BTWA3

Backendová REST API aplikace pro správu herních turnajů. Umožňuje registraci hráčů, vytváření turnajů a správu registrací.

## Technologie

- Java 17
- Spring Boot 3.3.5
- Spring Security + JWT
- Spring Data JPA
- H2 Database (in-memory)
- Swagger / OpenAPI 3.0
- Lombok
- JUnit 5

## Architektura

Aplikace je rozdělena do následujících vrstev:

- entity — databázové modely
- dto — objekty pro přenos dat
- repository — přístup k databázi
- service — business logika
- controller — REST endpointy
- config — konfigurace (Security, JWT, Swagger)
- exception — vlastní výjimky a globální handler

## Spuštění aplikace

Předpoklady: Java 17, Maven

    git clone https://github.com/bowlik/tournament.git
    cd tournament
    .\mvnw spring-boot:run

Aplikace běží na http://localhost:8080

## Dokumentace API

Po spuštění aplikace je dostupná na:

    http://localhost:8080/swagger-ui/index.html

## Použití API

### Registrace uživatele

    POST /api/auth/register
    Content-Type: application/json

    {
      "username": "hrac1",
      "password": "heslo123",
      "email": "hrac1@email.cz"
    }

### Přihlášení a získání JWT tokenu

    POST /api/auth/login
    Content-Type: application/json

    {
      "username": "hrac1",
      "password": "heslo123"
    }

Odpověď obsahuje JWT token který je nutné přiložit k dalším požadavkům v hlavičce:

    Authorization: Bearer <token>

## Uživatelské role

- PLAYER — výchozí role, může se registrovat do turnajů
- ADMIN — může vytvářet, upravovat a mazat turnaje

## Endpointy

| Metoda | Endpoint | Popis | Role |
|--------|----------|-------|------|
| POST | /api/auth/register | Registrace uživatele | Veřejný |
| POST | /api/auth/login | Přihlášení | Veřejný |
| GET | /api/tournaments | Seznam turnajů | Přihlášený |
| GET | /api/tournaments/{id} | Detail turnaje | Přihlášený |
| GET | /api/tournaments/available | Dostupné turnaje | Přihlášený |
| POST | /api/tournaments | Vytvoření turnaje | Admin |
| PUT | /api/tournaments/{id} | Úprava turnaje | Admin |
| DELETE | /api/tournaments/{id} | Smazání turnaje | Admin |
| POST | /api/registrations/{id} | Registrace do turnaje | Přihlášený |
| DELETE | /api/registrations/{id} | Odregistrování | Přihlášený |
| GET | /api/registrations/my | Moje turnaje | Přihlášený |
| GET | /api/users | Seznam uživatelů | Admin |
| DELETE | /api/users/{id} | Smazání uživatele | Admin |
| GET | /api/health | Stav aplikace | Veřejný |

## Testy

    .\mvnw test

Projekt obsahuje 10 testů pokrývajících UserService a TournamentService.

## H2 Konzole

Pro prohlížení databáze za běhu aplikace:

    http://localhost:8080/h2-console

- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (prázdné)
