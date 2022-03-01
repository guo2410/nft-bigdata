FROM openjdk:8-jre-alpine
LABEL maintainer="guoch"

COPY target/*.jar /app.jar
RUN apk add -U tzdata; \
ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime; \
echo 'Asia/Shanghai' >/etc/timezone; \
touch /app.jar;
ENV JAVA_OPTS=""
ENV PARAMS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom$JAVA_OPTS -jar /app.jar $PARAMS" ]