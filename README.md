# File Nest
# Telegram Bot: File Sharing with Asynchronous Message Processing

## Bot Workflow

1. Start the bot
2. User registration in the application (entering email)
3. Complete registration by following the confirmation link sent to email
4. Upload content: send files to the Telegram bot chat
5. Download content: receive an external link to download the file

## Application Architecture

![Application Architecture](https://raw.githubusercontent.com/zolotyh-dk/file-nest/f0b788e328a2aac52ca124e29e5fb5b07d736e34/application-architecture.svg)

---

## Database Setup (PostgreSQL in Docker)

The application uses **PostgreSQL** running inside a Docker container.

### Port Configuration

By default, PostgreSQL runs on port `5432`.  
If you already have a local PostgreSQL installation, this may cause a port conflict.
If you want to keep port `5432` on your host, make sure to stop your local PostgreSQL service before starting the container.
Alternatively, use a different host port (e.g., `5433:5432`) as shown below:

```yaml
services:
  postgres:
    image: postgres:16.1
    container_name: postgres
    environment:
      - POSTGRES_DB=messages
      - POSTGRES_USER=filenest
      - POSTGRES_PASSWORD=filenest
    ports:
      - "5433:5432" # host:container
```

5433 → port on the host machine   
5432 → internal container port

### Connecting to PostgreSQL

From your host machine:

```bash
psql "host=localhost port=5433 dbname=messages user=filenest password=filenest"
```

From another container in the same Docker network:

```bash
psql "host=postgres port=5432 dbname=messages user=filenest password=filenest"
```


