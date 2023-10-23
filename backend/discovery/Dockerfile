FROM openjdk:17-alpine

ARG JAR_FILE=/build/libs/*.jar

RUN echo "JAR_FILE -> ${JAR_FILE}"

COPY ${JAR_FILE} /app.jar

EXPOSE 9000

ENTRYPOINT ["java","-jar", "/app.jar"]