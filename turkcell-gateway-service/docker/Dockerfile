FROM amazoncorretto:11-al2023-headless
WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]