FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} flow-manager-services.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/flow-manager-services.jar"]