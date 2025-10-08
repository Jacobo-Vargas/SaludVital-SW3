# ===============================
# Etapa 1: Build con Gradle
# ===============================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copiar archivos necesarios para el build
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

# Build del JAR por ahora sin test
RUN ./gradlew clean build -x test

# ===============================
# Etapa 2: Runtime (solo el JAR)
# ===============================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el JAR construido en la etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar

# Exponer el puerto 
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java","-jar","app.jar"]
