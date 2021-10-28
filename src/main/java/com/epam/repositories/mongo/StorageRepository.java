package com.epam.repositories.mongo;


import com.epam.model.entity.StorageType;
import com.epam.model.storage.Storage;

public interface StorageRepository {

    Storage getStorage(StorageType storageType);
    void  saveStorage(Storage storage);
    Storage getStorageById(String id);
}
