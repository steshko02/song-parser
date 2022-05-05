package com.epam.model.resource.tempResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ByteArrayTempResource implements TempResource {

    private  final  byte[] content;

    public ByteArrayTempResource(byte[] content) {
        this.content = content;
    }

    @Override
    public InputStream getInputStream() {
        return new SizeAwareInputStream(Long.valueOf(content.length), new ByteArrayInputStream(content));
    }

    @Override
    public ReadableByteChannel getChannel() {
        return Channels.newChannel(getInputStream());
    }

    @Override
    public Long getSize() {
        return Long.valueOf(content.length);
    }

    @Override
    public void close() {
        //уберется гарбчколлектором
    }
}
