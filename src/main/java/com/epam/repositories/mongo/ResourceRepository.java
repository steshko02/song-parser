package com.epam.repositories.mongo;

import com.epam.model.entity.StorageType;
import com.epam.model.resource.ResourceObj;

import java.util.List;

public interface ResourceRepository {
    ResourceObj getResource(StorageType storageType );
    ResourceObj saveResource(ResourceObj resourceObj);
    ResourceObj getResourceById(String id);
    List<ResourceObj> getByStorageId(String  storageId);
}
