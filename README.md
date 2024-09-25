### Description

This is a reactive images processing service that may be used to enpower your apps with the images support.
It allows to maintain a number of image versions to be rendered on various devices just like desktops, phones or tables

[![Java CI with Maven](https://github.com/WildDev/images/actions/workflows/maven.yml/badge.svg)](https://github.com/WildDev/images/actions/workflows/maven.yml) [![Docker Image CI](https://github.com/WildDev/images/actions/workflows/docker-image.yml/badge.svg)](https://github.com/WildDev/images/actions/workflows/docker-image.yml)

### How it works

1. Upload an image \ Provide an external link
2. Get the generated ID
3. Receive a webhook once the image is ready

Then it may be accessed in this way:

```
https://test.website/images/find/650f35128d400f39c6a46ee2?width=1024
```

### Examples

###### Original image (109.31KB):
![Original image](src/main/resources/images/sample.jpg)

###### [Resize] Width 320px (16.88KB)
![Width 320px](src/main/resources/images/width320.jpg)

###### [Crop] Width 320px & ratio 1.35 (18.28KB)
![Crop to ratio 1.35](src/main/resources/images/ratio1.35.jpg)


> [!NOTE]
> In order to crop an image the service will first try to resize it. Once the resized copy covers the target dimensions the cropping is performed. Use this mode to create image previews.

### Get started

Build requirements:
* latest JDK and Maven

Runtime stack:
* Java 20
* MongoDB 6
* RabbitMQ 4
* [rabbitmq-cron](https://github.com/WildDev/rabbitmq-cron)

Checkout the project and build it using `mvn package` command

An example run:

```cmd
java -jar -Xmx512M target/images.jar \
    --server.port=8000 \
    --spring.cors.allowed-origins=http://image-uploader-1:8080,https://test.website \
    --spring.data.mongodb.host=mongodb \
    --spring.data.mongodb.username=images \
    --spring.data.mongodb.password=test \
    --spring.rabbitmq.host=rabbitmq \
    --spring.rabbitmq.username=images \
    --spring.rabbitmq.password=test \
    --image.processor.tasks=CROP \
    --webhook.url=http://image-uploader-1:8080/images/webhook
```

> [!WARNING]
> Ensure that `rabbitmq-cron` service runs on the same RabbitMQ username and initially was launched first

> [!WARNING]
> The service is unprotected by default! Ensure the only accessor endpoint is publicly available and your proxy server is properly configured

Also available on [Docker Hub](https://hub.docker.com/r/wilddev/images)
