# Sadece JAR'ı kopyalayan en hafif (Best Practice) Dockerfile
FROM amazoncorretto:21-al2023-headless
WORKDIR /app
# Kök dizinden değil, servis klasörünün içinden JAR'ı kopyala
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]