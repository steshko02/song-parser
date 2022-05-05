package com.epam.model.resource;

import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public interface ResourceObj {
    InputStream read() throws IOException;
    void save(InputStream stream) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    void setStorageId(String storageId);
    String getId();
    String getStorageId();
    String getPath();
    void setPath(String path);
    Class<? extends ResourceObj>  supports();
    String getFileName();

    void  save(ContentConsumer contentConsumer) throws IOException;

    void delete();

    @FunctionalInterface
    interface ContentConsumer {
        void writeContent(OutputStream outputStream) throws IOException;
    }


}
