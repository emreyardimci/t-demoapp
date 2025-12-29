# Runtime image (Java 17)
FROM eclipse-temurin:17-jre

WORKDIR /app

# Jenkins/Maven build çıktısı: target/*.jar
# (artifactId/version değişse bile yakalar)
COPY target/*.jar /app/app.jar

# Uygulama sadece stdout'a yazıyorsa port gerekmez.
# Eğer ileride web app olursa açabilirsin:
# EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
