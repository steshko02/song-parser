package com.epam.model.storage;

import lombok.Data;
import com.epam.model.builder.ResourceBuilder;
import com.epam.model.builder.ResourceBuilderImpl;
import com.epam.model.entity.StorageType;
import com.epam.model.resource.CloudStorageEntity;
import com.epam.model.resource.ResourceObj;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@TypeAlias("MS")
public class MinioStorage implements Storage{

    @Id
    private String id;
    private  String  bucket;
    private StorageType storageType  = StorageType.CLOUD_SYSTEM;

    private String URL ;
    private String accessKey;
    private  String secretKey;

    public MinioStorage(String id,String bucket,StorageType storageType) {
        this.setBucket(bucket);
        this.setId(id);
        this.setStorageType(storageType);
    }

    public MinioStorage(String id,String bucket, StorageType storageType, String URL, String accessKey, String secretKey) {
        this.setId(id);
        this.bucket = bucket;
        this.storageType = storageType;
        this.URL = URL;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public MinioStorage() {
    }

    @Override
    public StorageType getType() {
        return this.storageType;
    }

    @Override
    public ResourceObj createNewResource() {
        return new CloudStorageEntity(this.id,this.URL,this.accessKey,this.secretKey,this.bucket);
    }

    @Override
    public ResourceBuilder requestBuilder() {
        return new ResourceBuilderImpl(createNewResource());
    }
}
