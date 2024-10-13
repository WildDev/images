FROM maven:3.9.4-eclipse-temurin-20

MAINTAINER https://github.com/WildDev

COPY . /
RUN --mount=type=cache,target=$MAVEN_CONFIG mvn clean package

FROM eclipse-temurin:20-jre

ENV ALLOWED_IMAGES=JPEG,PNG,WEBP
ENV ALLOWED_ORIGINS=http://localhost
ENV IMAGE_POLL_SIZE=100
ENV IMAGE_TIMEOUT=10m
ENV JAVA_OPTS=-Xmx512M
ENV MAX_FILE_SIZE=10485760
ENV MONGODB_HOST=mongodb
ENV MONGODB_PORT=27017
ENV MONGODB_DATABASE=images
ENV MONGODB_USER=images
ENV MONGODB_PASS=test
ENV MONGODB_URI=''
ENV RABBIT_HOST=rabbitmq
ENV RABBIT_USER=images
ENV RABBIT_PASS=test
ENV RABBIT_PORT=5672
ENV SERVER_PORT=8080
ENV TASKS=CROP,RESIZE
ENV WEBHOOK_POLL_SIZE=100
ENV WEBHOOK_TIMEOUT=10m
ENV WEBHOOK_URL=''

COPY --from=0 /target/images.jar /

ENTRYPOINT java -jar $JAVA_OPTS /images.jar \
 --image.allowed-types=$ALLOWED_IMAGES \
 --image.poll-size=$IMAGE_POLL_SIZE \
 --image.processor.tasks=$TASKS \
 --image.timeout=$IMAGE_TIMEOUT \
 --server.port=$SERVER_PORT \
 --spring.cors.allowed-origins=$ALLOWED_ORIGINS \
 --spring.data.mongodb.host=$MONGODB_HOST \
 --spring.data.mongodb.port=$MONGODB_PORT \
 --spring.data.mongodb.database=$MONGODB_DATABASE \
 --spring.data.mongodb.username=$MONGODB_USER \
 --spring.data.mongodb.password=$MONGODB_PASS \
 --spring.data.mongodb.uri=$MONGODB_URI \
 --spring.rabbitmq.host=$RABBIT_HOST \
 --spring.rabbitmq.port=$RABBIT_PORT \
 --spring.rabbitmq.username=$RABBIT_USER \
 --spring.rabbitmq.password=$RABBIT_PASS \
 --spring.servlet.multipart.max-file-size=$MAX_FILE_SIZE \
 --webhook.poll-size=$WEBHOOK_POLL_SIZE \
 --webhook.timeout=$WEBHOOK_TIMEOUT \
 --webhook.url=$WEBHOOK_URL
