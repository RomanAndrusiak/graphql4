package andrusiak.graphql4.repository;


import andrusiak.graphql4.model.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class MessageRepository {
    private final List<Message> messages = new CopyOnWriteArrayList<>();

    public Message saveMessage(String content, String author) {
        Message message = new Message(UUID.randomUUID().toString(), content, author);
        messages.add(message);
        return message;
    }

    public List<Message> findAll() {
        return new ArrayList<>(messages);
    }
}