package fun.wilddev.images.config.props;

public record QueueProps(String name, Integer concurrentConsumers, Integer maxConcurrentConsumers,
                         Integer maxLength, Integer ttl, RetryProps retry) {

}
