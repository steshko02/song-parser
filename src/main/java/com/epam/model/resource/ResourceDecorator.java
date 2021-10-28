package com.epam.model.resource;

import io.minio.errors.*;
import lombok.Data;
import com.epam.model.entity.StorageType;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Data
public abstract class ResourceDecorator  implements ResourceObj {


    private StorageType storageType;

    private ResourceObj resourceObj;

    public ResourceDecorator(ResourceObj resourceObj) {
        this.resourceObj = resourceObj;
    }

    public ResourceDecorator(StorageType storageType) {
        this.storageType = storageType;
    }

    @Override
    public InputStream read() throws IOException {
        return this.resourceObj.read();
    }

    public ResourceDecorator() {
    }

    @Override
    public void save(InputStream stream) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        resourceObj.save(stream);
    }
    @Override
    public void setStorageId(String storageId){
        resourceObj.setStorageId(storageId);
    }
    @Override
    public String getId(){
        return resourceObj.getId();
    }
    @Override
    public String getStorageId(){
        return  resourceObj.getStorageId();
    }
    @Override
    public String getPath(){
        return resourceObj.getPath();
    }
    @Override
    public    void delete(){
        resourceObj.delete();
    }
    @Override
    public  void setPath(String path){
        resourceObj.setPath(path);
    }
    @Override
    public Class<? extends ResourceObj>  supports(){
        return resourceObj.supports();
    }
    @Override
    public String getFileName(){
        return resourceObj.getFileName();
    }
}
