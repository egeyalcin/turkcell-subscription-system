# Temel imaj
FROM amazoncorretto:21-al2023-headless

# Çalışma dizini
WORKDIR /app

# Dışarıdan gelecek olan JAR yolunu tanımlıyoruz
ARG JAR_FILE

# Belirtilen JAR dosyasını kopyala
COPY ${JAR_FILE} app.jar

# JVM Optimizasyonları ile çalıştır
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]