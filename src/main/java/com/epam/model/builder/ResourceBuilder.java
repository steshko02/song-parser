package com.epam.model.builder;

import com.epam.model.resource.ResourceObj;

public interface ResourceBuilder {
  ResourceBuilder   withCompression();
  ResourceObj build();
}
