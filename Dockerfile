FROM openjdk:11
VOLUME /tmp
ADD ./target/microservicios-operaciones-0.0.1-SNAPSHOT.jar microservicio-operaciones.jar
ENTRYPOINT ["java","-jar","/microservicio-operaciones.jar"]



