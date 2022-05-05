package com.epam.model.resource.tempResource;

import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;

public interface TempResource extends AutoCloseable{
    InputStream getInputStream();

    ReadableByteChannel getChannel();

    Long getSize();

    @Override
    void close();
}
