package com.epam.model.builder;


import com.epam.model.resource.CompressionResource;
import com.epam.model.resource.ResourceObj;

public class ResourceBuilderImpl implements ResourceBuilder{

    private ResourceObj resourceObj;

    public ResourceBuilderImpl(ResourceObj resourceObj) {
        this.resourceObj = resourceObj;
    }

    @Override
    public ResourceBuilder withCompression() {
        CompressionResource compressionResource = new CompressionResource(resourceObj);
        this.resourceObj = compressionResource;
        return  this;
    }

    @Override
    public ResourceObj build() {
        return resourceObj;
    }
}
