package ru.banking_service.users.util.kafka;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(force = true)
@Slf4j
public class KafkaSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Object message, String topicName) {
        log.info("Sending :{}", message);
        log.info("-------------------------------------------");

        kafkaTemplate.send(topicName, message);
    }
}
