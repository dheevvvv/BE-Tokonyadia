# Stage 1: Build aplikasi menggunakan Maven
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Menyalin semua file proyek ke dalam container
COPY . .

# Melakukan build aplikasi (menghasilkan file .jar di target/)
RUN mvn clean package -DskipTests

# Stage 2: Membuat image untuk menjalankan aplikasi
FROM openjdk:17-alpine
WORKDIR /app

# Menyalin hasil build (file JAR) dari stage 1
COPY --from=builder /app/target/tokonyadia_api-0.0.1-SNAPSHOT.jar /app/tokonyadia_api.jar



# Perintah untuk menjalankan aplikasi
ENTRYPOINT ["java", "-jar", "tokonyadia_api.jar"]
