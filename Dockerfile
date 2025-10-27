FROM eclipse-temurin:17-jdk-alpine AS build-stage
WORKDIR /app

COPY ./.mvn/ ./.mvn/
COPY ./pom.xml ./mvnw ./
RUN ./mvnw dependency:go-offline -B

COPY ./src ./src
RUN ./mvnw package -DskipTests -DdockerBuild=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build-stage /app/build/app.jar .

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]