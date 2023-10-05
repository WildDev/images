package fun.wilddev.images.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.wilddev.images.config.rabbitmq.DeadLetterQueueNameFactory;

import java.util.concurrent.TimeUnit;

import org.aopalliance.aop.Advice;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import fun.wilddev.images.config.props.*;
import fun.wilddev.images.rabbitmq.recoverer.*;
import fun.wilddev.spring.core.services.date.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConf {

    private final ExchangeProps exchangeProps;

    private final QueueMappingProps queueMappingProps;

    private final RoutingKeyMappingProps routingKeyMappingProps;

    private final DurationReader durationReader;

    private final String defaultImagesDeadLetterQueue;

    private final String externalImagesDeadLetterQueue;

    private final String webhooksDeadLetterQueue;

    public RabbitConf(ExchangeProps exchangeProps,
                      QueueMappingProps queueMappingProps,
                      RoutingKeyMappingProps routingKeyMappingProps,
                      DurationReader durationReader,
                      DeadLetterQueueNameFactory deadLetterQueueNameFactory) {

        this.exchangeProps = exchangeProps;
        this.queueMappingProps = queueMappingProps;
        this.routingKeyMappingProps = routingKeyMappingProps;
        this.durationReader = durationReader;

        this.defaultImagesDeadLetterQueue = deadLetterQueueNameFactory.create(queueMappingProps.defaultImages());
        this.externalImagesDeadLetterQueue = deadLetterQueueNameFactory.create(queueMappingProps.externalImages());
        this.webhooksDeadLetterQueue = deadLetterQueueNameFactory.create(queueMappingProps.webhooks());
    }

    private SimpleRabbitListenerContainerFactory buildContainerFactory(ConnectionFactory connectionFactory,
                                                                       MessageConverter messageConverter,
                                                                       Advice... adviceChain) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAdviceChain(adviceChain);

        return factory;
    }

    private SimpleRabbitListenerContainerFactory buildImageContainerFactory(ConnectionFactory connectionFactory,
                                                                            MessageConverter messageConverter,
                                                                            ImageRecoverer imageRecoverer,
                                                                            QueueProps queueProps) {
        return buildContainerFactory(connectionFactory, messageConverter,
                retryOperationsInterceptor(queueProps, imageRecoverer));
    }

    private Queue buildTickQueue(QueueProps queueProps) {

        return QueueBuilder.nonDurable(queueProps.name())
                .withArgument("x-max-length", queueProps.maxLength())
                .build();
    }

    private Queue buildClassicQueue(QueueProps queueProps, String deadLetterExchange) {

        return QueueBuilder.durable(queueProps.name())
                .deadLetterExchange(deadLetterExchange)
                .ttl(queueProps.ttl())
                .build();
    }

    private Binding bindToExistingExchange(QueueProps queueProps,
                                           RoutingKeyProps routingKeyProps,
                                           String exchange) {

        return new Binding(queueProps.name(), Binding.DestinationType.QUEUE,
                exchange, routingKeyProps.name(), null);
    }

    private long readRetryIntervalAsMillis(RetryProps retryProps) {

        final DurationValue durationValue = durationReader.read(retryProps.interval());

        TimeUnit timeUnit = durationValue.timeUnit();

        if (timeUnit == TimeUnit.SECONDS)
            return durationValue.value() * 1000;

        throw new UnsupportedOperationException("Time unit '" + timeUnit + "' is not supported");
    }

    private FixedBackOffPolicy backOffPolicy(RetryProps retryProps) {

        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(readRetryIntervalAsMillis(retryProps));

        return policy;
    }

    private RetryTemplate retryTemplate(QueueProps queueProps) {

        RetryTemplate template = new RetryTemplate();
        RetryProps retryProps = queueProps.retry();

        template.setRetryPolicy(new SimpleRetryPolicy(retryProps.maxAttempts()));
        template.setBackOffPolicy(backOffPolicy(retryProps));

        return template;
    }

    private StatefulRetryOperationsInterceptor retryOperationsInterceptor(QueueProps queueProps,
                                                                          Recoverer<?> recoverer) {
        return RetryInterceptorBuilder.stateful()
                .retryOperations(retryTemplate(queueProps))
                .recoverer(recoverer)
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setCreateMessageIds(true);

        return converter;
    }

    @Bean
    public TopicExchange imageExchange() {
        return new TopicExchange(exchangeProps.image());
    }

    @Bean
    public TopicExchange imageDeadLetterExchange() {
        return new TopicExchange(exchangeProps.imageDeadLetter());
    }

    @Bean
    public DirectExchange webhookExchange() {
        return new DirectExchange(exchangeProps.webhook());
    }

    @Bean
    public DirectExchange webhookDeadLetterExchange() {
        return new DirectExchange(exchangeProps.webhookDeadLetter());
    }

    @Bean
    public Queue imagePollTickQueue() {
        return buildTickQueue(queueMappingProps.imagePollTick());
    }

    @Bean
    public Queue imageGcTickQueue() {
        return buildTickQueue(queueMappingProps.imageGcTick());
    }

    @Bean
    public Queue defaultImagesQueue() {
        return buildClassicQueue(queueMappingProps.defaultImages(), exchangeProps.imageDeadLetter());
    }

    @Bean
    public Queue defaultImagesDeadLetterQueue() {
        return QueueBuilder.durable(defaultImagesDeadLetterQueue).build();
    }

    @Bean
    public Queue externalImagesQueue() {
        return buildClassicQueue(queueMappingProps.externalImages(), exchangeProps.imageDeadLetter());
    }

    @Bean
    public Queue externalImagesQueueDeadLetter() {
        return QueueBuilder.durable(externalImagesDeadLetterQueue).build();
    }

    @Bean
    public Queue webhookPollTickQueue() {
        return buildTickQueue(queueMappingProps.webhookPollTick());
    }

    @Bean
    public Queue webhookGcTickQueue() {
        return buildTickQueue(queueMappingProps.webhookGcTick());
    }

    @Bean
    public Queue webhooksQueue() {
        return buildClassicQueue(queueMappingProps.webhooks(), exchangeProps.webhookDeadLetter());
    }

    @Bean
    public Queue webhooksDeadLetterQueue() {
        return QueueBuilder.durable(webhooksDeadLetterQueue).build();
    }

    @Bean
    public Binding imagePollTickBinding() {
        return bindToExistingExchange(queueMappingProps.imagePollTick(),
                routingKeyMappingProps.secondTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding imageGcTickBinding() {
        return bindToExistingExchange(queueMappingProps.imageGcTick(),
                routingKeyMappingProps.minuteTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding defaultImagesBinding() {
        return BindingBuilder.bind(defaultImagesQueue()).to(imageExchange())
                .with(routingKeyMappingProps.defaultImages().name());
    }

    @Bean
    public Binding defaultImagesDeadLetterBinding() {
        return BindingBuilder.bind(defaultImagesQueue()).to(imageDeadLetterExchange())
                .with(routingKeyMappingProps.defaultImages().name());
    }

    @Bean
    public Binding externalImagesBinding() {
        return BindingBuilder.bind(externalImagesQueue()).to(imageExchange())
                .with(routingKeyMappingProps.externalImages().name());
    }

    @Bean
    public Binding externalImagesDeadLetterBinding() {
        return BindingBuilder.bind(externalImagesQueue()).to(imageDeadLetterExchange())
                .with(routingKeyMappingProps.externalImages().name());
    }

    @Bean
    public Binding webhookPollTickBinding() {
        return bindToExistingExchange(queueMappingProps.webhookPollTick(),
                routingKeyMappingProps.secondTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding webhookGcTickBinding() {
        return bindToExistingExchange(queueMappingProps.webhookGcTick(),
                routingKeyMappingProps.minuteTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding webhooksBinding() {
        return BindingBuilder.bind(webhooksQueue()).to(webhookExchange())
                .with(routingKeyMappingProps.webhooks().name());
    }

    @Bean
    public Binding webhooksDeadLetterBinding() {
        return BindingBuilder.bind(webhooksDeadLetterQueue()).to(webhookDeadLetterExchange())
                .with(routingKeyMappingProps.webhooks().name());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory defaultImagesRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter,
            ImageRecoverer imageRecoverer) {

        return buildImageContainerFactory(connectionFactory, messageConverter,
                imageRecoverer, queueMappingProps.defaultImages());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory externalImagesRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter,
            ImageRecoverer imageRecoverer) {

        return buildImageContainerFactory(connectionFactory, messageConverter,
                imageRecoverer, queueMappingProps.externalImages());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory webhookRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter,
            WebhookRecoverer webhookRecoverer) {

        return buildContainerFactory(connectionFactory, messageConverter,
                retryOperationsInterceptor(queueMappingProps.webhooks(), webhookRecoverer));
    }
}
