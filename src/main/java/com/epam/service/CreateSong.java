package com.epam.service;

import com.epam.exceptions.CheckSumException;
import com.epam.exceptions.EntityNotFoundException;
import com.epam.exceptions.FileParseException;
import com.epam.jms.Producer;
import com.epam.model.entity.*;
import com.epam.model.resource.ResourceObj;
import com.epam.model.storage.Storage;
import com.epam.parsers.Mp3TikaFileParser;
import com.epam.service.interfaces.entity_interface.*;
import com.epam.service.mappers.MapMetadataToSong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.CountingInputStream;
import org.farng.mp3.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.epam.parsers.AudioParser;
import com.epam.parsers.Mp3Metadata;
import com.epam.repositories.mongo.StorageRepository;
import com.epam.utils.CheckSumImpl;
import com.epam.utils.UnzipUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Set;

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
    private  Producer producer;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private GenreService genreService;
    @Autowired
    private ArtistService artistService;

    @Autowired
    private MapMetadataToSong mapMetadataToSong;

    @Autowired
    private Mp3TikaFileParser mp3TikaFileParser ;

    public  void  saveSong(ResourceObj resourceObj) throws IOException {
        try(BufferedInputStream stream = new BufferedInputStream(resourceObj.read())) {
            if(UnzipUtils.isZip(stream)){
                Storage storage = storageRepository.getStorageById(resourceObj.getStorageId());
                UnzipUtils.unzip(stream, x-> {
                    ResourceObj innerResource = storage.requestBuilder().build();
                    try {
                        innerResource.save(new ByteArrayInputStream(x.toByteArray()));
                        createSong(innerResource);
                    } catch (Exception e) {
                        throw new UncheckedIOException(new IOException("cannot create song!"));
                    }
                });
                resourceObj.delete();
            }else{
                createSong(resourceObj);
            }
        } catch (IOException e) { //рефакторить, выглядит дерьмово
            resourceObj.delete();
            throw new IOException("failed parse file");
        } catch (Exception e) {
            resourceObj.delete();
                    throw new IOException("failed parse file!");
        }
    }

    private Resource createResource(String path, Long size, MessageDigest md) throws Exception {
            String checkSumRes = CheckSumImpl.create(md);
            if(!resourceService.ifExistsByCheckSum(checkSumRes)){
               return new Resource(path,size,checkSumRes);
            } else {
                throw  new CheckSumException("there is this song...");
            }
    }

    public Album createAlbum(Mp3Metadata metadata){
        Album album ;
        try {
            if(metadata.getAlbum()=="")
                throw new EntityNotFoundException(Album.class);
            album = albumService.findByName(metadata.getAlbum());
        }catch (EntityNotFoundException e){
            album = new Album(metadata.getAlbum(),metadata.getYear(),"");

            Set<Genre> genres = genreService.getByNameElseSave(metadata.getGenre());
            Set<Artist> artists = artistService.getByNameElseSave(metadata.getArtist(),genres);
            album.setGenres(genres);
            album.setArtists(artists);
        }
     return album;
    }

    public void createSong(ResourceObj resourceObj) throws Exception {
        log.info("this is CREATE SONG!");
        MessageDigest md = MessageDigest.getInstance(MSG_DIGEST);
        try(BufferedInputStream bis = new BufferedInputStream(resourceObj.read());
            CountingInputStream is =new CountingInputStream(new DigestInputStream(bis,md))) {

            Mp3Metadata metadata = mp3TikaFileParser.parse(is);

            Song song = mapMetadataToSong.mapping(metadata);
            song.setResourceObjId(resourceObj.getId());
            song.setResource(createResource(resourceObj.getPath(),is.getByteCount(),md));

            Album album = createAlbum(metadata);

            song.setAlbum(album);
            songService.addSong(song);
            metadata.setId(song.getId());
            producer.sendMessage(metadata);

        } catch (IOException | TagException e) {
            throw new Exception("Error: " + e);
        } catch (FileParseException e) {
            throw new FileParseException(AudioParser.class);
        }
    }
}
