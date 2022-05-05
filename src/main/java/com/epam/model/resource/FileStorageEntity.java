package com.epam.model.resource;

import com.epam.model.entity.StorageType;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Document
@TypeAlias("FSResource")
public class FileStorageEntity implements ResourceObj {
    @Transient
    private File file;

    @Transient
    public static final String SEQUENCE_NAME = "res_sequence";

    private String id;
    private String  path;
    private String storageId;


    public FileStorageEntity( String storageId,String path) {
        this.path = createFilepath(path).toString();
        this.storageId = storageId;
    }

    public FileStorageEntity(StorageType diskFileSystem) {

    }

    public FileStorageEntity() {
    }

    @Override
    public InputStream read() throws IOException {
        try {
            return new FileInputStream(path);
        } catch (IOException e) {
            throw new IOException("Exception occurred while decompressing input stream. ", e);
        }
    }
    @Override
    public void save(InputStream inputStream)  {
        this.file = new File(this.path);
            try ( InputStream inputStreamToUse = inputStream;
                    OutputStream outputStream = new FileOutputStream(this.file)) {
                IOUtils.copy(inputStreamToUse, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private Path createFilepath(String path){
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        Path newPath = Paths.get(path);
        return newPath.resolve(uuidAsString+".mp3");
    }

    @Override
    public void setStorageId(String storageId) {
         this.storageId = storageId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getStorageId() {
        return  this.storageId;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void delete() {
        file.delete();
    }

    @Override
    public void setPath(String path) {
        this.path=path;
    }

    @Override
    public Class<? extends ResourceObj> supports() {
        return this.getClass();
    }

    @Override
    public String getFileName() {
        return Paths.get(this.path).getFileName().toString();
    }

    @Override
    public void save(ContentConsumer contentConsumer) throws IOException {

    }
}
