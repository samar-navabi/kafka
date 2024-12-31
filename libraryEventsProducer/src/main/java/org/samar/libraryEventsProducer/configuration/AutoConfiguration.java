package org.samar.libraryEventsProducer.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoConfiguration
{
    @Value("${spring.kafka.topic}")
    public String topic;

    @Bean
    public NewTopic topic()
    {
        return TopicBuilder
                .name(topic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
