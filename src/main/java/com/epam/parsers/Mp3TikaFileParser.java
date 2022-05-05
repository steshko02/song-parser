package com.epam.parsers;

import lombok.Data;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.tika.parser.mp3.Mp3Parser;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
@Data
public class Mp3TikaFileParser {

    public Mp3Metadata parse(InputStream stream) throws IOException, SAXException, TikaException {
        try {
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(stream, handler, metadata, parseCtx);
            stream.close();
            Mp3Metadata mp3Metadata = new Mp3Metadata();
            mp3Metadata.setName(nullPointerCheck(metadata.get("dc:title")));

            String year = metadata.get("xmpDM:releaseDate");
            mp3Metadata.setYear(!year.equals("")  ? Integer.parseInt(year): 0);

            mp3Metadata.setArtist(List.of(nullPointerCheck(metadata.get("xmpDM:artist")),
                    nullPointerCheck(metadata.get("xmpDM:albumArtist"))));
            mp3Metadata.setGenre(List.of(nullPointerCheck(metadata.get("xmpDM:genre"))));
            mp3Metadata.setNotes("duration - " + metadata.get("xmpDM:duration") + "\n"
                    + "Content-Type - " + nullPointerCheck(metadata.get("Content-Type")) + "\n" + "version - " + nullPointerCheck(metadata.get("version")
            ));
            mp3Metadata.setAlbum(nullPointerCheck(metadata.get("xmpDM:album")));

            return mp3Metadata;
        }  catch (IOException e) {
            throw new IOException("close stream");
        } catch (SAXException e) {
            throw new SAXException("sax exception");
        } catch (TikaException e) {
            throw  new TikaException("cannot get metadata from stream");
        }
    }

    private String nullPointerCheck(String param){
        return (param!=null && !param.equals("")? param : "");
    }
}
