# --- Etapa 1: Construcción (Build Stage) ---
# Usamos una imagen oficial de Maven con Java 17 para construir el proyecto.
# Le ponemos un alias "build" para referirnos a esta etapa después.
FROM maven:3.9-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copiamos primero el archivo pom.xml para aprovechar el cache de Docker.
# Si las dependencias no cambian, Docker no las descargará de nuevo.
COPY pom.xml .

# Descargamos todas las dependencias del proyecto.
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente de tu proyecto.
COPY src ./src

# Ejecutamos el comando para compilar y empaquetar la aplicación en un .jar.
# Saltamos los tests para acelerar la construcción en el despliegue.
RUN mvn clean package -DskipTests


# --- Etapa 2: Ejecución (Run Stage) ---
# Ahora, usamos una imagen base mucho más ligera que solo contiene Java 17.
# Esto hace que nuestro contenedor final sea más pequeño y seguro.
FROM eclipse-temurin:17-jre

# Establecemos el directorio de trabajo.
WORKDIR /app

# Copiamos ÚNICAMENTE el archivo .jar generado en la Etapa 1 ("build").
# Esto es lo único que necesitamos para ejecutar la aplicación.
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto en el que corre tu aplicación (8083 según tu application.properties).
EXPOSE 8081

# Este es el comando que se ejecutará cuando el contenedor se inicie.
ENTRYPOINT ["java", "-jar", "app.jar"]