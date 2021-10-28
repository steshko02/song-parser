package com.epam.service.interfaces;

import io.minio.errors.*;
import com.epam.model.entity.StorageType;
import com.epam.model.resource.ResourceObj;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface StorageService<T extends ResourceObj> {

    String store(InputStream entity) throws IOException, NoSuchAlgorithmException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, XmlParserException, ErrorResponseException;

    List<String> loadAll();

    Path load(String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;

    Resource loadAsResource(String filename) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException;

    void deleteAll() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;

    T create(String cs,String filename,long size);

    Resource getResource(String filename) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InsufficientDataException, ErrorResponseException;

    boolean supports(StorageType storageType);
    boolean supports(Class<? extends ResourceObj> clazz);

    Class<? extends ResourceObj> getSupportsClass();

}