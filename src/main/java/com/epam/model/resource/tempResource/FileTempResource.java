package com.epam.model.resource.tempResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;

public class FileTempResource implements TempResource{

    private  File tempFile;

    public FileTempResource(File tempFile){
        this.tempFile = tempFile;
    }

    @Override
    public InputStream getInputStream() {
        return new SizeAwareInputStream(tempFile.length(), Channels.newInputStream(getChannel()));
    }

    @Override
    public ReadableByteChannel getChannel() {
        try {
             return FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
        }catch (IOException e){
            throw new UncheckedIOException("Exception while read file:" + tempFile,e);
        }
    }

    @Override
    public Long getSize() {
        return tempFile.length();
    }

    @Override
    public void close() {

    }
}
