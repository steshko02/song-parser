package com.epam.parsers;

import com.epam.exceptions.FileParseException;
import lombok.Data;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@Deprecated
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
        List<String> genres = new ArrayList<>();
        genres.add(getGenre(abstractID3v2));

        List<String> artists = new ArrayList<>();
       artists.add(getArtist(abstractID3v2));

        Mp3Metadata metadata = new Mp3Metadata(getName(abstractID3v2),getYear(abstractID3v2),
                getNotes(abstractID3v2),getAlbum(abstractID3v2),genres,artists);
        file.delete();
        return metadata;
    }

    private AbstractID3v2 create(MP3File mp3File) throws  FileParseException {
        if(mp3File.hasID3v2Tag()) {
         return mp3File.getID3v2Tag();
        }
        else throw new FileParseException(Mp3FileParser.class);
    }

    private String getName(AbstractID3v2 abstractID3v2) throws IOException {

        byte[] name =abstractID3v2.getSongTitle().getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(name);
        InputStreamReader inputStreamReader = new InputStreamReader(in,Charset.forName("UTF-8"));

        String str = "";
            int c;
            while ((c=inputStreamReader.read()) !=-1)
                    str+=(char)c;
        return str;
    }

    private String getAlbum(AbstractID3v2 abstractID3v2) throws UnsupportedEncodingException {
        byte[] album = abstractID3v2.getAlbumTitle().getBytes(StandardCharsets.UTF_8);
//        if(album == null || album.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
//        }
        return new String(album);
    }

    private int getYear(AbstractID3v2 abstractID3v2) throws FileParseException {
        byte[] year = abstractID3v2.getYearReleased().getBytes(StandardCharsets.UTF_8);
        if(year.length==0){
//            throw new FileParseException(Mp3FileParser.class);
        return  0;
        }
        return Integer.parseInt(String.valueOf(year));
    }

    private String getNotes(AbstractID3v2 abstractID3v2) throws FileParseException{
        byte[] notes = abstractID3v2.getSongComment().getBytes(StandardCharsets.UTF_8);
//        if(notes == null || notes.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
//        }
        return String.valueOf(notes);
    }
    private String getArtist(AbstractID3v2 abstractID3v2) throws FileParseException{
        byte[] artist = abstractID3v2.getLeadArtist().getBytes(StandardCharsets.UTF_8);
//        if(notes == null || notes.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
//        }
        return String.valueOf(artist);
    }
    private String getGenre(AbstractID3v2 abstractID3v2) throws FileParseException{
        byte[] genre = abstractID3v2.getSongGenre().getBytes(StandardCharsets.UTF_8);
//        if(notes == null || notes.isEmpty()){
//            throw new FileParseException(Mp3FileParser.class);
//        }
        return String.valueOf(genre);
    }
}
