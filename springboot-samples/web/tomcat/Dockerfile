FROM openjdk:8-jdk-alpine
ARG JAR_FILE=./base-web-single-host/base-web-single-host-bootstrap/target/base-web-single-host-bootstrap-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /
# 如果是  mac 64位机器，则执行
EXPOSE 8080
EXPOSE 1238
ENV MODULE_CONTROLLER_ADDRESS=127.0.0.1
CMD ["sh", "-c", "java -Dkoupleless.arklet.http.heartbeat.endpoint=http://${MODULE_CONTROLLER_ADDRESS} -jar /base-web-single-host-bootstrap-0.0.1-SNAPSHOT.jar"]
