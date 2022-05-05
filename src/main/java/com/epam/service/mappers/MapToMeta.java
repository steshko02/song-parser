package com.epam.service.mappers;
import com.epam.parsers.Mp3Metadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
@Slf4j
public class MapToMeta implements MessageConverter {

    private ObjectMapper mapper;

    public MapToMeta() {
        mapper = new ObjectMapper();
    }

    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
        Mp3Metadata mp3Metadata = (Mp3Metadata) o;
        String payload = null;
        try {
            payload = mapper.writeValueAsString(mp3Metadata);
        } catch (JsonProcessingException e) {
            log.error("error converting form song", e);
        }
        TextMessage message = session.createTextMessage();
        message.setText(payload);
        return message;    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        TextMessage textMessage = (TextMessage) message;
        String payload = textMessage.getText();
        log.info("inbound json='{}'", payload);

        Mp3Metadata mp3Metadata = null;
        try {
            mp3Metadata = mapper.readValue(payload, Mp3Metadata.class);
        } catch (Exception e) {
            log.error("error converting to song", e);
        }
        return mp3Metadata;
    }
}
