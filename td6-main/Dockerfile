#    _         _ _    _
#   | |__ _  _(_) |__| |
#   | '_ \ || | | / _` |
#   |_.__/\_,_|_|_\__,_|
#
FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# installation des dépendances dans le repo local
RUN cd services && mvn clean install
RUN mvn clean package

#    _ _ _  _ _ _
#   | '_| || | ' \
#   |_|  \_,_|_||_|
#
FROM --platform=linux/amd64 eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=build /app/target/demo.jar webapp.jar
COPY --from=build /app/target/lib/* ./lib/
EXPOSE 3000
RUN ls /app/lib
ENTRYPOINT ["java","-jar","/app/webapp.jar"]
