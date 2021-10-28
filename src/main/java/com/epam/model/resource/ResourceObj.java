package com.epam.model.resource;

import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public interface ResourceObj {
    InputStream read() throws IOException;
    void save(InputStream stream) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    void setStorageId(String storageId);
    String getId();
    String getStorageId();
    String getPath();
    void delete();
    void setPath(String path);
    Class<? extends ResourceObj>  supports();
    String getFileName();
}
