# ---- Etapa 1: build com Maven + JDK 24 ----
FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Etapa 2: imagem final, só com o JRE ----
FROM eclipse-temurin:24-jre-jammy
WORKDIR /app

COPY --from=build /app/target/Filmes-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
