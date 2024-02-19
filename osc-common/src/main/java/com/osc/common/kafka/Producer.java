package com.osc.common.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Producer {

    @Autowired
    private  KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private  ObjectMapper objectMapper;

    public void sendMessage(final String topic, Map<String, String> mailMap) throws JsonProcessingException {
        System.out.println("Producing Message " + objectMapper.writeValueAsString(mailMap));
        this.kafkaTemplate.send(topic, objectMapper.writeValueAsString(mailMap));
    }
}
