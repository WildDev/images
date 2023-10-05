package fun.wilddev.images;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConfigurationPropertiesScan
@SpringBootApplication
public class ImageServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageServerApplication.class, args);
	}
}
