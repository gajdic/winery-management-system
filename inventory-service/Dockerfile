FROM maven:3.9.12-eclipse-temurin-25 AS build

WORKDIR /workspace

COPY pom.xml .
COPY common-service/pom.xml common-service/pom.xml
COPY customer-service/pom.xml customer-service/pom.xml
COPY harvest-service/pom.xml harvest-service/pom.xml
COPY production-service/pom.xml production-service/pom.xml
COPY inventory-service/pom.xml inventory-service/pom.xml

RUN mvn -pl inventory-service -am dependency:go-offline

COPY common-service/src common-service/src
COPY inventory-service/src inventory-service/src

RUN mvn -pl inventory-service -am package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /workspace/inventory-service/target/inventory-service-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
