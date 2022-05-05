package com.epam.model.resource.tempResource;

import com.epam.model.resource.tempResource.TempResource;
import org.hibernate.result.Output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface TempResourceFactory {
    TempResource createTempResource(InputStream inputStream) throws IOException;

    TempResource createTempResource(TempResourceWriter tempResourceWriter) throws IOException;

    @FunctionalInterface
    public interface TempResourceWriter{
        void  write(OutputStream outputStream) throws IOException;
    }
}
