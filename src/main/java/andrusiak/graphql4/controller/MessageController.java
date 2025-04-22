package andrusiak.graphql4.controller;

import andrusiak.graphql4.model.Message;
import andrusiak.graphql4.publisher.MessagePublisher;
import andrusiak.graphql4.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageRepository messageRepository;
    private final MessagePublisher messagePublisher;

    public MessageController(MessageRepository messageRepository, MessagePublisher messagePublisher) {
        this.messageRepository = messageRepository;
        this.messagePublisher = messagePublisher;
    }

    @QueryMapping
    public List<Message> messages() {
        logger.info("Отримання всіх повідомлень");
        return messageRepository.findAll();
    }

    @MutationMapping
    public Message createMessage(@Argument String content, @Argument String author) {
        logger.info("Створення нового повідомлення: {}, автор: {}", content, author);
        Message message = messageRepository.saveMessage(content, author);
        messagePublisher.publish(message);
        return message;
    }

    @SubscriptionMapping
    public Flux<Message> messageAdded() {
        logger.info("Нова підписка на повідомлення активована");
        return messagePublisher.getMessageFlux();
    }
}