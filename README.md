# Prodavnica Računara - Computer Store Management System

RESTful web servis za upravljanje prodavnicom računara kreiran koristeći Spring Boot 3.0 i JDK 17.

## Funkcionalnosti

### 1. Upravljanje proizvodima
- CRUD operacije za računare i dodatnu opremu
- Praćenje specifikacija (CPU, RAM, GPU)
- Upravljanje cenom i količinom na stanju
- Pretraga proizvoda po različitim kriterijumima

### 2. Upravljanje kupcima
- Registracija novih kupaca
- Ažuriranje podataka kupaca
- Pretraga kupaca po imenu, prezimenu, email-u
- Validacija jedinstvenih podataka (email, telefon)

### 3. Narudžbine
- Kreiranje narudžbi sa više proizvoda
- Praćenje statusa narudžbe (U obradi, Poslata, Isporučena, Otkazana)
- Automatsko upravljanje stanjem proizvoda
- Monitoring i automatsko ažuriranje statusa narudžbi

### 4. Plaćanja
- Evidencija plaćanja za narudžbe
- Podrška za različite načine plaćanja (Kartica, PayPal, Gotovina)
- Praćenje statusa plaćanja
- Statistike prihoda i plaćanja

## Tehnički stack

- **Framework**: Spring Boot 3.0.12
- **Java**: JDK 17
- **Baza podataka**: MySQL 8.0
- **Build tool**: Maven
- **API dokumentacija**: OpenAPI 3.0 / Swagger UI
- **Logovanje**: SLF4J + Logback

## Arhitektura

Projekat koristi višeslojnu arhitekturu:

```
├── Controller Layer    # REST endpointi
├── Service Layer       # Business logika
├── Repository Layer    # Data Access Object (JPA)
└── Entity Layer        # JPA entiteti
```

## Pokretanje aplikacije

### Preduslovi
- JDK 17
- Maven 3.6+
- MySQL 8.0

### Konfiguracija baze podataka

1. Kreirati MySQL bazu podataka:
```sql
CREATE DATABASE prodavnicaracunara;
```

2. Ažurirati `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prodavnicaracunara
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Pokretanje

```bash
# Build aplikacije
mvn clean package

# Pokretanje
java -jar target/prodavnicaracunara-1.0.0.jar

# Ili direktno sa Maven-om
mvn spring-boot:run
```

Aplikacija će biti dostupna na: `http://localhost:8080/api`

## API dokumentacija

Swagger UI je dostupan na: `http://localhost:8080/api/swagger-ui.html`

OpenAPI specifikacija: `http://localhost:8080/api/api-docs`

## API Endpointi

### Proizvodi
- `GET /api/proizvodi` - Lista svih proizvoda
- `POST /api/proizvodi` - Kreiranje novog proizvoda
- `GET /api/proizvodi/{id}` - Dohvatanje proizvoda po ID
- `PUT /api/proizvodi/{id}` - Ažuriranje proizvoda
- `DELETE /api/proizvodi/{id}` - Brisanje proizvoda
- `GET /api/proizvodi/search?naziv={naziv}` - Pretraga po nazivu
- `GET /api/proizvodi/in-stock` - Proizvodi na stanju
- `PATCH /api/proizvodi/{id}/stock?novaKolicina={kolicina}` - Ažuriranje stanja

### Kupci
- `GET /api/kupci` - Lista svih kupaca
- `POST /api/kupci` - Registracija novog kupca
- `GET /api/kupci/{id}` - Dohvatanje kupca po ID
- `PUT /api/kupci/{id}` - Ažuriranje kupca
- `DELETE /api/kupci/{id}` - Brisanje kupca
- `GET /api/kupci/email/{email}` - Dohvatanje kupca po email-u

### Narudžbe
- `GET /api/narudzbe` - Lista svih narudžbi
- `POST /api/narudzbe` - Kreiranje nove narudžbe
- `GET /api/narudzbe/{id}` - Dohvatanje narudžbe po ID
- `GET /api/narudzbe/broj/{brojNarudzbe}` - Dohvatanje po broju narudžbe
- `GET /api/narudzbe/kupac/{kupacId}` - Narudžbe određenog kupca
- `PATCH /api/narudzbe/{id}/status?noviStatus={status}` - Ažuriranje statusa
- `PATCH /api/narudzbe/{id}/cancel` - Otkazivanje narudžbe

### Plaćanja
- `GET /api/placanja` - Lista svih plaćanja
- `POST /api/placanja` - Kreiranje novog plaćanja
- `GET /api/placanja/{id}` - Dohvatanje plaćanja po ID
- `GET /api/placanja/narudzba/{narudzbaId}` - Plaćanje za narudžbu
- `PATCH /api/placanja/{id}/process` - Obrađivanje plaćanja
- `GET /api/placanja/revenue?startDate={date}&endDate={date}` - Statistike prihoda

## Dodatne funkcionalnosti

### Background task
Aplikacija pokreće background task koji svakih 5 sekundi proverava i ažurira status narudžbi:
- Narudžbe u statusu "U obradi" se automatski prebacuju u "Poslata" nakon 5 minuta
- Narudžbe u statusu "Poslata" se automatski prebacuju u "Isporučena" nakon 10 minuta

### Logovanje
Aplikacija generiše detaljne logove svih aktivnosti, dostupne u konzoli i log fajlovima.

### Exception handling
Implementiran je globalni exception handler koji vraća strukturisane error response-e.

## Primer korišćenja

### Kreiranje proizvoda
```bash
curl -X POST http://localhost:8080/api/proizvodi \
  -H "Content-Type: application/json" \
  -d '{
    "naziv": "Gaming Laptop",
    "cpu": "Intel i7-12700H", 
    "ram": "16GB DDR4",
    "gpu": "NVIDIA RTX 3060",
    "cena": 159990.00,
    "kolicinaUStanju": 5
  }'
```

### Kreiranje kupca
```bash
curl -X POST http://localhost:8080/api/kupci \
  -H "Content-Type: application/json" \
  -d '{
    "ime": "Petar",
    "prezime": "Petrović", 
    "email": "petar@email.com",
    "telefon": "+381601234567",
    "adresa": "Beograd, Srbija"
  }'
```

### Kreiranje narudžbe
```bash
curl -X POST http://localhost:8080/api/narudzbe \
  -H "Content-Type: application/json" \
  -d '{
    "kupacId": 1,
    "proizvodIds": [1, 2],
    "ukupnaCena": 199990.00
  }'
```

## Struktura projekta

```
src/main/java/com/example/prodavnicaracunara/
├── controller/          # REST kontroleri
├── service/            # Business logika
├── repository/         # JPA repozitorijumi
├── entity/             # JPA entiteti
├── dto/                # Data Transfer Objects  
├── config/             # Konfiguracija
├── exception/          # Exception handling
└── task/               # Background tasks
```

## Razvoj

Za razvoj aplikacije koristiti:
- IDE sa podrškom za Spring Boot (IntelliJ IDEA, Eclipse STS)
- MySQL Workbench za upravljanje bazom podataka
- Postman ili curl za testiranje API-ja
- Git za version control

## Licenca

MIT License
