FROM openjdk
VOLUME /tmp
ARG JAR_FILE=target/bnpl-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]