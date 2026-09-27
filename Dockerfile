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

# O plano free do Render dá só 512Mi de RAM pro container inteiro. Sem
# limites explícitos, o Java tenta usar memória demais (o padrão dele
# tenta enxergar toda a RAM disponível do host, não só a fatia que o
# container realmente tem), e o Render mata o processo com
# "Out of memory". Essas flags mantêm o Java dentro do limite:
#   -Xmx300m            -> teto do heap (memória "de trabalho" da aplicação)
#   -XX:MaxMetaspaceSize -> teto da memória usada pelas classes carregadas
#   -Xss256k            -> pilha de cada thread menor (o padrão gasta mais)
#   -XX:+UseSerialGC     -> coletor de lixo mais simples e mais econômico
#                           em memória, recomendado pra containers pequenos
ENTRYPOINT ["java", "-Xmx300m", "-XX:MaxMetaspaceSize=160m", "-Xss256k", "-XX:+UseSerialGC", "-jar", "app.jar"]
