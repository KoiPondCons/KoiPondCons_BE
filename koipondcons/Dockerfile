#FROM maven:3.9-eclipse-temurin-22 AS build
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM eclipse-temurin:22-alpine
#COPY --from=build /target/*.jar demo.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "demo.jar"]

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:22-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]



