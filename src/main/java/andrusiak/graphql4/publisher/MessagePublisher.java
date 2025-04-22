package andrusiak.graphql4.publisher;

import andrusiak.graphql4.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Component
public class MessagePublisher {
    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    private final DirectProcessor<Message> processor;
    private final FluxSink<Message> sink;

    public MessagePublisher() {
        this.processor = DirectProcessor.create();
        this.sink = processor.sink();
    }

    public void publish(Message message) {
        logger.info("Публікація повідомлення: {}", message.getContent());
        try {
            sink.next(message);
            logger.info("Повідомлення успішно опубліковано");
        } catch (Exception e) {
            logger.error("Помилка при публікації повідомлення", e);
        }
    }

    public Flux<Message> getMessageFlux() {
        logger.info("Видача потоку повідомлень для підписки");
        return processor.onBackpressureBuffer();
    }
}