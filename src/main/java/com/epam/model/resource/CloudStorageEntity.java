package com.epam.model.resource;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.fusesource.hawtbuf.BufferInputStream;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Document
@Data
@TypeAlias("MinioResource")
public class CloudStorageEntity implements ResourceObj {

    @Transient
    public static final String SEQUENCE_NAME = "res_sequence";

    private String id;
    private String storageId;

    private String URL ;
    private String accessKey;
    private String secretKey;

    private  String bucket;
    private String name;
    @Transient
    private MinioClient minioClient;


    public CloudStorageEntity() {

    }

    private MinioClient buildClient(){
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(URL)
                        .credentials(accessKey, secretKey)
                        .build();
        return minioClient;
    }

    public CloudStorageEntity(String storageId, String URL, String accessKey, String secretKey, String bucket,String name) {
        this.storageId = storageId;
        this.URL = URL;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.name = name;
    }

    public CloudStorageEntity(String storageId, String URL, String accessKey, String secretKey, String bucket) {
        this.storageId = storageId;
        this.URL = URL;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        createFileName();
    }
    @SneakyThrows
    @Override
    public InputStream read() throws IOException {
        if(minioClient==null) this.minioClient = buildClient();
        try (InputStream stream = new ByteArrayInputStream(minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(this.bucket)
                        .object(this.name)
                        .build()).readAllBytes())){

            return stream;
        }
        catch (IOException e){
            throw new IOException(e);
        }
    }

    @Override
    public void save(InputStream stream) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        byte[] buffer = stream.readAllBytes();
        stream.close();
        try (ByteArrayInputStream bis= new ByteArrayInputStream(buffer)){
            long  size =buffer.length;
            this.minioClient = buildClient();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucket).object(name).stream(
                                    bis, size, -1)
                            .build());
        }
    }

    private void createFileName(){
        UUID uuid = UUID.randomUUID();
        this.name = uuid.toString() +".mp3";
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void delete() {

    }
    @Override
    public Class<? extends ResourceObj> supports() {
        return this.getClass();
    }

    @Override
    public String getFileName() {
        return  this.name;
    }

    @Override
    public void setPath(String path) {

    }
}

