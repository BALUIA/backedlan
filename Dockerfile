# Etapa 1: Construir la aplicación con Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen final con solo el JRE y el JAR
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=build /app/target/security-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
