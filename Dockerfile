# Stage 1: Build the application
FROM eclipse-temurin:21-jdk as builder
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/MoneyManager-0.0.1-SNAPSHOT.jar MoneyManager-v1.0.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT [ "java","-jar","MoneyManager-v1.0.jar" ]