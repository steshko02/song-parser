package com.epam;

import com.epam.model.resource.ResourceObj;
import com.epam.repositories.mongo.ResourceObjRepository;
import com.epam.service.CreateSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private ResourceObjRepository resourceObjRepository;

    @Autowired
    private CreateSong createSong;
    @GetMapping("test")
    public void test() throws IOException {

     ResourceObj resourceObj = resourceObjRepository.getResourceById("6170603503eff01a1f0676ed");
        createSong.saveSong(resourceObj);
    }
}
