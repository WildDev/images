package fun.wilddev.images.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.DateFormat;

import com.fasterxml.jackson.databind.*;
import org.springframework.context.annotation.*;

@Configuration
public class JacksonConf {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .setDateFormat(DateFormat.getDateTimeInstance());

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return objectMapper;
    }
}
