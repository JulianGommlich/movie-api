# The Movie API

This SpringBoot service is an example Backend for my lecture "Software Development" at [Duale Hochschule Baden-Württemberg](https://www.dhbw-vs.de/en/index.html). It shall show, how a basic SpringBoot service looks like and how to use Docker/Podman to containerize this Backend.

## Run it locally

### Prerequisites - Local

- Java JDK 21+
- Maven

### How to - Local

To build and run the app locally, run:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

## Run the container

### Prerequisites - Container

- Docker

Or

- Windows Subsystem for Linux (WSL)
- Podman

### How to - Container

To pull and run the container, run:

```bash
docker run -p 8080:8080 ghcr.io/juliangommlich/movie-api:latest
```

or

```bash
podman run -p 8080:8080 ghcr.io/juliangommlich/movie-api:latest
```

## Further Information

To see the other part of the application, visit the [Frontend Repo](https://github.com/JulianGommlich/movie-ui).

