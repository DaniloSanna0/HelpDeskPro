## 🔹 Caratteristiche principali
- **Autenticazione JWT**: Sistema di login con JSON Web Token e gestione dei ruoli (`USER`, `ADMIN`).
- **Gestione dei ticket**: CRUD completa sui ticket con associazione ai rispettivi utenti.
- **Ruoli e permessi**: Gli utenti possono gestire solo i propri ticket, mentre gli admin possono vedere e modificare tutti i ticket.
- **Database con MySQL**: Persistenza dei dati tramite JPA e Hibernate.
- **Spring Security**: Protezione delle API e autorizzazione basata sui ruoli.
- **RESTful API**: Endpoint ben strutturati per l'interazione con il frontend.

## 🎯 Tecnologie utilizzate
- **Java 23**
- **Spring Boot 3.4.3**
- **Spring Security**
- **Spring Data JPA**
- **JWT (Json Web Token)**
- **MySQL 8+**
- **Maven**

## 🔄 1. Autenticazione
- POST /auth/login → Login utente
- POST /auth/register → Registrazione nuovo utente

## 🎟️ 2. Ticket
- GET /api/tickets → Utenti normali: vedono solo i propri ticket. Admin: vede tutti
- POST /api/tickets → Creazione di un ticket
- PUT /api/tickets/{id}/status → Aggiornamento stato ticket (solo Admin)

## 👤 3. Utenti
- GET /api/users/me → Ottieni i dati dell'utente autenticato
