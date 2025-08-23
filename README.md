# Progetto corso Basi di Dati
# Istruzioni sull'avvio dell'applicativo

## Requisiti:
- Java 21+
- MySQL 8.0+
- Scaricare i file `McDonald.sql` e `McDonald.jar` dal più recente [Release](https://github.com/Jackmo04/Progetto-BD/releases)

## Creazione del database MySQL in locale:
- Avviare lo script `McDonald.sql` su una connessione localhost di MySQL
- L'applicazione accede come utente root, senza necessitare di alcuna password

## Avvio dell'applicazione:
Se si ha Java per desktop installato:
- Fare doppio clic sul file `McDonald.jar`

Altrimenti, eseguire da terminale:
- Windows: `java -jar .\McDonald.jar`
- Linux/Mac: `java -jar ./McDonald.jar`

In caso di problemi, clonare il repository e ricompilare il progetto eseguendo da terminale:
- Windows: `.\gradlew.bat clean run`
- Linux/Mac: `./gradlew clean run`

## Utilizzo applicazione
Una volta avviata l'applicazione è necessario effettuare il login.
È data la possibilità di registrarsi, perciò non servono credenziali apposite;
tuttavia proponiamo alcuni utenti già registrati per testare l'applicazione in base a ruoli diversi:

#### Admin

| Email                  | Password |
| ---------------------- | -------- |
| admin@mcdonald.com     | admin    |

#### Staff

| Email                 | Password |
| --------------------- | -------- |
| staff@mcdonald.com    | staff    |

#### Client

| Email                 | Password |
| --------------------- | -------- |
| client@email.com      | client   |
| paolo.neri@email.com  | pneri    | ## utente bloccato
| mario.rossi@email.com | mrossi   |
