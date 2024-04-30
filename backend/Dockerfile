
# Use Maven and OpenJDK 17 to build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /spring-app
COPY . .
RUN mvn clean package

# Use OpenJDK 17 to run the application
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /spring-app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
