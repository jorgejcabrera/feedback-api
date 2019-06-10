FROM maven:3.5-jdk-8-alpine as build

RUN mkdir /feedback-api
COPY . /feedback-api/
WORKDIR /feedback-api
RUN mvn -Dmaven.test.skip=true package

ENTRYPOINT [ "sh", "-c", "java -jar -Dspring.profiles.active=$PROFILE -Duser.timezone=$TIMEZONE -XX:+UseG1GC -Xms256m -Xmx2048m -XX:PermSize=2048m -XX:MaxPermSize=2048m -Xss1m /feedback-api/*SNAPSHOT.jar" ]

EXPOSE 8080