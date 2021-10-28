package com.epam.service.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface CompressionOperation {
    InputStream compressInputStream(InputStream inputStreamToCompress) throws IOException;

    OutputStream compressOutputStream(OutputStream outputStreamToCompress) throws IOException;

    InputStream decompressInputStream(InputStream inputStreamToDecompress) throws IOException;
}
