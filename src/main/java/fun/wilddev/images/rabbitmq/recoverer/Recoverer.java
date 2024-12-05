package fun.wilddev.images.rabbitmq.recoverer;

import fun.wilddev.images.rabbitmq.RabbitTrustedPackages;
import fun.wilddev.images.rabbitmq.data.RefData;
import fun.wilddev.images.services.FailureSetter;

import org.slf4j.Logger;

import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;

import com.fasterxml.jackson.databind.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.*;

public abstract class Recoverer<T extends RefData> extends RejectAndDontRequeueRecoverer {

    protected final Logger log;

    protected final Jackson2JavaTypeMapper jackson2JavaTypeMapper;

    protected final ObjectMapper objectMapper;

    protected final FailureSetter failureSetter;

    protected final String failureLogMessageTemplate;

    protected final Class<T> type;

    protected Recoverer(Logger log, ObjectMapper objectMapper, FailureSetter failureSetter,
                        String failureLogMessageTemplate, Class<T> type) {

        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        javaTypeMapper.setTrustedPackages(RabbitTrustedPackages.DATA);

        this.log = log;
        this.jackson2JavaTypeMapper = javaTypeMapper;
        this.objectMapper = objectMapper;
        this.failureSetter = failureSetter;
        this.failureLogMessageTemplate = failureLogMessageTemplate;
        this.type = type;
    }

    @Override
    public void recover(Message message, Throwable cause) {

        final MessageProperties props = message.getMessageProperties();

        try {
            JavaType targetJavaType = jackson2JavaTypeMapper.toJavaType(props);
            Object o = objectMapper.readValue(message.getBody(), targetJavaType);

            if (o != null && o.getClass().isAssignableFrom(type)) {

                final T ref = type.cast(o);
                final String id = ref.getId();

                failureSetter.setFailed(id);
                log.error(failureLogMessageTemplate, id);
            }

        } catch (Exception ex) {
            log.error("Failed to register failure for rabbitmq message {}", props.getMessageId(), ex);
        }

        super.recover(message, cause);
    }
}
