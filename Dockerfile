FROM maven:3.9.9-openjdk-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q package

FROM tomcat:10.1.17-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /build/target/calculator.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
