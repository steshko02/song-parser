package com.epam.model.resource;

import com.epam.model.resource.tempResource.SizeAwareInputStream;
import com.epam.model.resource.tempResource.TempResource;
import com.epam.model.resource.tempResource.TempResourceFactory;
import com.epam.model.resource.threshold.ThresholdBasedTempResourceFactory;
import io.minio.*;
import io.minio.errors.*;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Document
@Data
@TypeAlias("MinioResource")
public class CloudStorageEntity implements ResourceObj, ResourceObj.ContentConsumer {

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

    private MinioClient buildClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(URL)
                        .credentials(accessKey, secretKey)
                        .build();
        if (! minioClient.bucketExists(BucketExistsArgs.builder().bucket(this.bucket).build())){
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(this.bucket)
                    .build());
        }
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
        GetObjectArgs getObjectArgs =   GetObjectArgs.builder()
                .bucket(this.bucket)
                .object(this.name)
                .build();

        try {

            return minioClient.getObject(getObjectArgs);
        }
        catch (IOException e){
            throw e;
        }
    }

    @Override
    public void save(InputStream stream) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if(SizeAwareInputStream.class.isAssignableFrom(stream.getClass())){
           saveStreamWithSize(stream, ((SizeAwareInputStream) stream).getInputStreamSize()); return;
        }else{
            try(TempResource tempResource = ThresholdBasedTempResourceFactory.defaults()
                    .createTempResource(stream);
                    InputStream inputStream = tempResource.getInputStream()) {
                saveStreamWithSize(inputStream, tempResource.getSize());
                return;
            }
        }
    }

    private void saveStreamWithSize(InputStream stream,Long size) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        this.minioClient = buildClient();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucket).object(name).stream(
                                    stream, size, -1)
                            .build());
    }

    private void createFileName(){
        UUID uuid = UUID.randomUUID();
        this.name = uuid.toString() +".mp3";
    }

    @Override
    public String getPath() {
        return this.URL;
    }

    @SneakyThrows
    @Override
    public void delete() {
        this.minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).
                object(this.name).build());
    }

    @Override
    public Class<? extends ResourceObj> supports() {
        return this.getClass();
    }

    @Override
    public String getFileName() {
        return  this.name;
    }

    @SneakyThrows
    @Override
    public void save(ContentConsumer contentConsumer) throws IOException {
        TempResourceFactory.TempResourceWriter writer = contentConsumer::writeContent;
        try (TempResource tempResource =
                ThresholdBasedTempResourceFactory.defaults().createTempResource(writer)){
            save(tempResource.getInputStream());
        }
    }

    @Override
    public void setPath(String path) {
        this.URL = path;
    }

    @Override
    public void writeContent(OutputStream outputStream) throws IOException {
        try(InputStream stream = read()) {
            IOUtils.copy(stream,outputStream);
        }catch (IOException e){
            throw e;
        }
    }

}

