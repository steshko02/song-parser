package com.epam.model.resource.tempResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Deprecated
public class CompositeInputStream extends InputStream {


    private final InputStream[] streams;

    private int currentStreamIndex = 0;

    public CompositeInputStream(InputStream[] streams) {
        this.streams = streams;
    }

    public CompositeInputStream(List<InputStream> streams) {
        this.streams = streams.toArray(new InputStream[streams.size()]);
    }

    @Override
    public int read() throws IOException {
        int data = streams[currentStreamIndex].read();
        if (data == -1 && currentStreamIndex < streams.length - 1) {
            currentStreamIndex++;
            return read();
        }
        return data;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = streams[currentStreamIndex].read(b, off, len);
        if (read == -1 && currentStreamIndex < streams.length - 1) {
            currentStreamIndex++;
            return read(b, off, len);
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        for (InputStream stream : streams) {
            stream.close();
        }
    }

}
