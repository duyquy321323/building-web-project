FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/buildingweb-1.0.jar buildingweb.jar
EXPOSE 8080
CMD [ "java","-jar","buildingweb.jar" ]