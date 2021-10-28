package com.epam.model.storage;

import com.epam.model.builder.ResourceBuilder;
import com.epam.model.entity.StorageType;
import com.epam.model.resource.ResourceObj;

public interface Storage {
    StorageType getType();
    ResourceObj createNewResource();
    String getId();
    ResourceBuilder requestBuilder();

}
