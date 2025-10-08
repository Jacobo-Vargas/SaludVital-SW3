# ===============================
# Etapa 1: Build con Gradle
# ===============================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

# Dar permiso de ejecución al gradle wrapper
RUN chmod +x gradlew

# Build del JAR (sin tests)
RUN ./gradlew clean build -x test

# ===============================
# Etapa 2: Runtime (solo el jar)
# ===============================
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
