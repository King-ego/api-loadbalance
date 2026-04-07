FROM eclipse-temurin:21-jdk

WORKDIR /src/app

COPY . .

EXPOSE 8090

CMD ["./mvnw", "spring-boot:run"]