# Etapa 1: Compilación de la aplicación con Maven y Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar y empaquetar la aplicación omitiendo los tests para acelerar el build
RUN mvn clean package -DskipTests

# Etapa 2: Imagen liviana de ejecución (JRE 17)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear la carpeta de uploads para guardar los PDFs e imágenes subidos
RUN mkdir -p /app/uploads

# Copiar el archivo JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]