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

### CRUD simulation

We are going to use 100 users per second for a duration of 10 seconds.
Each user will do 4 requests:

- insert article
- read all articles
- read the inserted article
- delete the article

The time results are for "insert article" requests and the CPU and Heap are overall.
I run this simulation 3 times and the results are from the last run.

| module           | 75th (ms) | 99th (ms) | Max (ms) | Mean (ms) | Max CPU Usage (%) | Max Heap usage (MB) |
|------------------|-----------|-----------|----------|-----------|-------------------|---------------------|
| reactive-mongodb |           |           |          |           |                   |                     |
| reactive-mysql   |           |           |          |           |                   |                     |
| sync-jdbc-mysql  |           |           |          |           |                   |                     |
| sync-jpa-mysql   |           |           |          |           |                   |                     |
| sync-mongodb     |           |           |          |           |                   |                     |

### Spike

We are going to use 1000 users at once.
Each user will do 4 requests:

- insert article
- read all articles
- read the inserted article
- delete the article

The time results are for "insert article" requests and the CPU and Heap are overall.
I run this simulation 3 times and the results are from the last run.

| module           | 75th (ms) | 99th (ms) | Max (ms) | Mean (ms) | Max CPU Usage (%) | Max Heap usage (MB) | Errors (%) |
|------------------|-----------|-----------|----------|-----------|-------------------|---------------------|------------|
| reactive-mongodb |           |           |          |           |                   |                     |            |
| reactive-mysql   |           |           |          |           |                   |                     |            |
| sync-jdbc-mysql  |           |           |          |           |                   |                     |            |
| sync-jpa-mysql   |           |           |          |           |                   |                     |            |
| sync-mongodb     |           |           |          |           |                   |                     |            |
