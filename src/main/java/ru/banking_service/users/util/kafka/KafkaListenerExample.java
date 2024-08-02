package ru.banking_service.users.util.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListenerExample {

    @KafkaListener(topics = "topic-1", groupId = "group1")
    void listener(String data) {
        log.info("Received message [{}] in group 1", data);
    }

    @KafkaListener(topics = "topic-1,topic-2", groupId = "group1")
    void listenerWithHeaders(@Payload String data,
                             @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                             @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Received message [{}] from group 1, partition--{} with offset {}", data, partition, offset);
    }

    @KafkaListener(
            groupId = "group2",
            topicPartitions = @TopicPartition(topic = "topic-2",
            partitionOffsets = {
                    @PartitionOffset(partition = "0", initialOffset = "0"),
                    @PartitionOffset(partition = "3", initialOffset = "0")
            })
    )
    public void listenToPartition(@Payload String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("Received message [{}] from partition--{}", message, partition);
    }
}
