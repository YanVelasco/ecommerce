# Multi-stage build otimizado para Payment Service
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Cache de dependências
COPY pom.xml ./
RUN mvn -B dependency:go-offline -DskipTests

# Build da aplicação
COPY src ./src
RUN mvn -B -T 1C -DskipTests clean package && rm -rf ~/.m2/repository

# Extrai layers do JAR
RUN java -Djarmode=layertools -jar target/*.jar extract

# Runtime otimizado
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="EAD Platform"
LABEL service="payment-service"

# Instala utilitários necessários (curl) e cria usuário não-root
RUN apk add --no-cache curl \
    && addgroup -S app && adduser -S -G app app \
    && rm -rf /var/cache/apk/* /tmp/*

WORKDIR /app

# Copia layers extraídas
COPY --from=build --chown=app:app /app/dependencies/ ./
COPY --from=build --chown=app:app /app/spring-boot-loader/ ./
COPY --from=build --chown=app:app /app/snapshot-dependencies/ ./
COPY --from=build --chown=app:app /app/application/ ./

USER app

EXPOSE 8086

# JVM flags otimizadas para containers
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=100 \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.backgroundpreinitializer.ignore=true"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
