package com.epam.repositories.mongo;


import com.epam.model.entity.StorageType;
import com.epam.model.resource.ResourceObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResourceObjRepository implements ResourceRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResourceObj getResource(StorageType storageType ){
        Query q = new Query();
        q.addCriteria(Criteria.where("storageType").is(storageType));
      return mongoTemplate.findOne(q, ResourceObj.class,"res_song");
    }

    @Override
    public ResourceObj saveResource(ResourceObj resourceObj) {
        mongoTemplate.save(resourceObj,"res_song");
        return resourceObj;
    }

    @Override
    public ResourceObj getResourceById(String id) {
        Query query = new Query(Criteria.where("_id").is(convertToObjectId(id)));
        return mongoTemplate.findOne(query, ResourceObj.class,"res_song");
    }

   private Object convertToObjectId(String  id) {
        if (id instanceof String && ObjectId.isValid(id)) {
            return new ObjectId(id);
        }
        return id;
    }

   @Override
   public  List<ResourceObj> getByStorageId(String  storageId){
       Query q = new Query();
       Criteria criteria = new Criteria();
       q.addCriteria(criteria.orOperator(Criteria.where("delegate.storageId").is(storageId),Criteria.where("storageId").is(storageId)));
       return mongoTemplate.find(q, ResourceObj.class,"res_song");
   }
}