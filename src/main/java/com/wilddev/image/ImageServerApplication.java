package com.wilddev.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.springframework.scheduling.annotation.*;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class ImageServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageServerApplication.class, args);
	}
}
