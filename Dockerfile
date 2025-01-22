#FROM docker.lawrenceli.me/csanchez/maven:3.9.9-azulzulu-17-alpine AS builder
#WORKDIR /workspace
#COPY . /workspace
#RUN mkdir -p /root/.m2 && curl -sL https://go.lawrenceli.me/settings.xml -o /root/.m2/settings.xml
#RUN mvn clean package -B -T 2C -Dmaven.test.skip=true
#
FROM docker.lawrenceli.me/azul/zulu-openjdk:17.0.14-jre
WORKDIR /workspace
EXPOSE 8080
#COPY --from=builder /workspace/target/*.jar /workspace/app.jar
COPY target/*.jar /workspace/app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
#HEALTHCHECK --interval=1m --timeout=3s CMD wget -q -T 3 -s http://localhost:8080/api/
