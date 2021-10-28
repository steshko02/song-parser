package com.epam.service;



import com.epam.service.interfaces.CompressionOperation;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCompressionOperations implements CompressionOperation {

    public InputStream compressInputStream(InputStream inputStreamToCompress) {
        final PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        try {
            pis.connect(pos);

            Thread thread = new Thread() {
                public void run () {
                    startWriting(pos, inputStreamToCompress);
                }
            };
            thread.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return pis;
    }

    public void startWriting(OutputStream out, InputStream in) {
        try (GZIPOutputStream gOut = new GZIPOutputStream(out)) {
            byte[] buffer = new byte[10240];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                gOut.write(buffer, 0, len);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch( Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public OutputStream compressOutputStream(OutputStream outputStreamToCompress) throws IOException {
        return new GZIPOutputStream(new BufferedOutputStream(outputStreamToCompress));
    }

    @Override
    public InputStream decompressInputStream(InputStream inputStreamToDecompress)
            throws IOException {
        return new GZIPInputStream(inputStreamToDecompress);
    }
}
