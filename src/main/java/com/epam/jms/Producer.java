package com.epam.jms;

import com.epam.parsers.Mp3Metadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(Mp3Metadata message) {
        log.info("send message: " + message);
        jmsTemplate.convertAndSend("meta",  message);
    }
}
