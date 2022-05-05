package com.epam.model.resource.tempResource.tempfactory;

import com.epam.model.resource.tempResource.ByteArrayTempResource;
import com.epam.model.resource.tempResource.TempResource;
import com.epam.model.resource.tempResource.TempResourceFactory;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayTempResourceFactory implements TempResourceFactory {
    @Override
    public TempResource createTempResource(InputStream inputStream) throws IOException {
        return new ByteArrayTempResource(ByteStreams.toByteArray(inputStream));
    }

    @Override
    public TempResource createTempResource(TempResourceWriter tempResourceWriter)  throws IOException{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            tempResourceWriter.write(outputStream);
            return new ByteArrayTempResource(outputStream.toByteArray());
        }

}
