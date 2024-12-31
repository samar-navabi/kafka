package org.samar.libraryEventsProducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.samar.libraryEventsProducer.record.LibraryEvents;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class LibraryEventsProducer
{
    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }


    //Asynchron calls
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvents(LibraryEvents libraryEvents) throws JsonProcessingException
    {
        var key = libraryEvents.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvents);

        //kafkaTemplate.send(topic, key, value);
        var conpletableFuture =  kafkaTemplate.send(topic, key, value);
            return  conpletableFuture.whenComplete((sendResult, throwable) -> {
                if(throwable != null)
                {
                    errorCase(throwable);
                }else
                {
                    successCase(key, value, sendResult);
                }
            });

    }

    //Second approach
    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvents2(LibraryEvents libraryEvents) throws JsonProcessingException
    {
        var key = libraryEvents.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvents);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key,value,topic);
        var completableFuture =  kafkaTemplate.send(producerRecord);

        return completableFuture
                .whenComplete((sendResult, throwable) -> {
                    if(throwable != null)
                    {
                        errorCase(throwable);
                    }else
                    {
                        successCase(key, value, sendResult);
                    }
                });
    }


    public void successCase(int key, String value, SendResult<Integer, String> sendResult)
    {
        log.info("Success sending the message for key {} and value {}, partition is {}", key, value, sendResult.getRecordMetadata().partition());
    }

    public void errorCase(Throwable ex)
    {
        log.error("Error sending the message and the exception is {}", ex.getMessage(), ex);
    }

    public ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic)
    {
        List<Header> headers = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key , value);
    }

}
