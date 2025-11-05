# DB connection comparison using Gatling load testing

## Description

This repository contains a set of microservices that connect to a database using different connection methods (JDBC, R2DBC, etc.)
and a set of Gatling performance tests to compare their performance under load.

## Requirements

To build and run this application you need to install the followings:

- [JDK 21](https://www.oracle.com/java/technologies/downloads/) (or newer)
- [Maven](https://maven.apache.org/install.html)
- [Podman](https://podman.io/docs/installation) (or Docker) with Compose extension

On macOS, you can use these commands (assuming that [Homebrew](https://brew.sh/) is already installed):

```bash
brew install openjdk@21
brew install --ignore-dependencies maven
brew install podman
brew install podman-compose
```

## Usage

### 1. Start the containers

The following command will read `docker-compose.yml`, pull the images and then starts the containers in detached mode.

```bash
podman-compose up -d
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the performance test for a given module

First you will need to start the module

```bash
cd <module-name>/
java -jar target/service.jar
```

You can see the available endpoints by going to [Swagger UI](http://127.0.0.1:8080/swagger-ui.html)

Then for running the tests run the following commands:

```bash
cd perfromance-tests/
mvn -nsu gatling:test
```

`-nsu` argument is for not updating the snapshot dependencies from remote servers

## Results

### Spike testing

|                 | OK: t < 800 ms | OK: 800 ms <= t < 1200 ms | OK: t >= 1200 ms | Failed |
|-----------------|----------------|---------------------------|------------------|--------|
| reactive-mongo  | 15,000         | 1,023                     | 3,977            | 0      |
| reactive-mysql  | 14,970         | 282                       | 4,39             | 89     |
| sync-mongo      | 9,295          | 3,123                     | 7,582            | 0      |
| sync-jdbc-mysql | 9,317          | 579                       | 10,024           | 20     |
| sync-jpa-mysql  | 155            | 488                       | 19,357           | 0      |
