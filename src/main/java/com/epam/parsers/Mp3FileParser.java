package com.epam.parsers;
;
import com.epam.exceptions.FileParseException;
import lombok.Data;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Data
public class Mp3FileParser implements AudioParser {

    private  String fileExtension = ".mp3" ;

    private File createTmpFile(InputStream stream) throws IOException {

       File tmpFile = File.createTempFile("data", fileExtension);
       try  {
           IOUtils.copy(stream,new FileOutputStream(tmpFile));
           stream.close();
           return  tmpFile;
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }
   public Mp3Metadata getMetadata(InputStream stream) throws TagException, FileParseException, IOException {
        File file = createTmpFile(stream);
        MP3File mp3File  = new MP3File(file);
        AbstractID3v2 abstractID3v2 = create(mp3File);
        Mp3Metadata metadata = new Mp3Metadata(getName(abstractID3v2),getYear(abstractID3v2),
                getNotes(abstractID3v2),getAlbum(abstractID3v2));
        file.delete();
        return metadata;
    }

    private AbstractID3v2 create(MP3File mp3File) throws  FileParseException {
        if(mp3File.hasID3v2Tag()) {
         return mp3File.getID3v2Tag();
        }
        else throw new FileParseException(Mp3FileParser.class);
    }

    private String getName(AbstractID3v2 abstractID3v2) throws FileParseException {
        String name =abstractID3v2.getSongTitle();
//        if(name == null|| name.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
//        }
        return name;
    }

    private String getAlbum(AbstractID3v2 abstractID3v2) throws FileParseException {
        String album = abstractID3v2.getAlbumTitle();
//        if(album == null || album.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
//        }
        return album;
    }

    private int getYear(AbstractID3v2 abstractID3v2) throws FileParseException {
        String year = abstractID3v2.getYearReleased();
        if(year == null || year.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
        return  0;
        }
        return Integer.parseInt(year);
    }

    private String getNotes(AbstractID3v2 abstractID3v2) throws FileParseException{
        String notes = abstractID3v2.getSongComment();
//        if(notes == null || notes.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
//        }
        return notes;
    }
}
