package com.epam.service.interfaces;


import com.epam.model.entity.StorageType;
import com.epam.model.resource.ResourceObj;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface CreateResource<T> {
     void createFiles(ResourceObj inputStream, String filename)  throws NoSuchAlgorithmException, IOException;
     boolean supports(StorageType storageType);
}
