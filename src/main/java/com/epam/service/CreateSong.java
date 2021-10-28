package com.epam.service;

import com.epam.exceptions.CheckSumException;
import com.epam.exceptions.FileParseException;
import com.epam.model.entity.Resource;
import com.epam.model.entity.Song;
import com.epam.model.resource.ResourceObj;
import com.epam.model.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.CountingInputStream;
import org.farng.mp3.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.epam.parsers.AudioParser;
import com.epam.parsers.Mp3Metadata;
import com.epam.repositories.mongo.StorageRepository;
import com.epam.service.interfaces.entity_interface.ResourceService;
import com.epam.service.interfaces.entity_interface.SongService;
import com.epam.service.interfaces.entity_interface.AlbumService;
import com.epam.utils.CheckSumImpl;
import com.epam.utils.UnzipUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

@Service
@Transactional
@Slf4j
public class CreateSong {

    private final String MSG_DIGEST = "MD5";

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AudioParser mp3Parser;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SongService songService;

    @Autowired
    private StorageRepository storageRepository;

    public  void  saveSong(ResourceObj resourceObj) throws IOException {
        try(BufferedInputStream stream = new BufferedInputStream(resourceObj.read())) {
            if(UnzipUtils.isZip(stream)){
                Storage storage = storageRepository.getStorageById(resourceObj.getStorageId());
                UnzipUtils.unzip(stream, x-> {
                    ResourceObj innerResource = storage.requestBuilder().withCompression().build();
                    try {
                        innerResource.save(new ByteArrayInputStream(x.toByteArray()));
                        createSong(innerResource);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                resourceObj.delete();
            }else{
                createSong(resourceObj);
            }
        } catch (IOException e) {
            throw new IOException("failed parse file");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Resource createResource(String path, Long size, MessageDigest md) throws Exception {
        try {
            String checkSumRes = CheckSumImpl.create(md);
            if(!resourceService.ifExistsByCheckSum(checkSumRes)){
               return new Resource(path,size,checkSumRes);
            }
            else {
                throw  new CheckSumException("there is this song...");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void createSong(ResourceObj resourceObj) throws Exception {
        log.info("this is CREATE SONG!");
        MessageDigest md = MessageDigest.getInstance(MSG_DIGEST);
        try(BufferedInputStream bis = new BufferedInputStream(resourceObj.read());
            CountingInputStream is =new CountingInputStream(new DigestInputStream(bis,md))) {
            Mp3Metadata metadata = mp3Parser.getMetadata(is);
            Song song = new Song();
            song.setResource(createResource(resourceObj.getPath(),is.getByteCount(),md));
            song.setName(metadata.getName());
            song.setAlbum(albumService.findByName(metadata.getAlbum()));
            song.setNotes(metadata.getNotes());
            song.setYear(metadata.getYear());
            song.setResourceObjId(resourceObj.getId());
            songService.addSong(song);
        } catch (IOException | TagException e) {
            throw new Exception("Error: " + e);
        } catch (FileParseException e) {
            throw new FileParseException(AudioParser.class);
        }
    }
}
