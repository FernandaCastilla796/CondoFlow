# Imagen del backend de CondoFlow (build en dos etapas).
# Etapa 1: compila con Maven y JDK 21. Etapa 2: sólo el JRE y el .jar (imagen más liviana).

FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Primero sólo el pom y el wrapper: Docker cachea las dependencias mientras el pom no cambie.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline

COPY src/ src/
RUN ./mvnw -q -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 1001 condoflow
COPY --from=build /app/target/condoflow-backend-*.jar app.jar
USER condoflow

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
