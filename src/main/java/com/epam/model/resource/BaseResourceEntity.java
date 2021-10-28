package com.epam.model.resource;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class BaseResourceEntity {
    private String id;

}
