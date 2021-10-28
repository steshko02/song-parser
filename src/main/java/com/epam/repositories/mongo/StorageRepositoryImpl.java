package com.epam.repositories.mongo;

import com.epam.model.entity.StorageType;
import com.epam.model.storage.Storage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class StorageRepositoryImpl implements StorageRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Storage getStorage(StorageType storageType) {
        Query q = new Query();
        q.addCriteria(Criteria.where("storageType").is(storageType));
        return mongoTemplate.findOne(q, Storage.class,"storages");
    }

    @Override
    public Storage getStorageById(String id) {
        Query query = new Query(Criteria.where("_id").is(convertToObjectId(id)));
        return mongoTemplate.findOne(query, Storage.class,"storages");
    }

    private Object convertToObjectId(String  id) {
        if (id instanceof String && ObjectId.isValid(id)) {
            return new ObjectId(id);
        }
        return id;
    }
    @Override
    public void saveStorage(Storage storage) {
        mongoTemplate.save(storage,"storages");
    }
}
