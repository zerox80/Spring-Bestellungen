# Bestellsystem (Spring Boot)

Eine einfache Beispiel-Anwendung für ein Bestellsystem mit Registrierung, Login und Bestellverwaltung auf Basis von Spring Boot und Thymeleaf.

## Features
- Benutzerregistrierung und Login mit Passwort-Hashing (BCrypt)
- Geschützte Bereiche nur für authentifizierte Nutzer
- Bestellungen anlegen und eigene Bestellungen einsehen
- Server-seitige Render-Views mit Thymeleaf
- H2 In-Memory-Datenbank (für Entwicklung und Tests)

## Tech-Stack
- Spring Boot 2.7.5
- Java 11
- Spring Web, Spring Data JPA, Spring Security
- Thymeleaf (+ thymeleaf-extras-springsecurity5)
- H2 Database (In-Memory)
- Maven

## Voraussetzungen
- Java 11 (JAVA_HOME gesetzt)
- Maven 3.6+

## Projekt starten (Entwicklung)
```bash
mvn spring-boot:run
```
Die Anwendung startet standardmäßig auf http://localhost:8080

## Wichtige URLs
- Startseite (öffentlich): `/`
- Registrierung (öffentlich): `/register`
- Login (öffentlich): `/login`
- Geschützte Startseite nach Login: `/home`
- Bestellungen (authentifiziert):
  - Liste: `/orders`
  - Neue Bestellung: `/orders/new`
  - Absenden (POST): `/orders`

## Benutzer und Rollen
- Standardrolle für neue Benutzer: `USER`
- Passwörter werden mit BCrypt gehasht

## Security-Konfiguration
Quelle: `src/main/java/com/example/bestellsystem/config/SecurityConfig.java`

- Öffentliche Endpunkte: `/`, `/register`, statische Ressourcen (`/css/**`, `/js/**`)
- Login-Seite: `/login`
- Erfolgs-Weiterleitung nach Login: `/home`
- Logout: POST auf `/logout` (Thymeleaf-Formular vorhanden)
- CSRF: Der Schutz ist standardmäßig aktiviert, wie es in Spring Security üblich ist. Die Formulare in den Thymeleaf-Templates sind korrekt konfiguriert und senden das CSRF-Token mit, was für die Sicherheit der Anwendung entscheidend ist.

Hinweis Thymeleaf Security Extras: In `home.html` wird das Namespace `xmlns:sec="http://www.thymeleaf.org/extras/spring-security5"` genutzt, um z. B. den eingeloggten Benutzernamen anzuzeigen.

## Datenmodell
- `User`: `id`, `username`, `password`, `roles`
- `Order`: Bestellung, gehört zu einem Benutzer (siehe `OrderController` und Repositories)

## Screens/Views (Thymeleaf)
- `index.html`: Öffentliche Startseite mit Links zu Registrierung und Login
- `register.html`: Formular zur Registrierung
- `login.html`: Login-Formular (zeigt Fehlermeldung bei Fehler, Logout-Hinweis)
- `home.html`: Geschützte Startseite nach Login (zeigt eingeloggten Benutzer, Logout-Formular)
- `orders.html`: Liste eigener Bestellungen
- `order-form.html`: Formular zum Anlegen einer neuen Bestellung

## Build
Erzeuge ein ausführbares Jar:
```bash
mvn clean package
```
Starten des Jars:
```bash
java -jar target/bestellsystem-1.0.0-SNAPSHOT.jar
```

## Tests
```bash
mvn clean test
```
Für Tests wird ein separates Profil mit H2 und Test-Properties verwendet (`src/main/resources/application-test.properties`).

## H2 Datenbank (optional)
- H2 Console kann über `/h2-console` erreicht werden, wenn aktiviert.
- Standard Test-Settings (siehe `application-test.properties`):
  - URL: `jdbc:h2:mem:testdb`
  - User: `sa`
  - Passwort: leer

## Troubleshooting
- Port 8080 belegt: Ändern Sie den Port per `server.port=<PORT>` in `application.properties` oder starten Sie den anderen Dienst neu.
- Login schlägt fehl: Prüfen Sie, ob der Benutzer korrekt registriert wurde. Passwörter sind gehasht, daher kann man sie nicht im Klartext in der DB setzen.
- CSRF-Fehler: Wenn Sie einen Fehler bezüglich eines fehlenden oder ungültigen CSRF-Tokens erhalten, stellen Sie sicher, dass Ihr Formular mit Thymeleaf (`th:action`) gerendert wird und ein `_csrf`-Token enthält. Dies sollte bei den vorhandenen Formularen bereits der Fall sein.

## Lizenz
Dieses Projekt ist ein Beispielprojekt zu Lernzwecken. Fügen Sie bei Bedarf Ihre Lizenzangaben hinzu.
