FROM maven:3.9.4-eclipse-temurin-20

MAINTAINER https://github.com/WildDev

COPY . /
RUN --mount=type=cache,target=$MAVEN_CONFIG mvn clean package

FROM eclipse-temurin:20-jre

ENV ALLOWED_IMAGES=JPEG,PNG,WEBP
ENV ALLOWED_ORIGINS=http://localhost
ENV DISTRIBUTED_LOCK_ENABLED=false
ENV JAVA_OPTS=-Xmx512M
ENV MAX_FILE_SIZE=10485760
ENV MONGODB_HOST=mongodb
ENV MONGODB_PORT=27017
ENV MONGODB_DATABASE=images
ENV MONGODB_USER=images
ENV MONGODB_PASS=test
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379
ENV REDIS_USER=''
ENV REDIS_PASS=''
ENV SERVER_PORT=8080
ENV TASKS=CROP,RESIZE
ENV WEBHOOK_TIMEOUT=10
ENV WEBHOOK_TRIES=3
ENV WEBHOOK_TRIES_DISTANCE=10
ENV WEBHOOK_URL=''

COPY --from=0 /target/images.jar /

ENTRYPOINT java -jar $JAVA_OPTS /images.jar \
 --image.allowed.types=$ALLOWED_IMAGES \
 --image.processor.tasks=$TASKS \
 --image.webhook.url=$WEBHOOK_URL \
 --image.webhook.timeout=$WEBHOOK_TIMEOUT \
 --image.webhook.tries=$WEBHOOK_TRIES \
 --image.webhook.tries.distance=$WEBHOOK_TRIES_DISTANCE \
 --lock.enabled=$DISTRIBUTED_LOCK_ENABLED \
 --server.port=$SERVER_PORT \
 --spring.cors.allowed-origins=$ALLOWED_ORIGINS \
 --spring.data.mongodb.host=$MONGODB_HOST \
 --spring.data.mongodb.port=$MONGODB_PORT \
 --spring.data.mongodb.database=$MONGODB_DATABASE \
 --spring.data.mongodb.username=$MONGODB_USER \
 --spring.data.mongodb.password=$MONGODB_PASS \
 --spring.data.redis.host=$REDIS_HOST \
 --spring.data.redis.port=$REDIS_PORT \
 --spring.data.redis.username=$REDIS_USER \
 --spring.data.redis.password=$REDIS_PASS \
 --spring.servlet.multipart.max-file-size=$MAX_FILE_SIZE
