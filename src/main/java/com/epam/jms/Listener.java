package com.epam.jms;

import com.epam.model.resource.ResourceObj;
import com.epam.repositories.mongo.ResourceObjRepository;
import com.epam.service.CreateSong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

@Component
@Slf4j
public class Listener  {

    @Autowired
    private ResourceObjRepository resourceObjRepository;

    @Autowired
    private CreateSong createSong;

    @JmsListener(destination = "resources")
    public void onMessage(Message message) {
        try{
            System.out.println(" Message here!!!");
            String id = ((TextMessage) message).getText();
            ResourceObj resource = resourceObjRepository.getResourceById(id);
            createSong.saveSong(resource);

            log.info("Received Message: "+ id);
        } catch(Exception e) {
            log.error("Received Exception : "+ e);
        }

    }

//    @JmsListener(destination = "resources")
//    @SendTo("zip")
//    public String init(String message) throws Exception {
//       ResourceObj resource = resourceObjRepository.getResourceById(message);
//        createSong.saveSong(resource);
//        return message;
//    }

}
