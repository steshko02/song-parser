package com.epam.service;



import com.epam.model.resource.tempResource.TempResourceWithInputStream;
import com.epam.model.resource.threshold.ThresholdBasedTempResourceFactory;
import com.epam.service.interfaces.CompressionOperation;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCompressionOperations implements CompressionOperation {

    public InputStream compressInputStream(InputStream inputStreamToCompress) throws IOException {

        return  new TempResourceWithInputStream(ThresholdBasedTempResourceFactory.defaults().createTempResource(
                outputStream -> {
                    try(GZIPOutputStream gzipOutputStream = new GZIPOutputStream(new BufferedOutputStream(outputStream))){
                        IOUtils.copy(inputStreamToCompress,gzipOutputStream);
                    }
                }
        ));
    }

    @Override
    public OutputStream compressOutputStream(OutputStream outputStreamToCompress) throws IOException {
        return new GZIPOutputStream(new BufferedOutputStream(outputStreamToCompress));
    }

    @Override
    public InputStream decompressInputStream(InputStream inputStreamToDecompress) throws IOException {
        return new GZIPInputStream(inputStreamToDecompress);
    }
}
