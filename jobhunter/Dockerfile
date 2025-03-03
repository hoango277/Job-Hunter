FROM gradle:8.7-jdk17 AS build
COPY --chown=gradle:gradle . /jobhunter
WORKDIR /jobhunter

#skip task: test
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the application
FROM openjdk:17-slim
EXPOSE 8080
COPY --from=build /jobhunter/build/libs/*.jar /spring-boot-job-hunter.jar
ENTRYPOINT ["java", "-jar", "/spring-boot-job-hunter.jar"]