FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app-consumer.jar
ENTRYPOINT ["java","-jar","app-consumer.jar"]
