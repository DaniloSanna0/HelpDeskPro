HelpDeskPro è un sistema di gestione ticket per supporto tecnico e assistenza clienti. Permette agli utenti di creare, gestire e risolvere ticket in modo efficiente, garantendo una comunicazione fluida tra utenti e team di supporto.

Caratteristiche
Autenticazione JWT con ruoli USER e ADMIN
Gestione ticket con CRUD completo (Creazione, Lettura, Aggiornamento, Eliminazione)
Protezione API con Spring Security
Architettura RESTful per una facile integrazione con frontend o app mobile

Tecnologie Utilizzate
Back-end: Spring Boot, Spring Security, JPA (Hibernate)
Database: MySQL
Autenticazione: JWT (JSON Web Token)
Strumenti: Postman per testing API, Lombok per ridurre il boilerplate code

API Endpoints
Autenticazione
POST /api/auth/register → Registra un nuovo utente
POST /api/auth/login → Ottiene il token JWT
Gestione Ticket
GET /api/tickets → Lista tutti i ticket (richiede autenticazione)
POST /api/tickets → Crea un nuovo ticket
PUT /api/tickets/{id} → Aggiorna un ticket
DELETE /api/tickets/{id} → Elimina un ticket
