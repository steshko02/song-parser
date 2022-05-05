package com.epam.model.resource;


import lombok.SneakyThrows;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import com.epam.service.GZIPCompressionOperations;
import com.epam.service.interfaces.CompressionOperation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Document
@TypeAlias("CompressRes")
public class CompressionResource implements ResourceObj {

    private String id;
    @Transient
    private CompressionOperation compressionOperation;

    private  ResourceObj delegate;

    public CompressionResource( ResourceObj delegate) {
       this.delegate = delegate;
        compressionOperation = new GZIPCompressionOperations();
    }

    @SneakyThrows
    @Override
    public void save(InputStream is) throws IOException {
        try (   InputStream isToUse = is;
                InputStream compressedIs = compressionOperation.compressInputStream(isToUse)) {
            delegate.save(compressedIs);
        }
    }

    @Override
    public InputStream read() throws IOException {
        try {
            //return  new BufferedInputStream(delegate.read());
             return compressionOperation.decompressInputStream(delegate.read());
        } catch (IOException e) {
           throw new IOException("failed read InputStream");
        }
    }

    @Override
    public void setStorageId(String storageId) {
        delegate.setStorageId(storageId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getStorageId() {
        return delegate.getStorageId();
    }

    @Override
    public String getPath() {
        return delegate.getPath();
    }

    @Override
    public void delete() {
        delegate.delete();
    }

    @Override
    public void setPath(String path) {
        delegate.setPath(path);
    }

    @Override
    public Class<? extends ResourceObj> supports() {
        return delegate.supports();
    }

    @Override
    public String getFileName() {
        return delegate.getFileName();
    }

    @Override
    public void save(ContentConsumer contentConsumer) throws IOException {
        delegate.save(contentConsumer);
    }
}
