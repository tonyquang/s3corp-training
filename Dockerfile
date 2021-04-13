# FROM adoptopenjdk:11-jre-hotspot as builder
# ARG JAR_FILE=target/s3training-eurekaserver-0.0.1-SNAPSHOT.jar
# COPY ${JAR_FILE} application.jar
# RUN java -Djarmode=layertools -jar application.jar extract
# EXPOSE 9000
# ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
#
# FROM adoptopenjdk:11-jre-hotspot as builder
# EXPOSE 9000
# ADD /target/s3training-cloud-gateway-0.0.1-SNAPSHOT.jar s3training-cloud-gateway-0.0.1-SNAPSHOT.jar
# ENTRYPOINT ["java","-jar","s3training-cloud-gateway-0.0.1-SNAPSHOT.jar"]

FROM java:8
FROM maven:alpine
# image layer
WORKDIR /app/s3training-cloud-gateway
#Image layer: with the application
COPY ./s3training-cloud-gateway /app/s3training-cloud-gateway
RUN mvn -v
RUN mvn clean install -DskipTests
ENTRYPOINT ["java", "-Deureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka","-jar","/app/s3training-cloud-gateway/target/s3training-cloud-gateway-0.0.1-SNAPSHOT.jar"]