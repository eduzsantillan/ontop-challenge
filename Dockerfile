FROM  openjdk
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app-challenge.jar
ENTRYPOINT ["java","-jar","/app-challenge.jar"]

