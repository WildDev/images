package fun.wilddev.images.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.wilddev.images.config.rabbitmq.DeadLetterQueueNameFactory;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.retry.policy.NeverRetryPolicy;

import fun.wilddev.images.config.props.*;
import fun.wilddev.images.rabbitmq.recoverer.*;
import fun.wilddev.spring.core.services.date.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitConf {

    private final static long X_MAX_LENGTH = 100;

    private final static int X_MESSAGE_TTL = 86_400_000;

    private final ExchangeProps exchangeProps;

    private final QueueMappingProps queueMappingProps;

    private final RoutingKeyMappingProps routingKeyMappingProps;

    private final String defaultImagesDeadLetterQueue;

    private final String externalImagesDeadLetterQueue;

    private final String webhooksDeadLetterQueue;

    private final String imageRemovalDeadLetterQueue;

    public RabbitConf(ExchangeProps exchangeProps,
                      QueueMappingProps queueMappingProps,
                      RoutingKeyMappingProps routingKeyMappingProps,
                      DeadLetterQueueNameFactory deadLetterQueueNameFactory) {

        this.exchangeProps = exchangeProps;
        this.queueMappingProps = queueMappingProps;
        this.routingKeyMappingProps = routingKeyMappingProps;

        this.defaultImagesDeadLetterQueue = deadLetterQueueNameFactory.create(queueMappingProps.defaultImages());
        this.externalImagesDeadLetterQueue = deadLetterQueueNameFactory.create(queueMappingProps.externalImages());
        this.webhooksDeadLetterQueue = deadLetterQueueNameFactory.create(queueMappingProps.webhooks());
        this.imageRemovalDeadLetterQueue = deadLetterQueueNameFactory.create(queueMappingProps.imageRemoval());
    }

    private SimpleRabbitListenerContainerFactory buildContainerFactory(ConnectionFactory connectionFactory,
                                                                       MessageConverter messageConverter,
                                                                       Advice... adviceChain) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAdviceChain(adviceChain);
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

    private SimpleRabbitListenerContainerFactory buildImageContainerFactory(ConnectionFactory connectionFactory,
                                                                            MessageConverter messageConverter,
                                                                            ImageRecoverer imageRecoverer) {
        return buildContainerFactory(connectionFactory, messageConverter, retryOperationsInterceptor(imageRecoverer));
    }

    private Queue buildTickQueue(QueueProps queueProps) {

        return QueueBuilder.nonDurable(queueProps.name())
                .withArgument("x-single-active-consumer", true)
                .withArgument("x-max-length", queueProps.maxLength())
                .build();
    }

    private Queue buildClassicQueue(QueueProps queueProps, String deadLetterExchange) {

        return QueueBuilder.durable(queueProps.name())
                .deadLetterExchange(deadLetterExchange)
                .ttl(queueProps.ttl())
                .build();
    }

    private Queue buildDeadLetterQueue(String name) {
        return QueueBuilder.durable(name)
                .maxLength(X_MAX_LENGTH)
                .ttl(X_MESSAGE_TTL)
                .build();
    }

    private Binding bindToExistingExchange(QueueProps queueProps,
                                           RoutingKeyProps routingKeyProps,
                                           String exchange) {

        return new Binding(queueProps.name(), Binding.DestinationType.QUEUE,
                exchange, routingKeyProps.name(), null);
    }

    private MethodInterceptor retryOperationsInterceptor(Recoverer<?> recoverer) {
        return RetryInterceptorBuilder.stateless()
                .retryPolicy(new NeverRetryPolicy())
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
    public TopicExchange rabbitImageExchange() {
        return new TopicExchange(exchangeProps.image());
    }

    @Bean
    public TopicExchange rabbitImageDeadLetterExchange() {
        return new TopicExchange(exchangeProps.imageDeadLetter());
    }

    @Bean
    public DirectExchange rabbitWebhookExchange() {
        return new DirectExchange(exchangeProps.webhook());
    }

    @Bean
    public DirectExchange rabbitWebhookDeadLetterExchange() {
        return new DirectExchange(exchangeProps.webhookDeadLetter());
    }

    @Bean
    public Queue rabbitImagePollTickQueue() {
        return buildTickQueue(queueMappingProps.imagePollTick());
    }

    @Bean
    public Queue rabbitImageGcTickQueue() {
        return buildTickQueue(queueMappingProps.imageGcTick());
    }

    @Bean
    public Queue rabbitDefaultImagesQueue() {
        return buildClassicQueue(queueMappingProps.defaultImages(), exchangeProps.imageDeadLetter());
    }

    @Bean
    public Queue rabbitDefaultImagesDeadLetterQueue() {
        return buildDeadLetterQueue(defaultImagesDeadLetterQueue);
    }

    @Bean
    public Queue rabbitExternalImagesQueue() {
        return buildClassicQueue(queueMappingProps.externalImages(), exchangeProps.imageDeadLetter());
    }

    @Bean
    public Queue rabbitExternalImagesDeadLetterQueue() {
        return buildDeadLetterQueue(externalImagesDeadLetterQueue);
    }

    @Bean
    public Queue rabbitWebhookPollTickQueue() {
        return buildTickQueue(queueMappingProps.webhookPollTick());
    }

    @Bean
    public Queue rabbitWebhookGcTickQueue() {
        return buildTickQueue(queueMappingProps.webhookGcTick());
    }

    @Bean
    public Queue rabbitWebhooksQueue() {
        return buildClassicQueue(queueMappingProps.webhooks(), exchangeProps.webhookDeadLetter());
    }

    @Bean
    public Queue rabbitWebhooksDeadLetterQueue() {
        return buildDeadLetterQueue(webhooksDeadLetterQueue);
    }

    @Bean
    public Queue rabbitImageRemovalQueue() {
        return buildClassicQueue(queueMappingProps.imageRemoval(), exchangeProps.imageDeadLetter());
    }

    @Bean
    public Queue rabbitImageRemovalDeadLetterQueue() {
        return buildDeadLetterQueue(imageRemovalDeadLetterQueue);
    }

    @Bean
    public Binding rabbitImagePollTickBinding() {
        return bindToExistingExchange(queueMappingProps.imagePollTick(),
                routingKeyMappingProps.imagePollTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding rabbitImageGcTickBinding() {
        return bindToExistingExchange(queueMappingProps.imageGcTick(),
                routingKeyMappingProps.imageGcTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding rabbitDefaultImagesBinding() {
        return BindingBuilder.bind(rabbitDefaultImagesQueue()).to(rabbitImageExchange())
                .with(routingKeyMappingProps.defaultImages().name());
    }

    @Bean
    public Binding rabbitDefaultImagesDeadLetterBinding() {
        return BindingBuilder.bind(rabbitDefaultImagesDeadLetterQueue()).to(rabbitImageDeadLetterExchange())
                .with(routingKeyMappingProps.defaultImages().name());
    }

    @Bean
    public Binding rabbitExternalImagesBinding() {
        return BindingBuilder.bind(rabbitExternalImagesQueue()).to(rabbitImageExchange())
                .with(routingKeyMappingProps.externalImages().name());
    }

    @Bean
    public Binding externalImagesDeadLetterBinding() {
        return BindingBuilder.bind(rabbitExternalImagesDeadLetterQueue()).to(rabbitImageDeadLetterExchange())
                .with(routingKeyMappingProps.externalImages().name());
    }

    @Bean
    public Binding rabbitWebhookPollTickBinding() {
        return bindToExistingExchange(queueMappingProps.webhookPollTick(),
                routingKeyMappingProps.webhookPollTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding rabbitWebhookGcTickBinding() {
        return bindToExistingExchange(queueMappingProps.webhookGcTick(),
                routingKeyMappingProps.webhookGcTick(), exchangeProps.scheduler());
    }

    @Bean
    public Binding rabbitWebhooksBinding() {
        return BindingBuilder.bind(rabbitWebhooksQueue()).to(rabbitWebhookExchange())
                .with(routingKeyMappingProps.webhooks().name());
    }

    @Bean
    public Binding rabbitWebhooksDeadLetterBinding() {
        return BindingBuilder.bind(rabbitWebhooksDeadLetterQueue()).to(rabbitWebhookDeadLetterExchange())
                .with(routingKeyMappingProps.webhooks().name());
    }

    @Bean
    public Binding rabbitImageRemovalBinding() {
        return BindingBuilder.bind(rabbitImageRemovalQueue()).to(rabbitImageExchange())
                .with(routingKeyMappingProps.imageRemoval().name());
    }

    @Bean
    public Binding rabbitImageRemovalDeadLetterBinding() {
        return BindingBuilder.bind(rabbitImageRemovalDeadLetterQueue()).to(rabbitImageDeadLetterExchange())
                .with(routingKeyMappingProps.imageRemoval().name());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory defaultImagesRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter,
            ImageRecoverer imageRecoverer) {

        return buildImageContainerFactory(connectionFactory, messageConverter, imageRecoverer);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory externalImagesRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter,
            ImageRecoverer imageRecoverer) {

        return buildImageContainerFactory(connectionFactory, messageConverter, imageRecoverer);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory imageRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        return buildContainerFactory(connectionFactory, messageConverter, (Advice[]) null);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory webhookRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter,
            WebhookRecoverer webhookRecoverer) {

        return buildContainerFactory(connectionFactory, messageConverter,
                retryOperationsInterceptor(webhookRecoverer));
    }
}
