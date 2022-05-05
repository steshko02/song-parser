package com.epam.model.resource.tempResource;


import java.io.FilterInputStream;
import java.io.InputStream;

public class SizeAwareInputStream extends FilterInputStream {

    private final  Long inputStreamSize;

    protected SizeAwareInputStream(long length, InputStream inputStream) {
        super(inputStream);
        this.inputStreamSize=length;
    }

    public Long getInputStreamSize(){
        return  this.inputStreamSize;
    }
}
