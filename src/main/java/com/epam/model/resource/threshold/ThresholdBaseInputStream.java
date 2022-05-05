package com.epam.model.resource.threshold;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ThresholdBaseInputStream  extends InputStream {

    private  Long threshold;

    private Long counter = 0L;


    private  boolean thresholdReached = false;

    private  InputStream delegate;

    public ThresholdBaseInputStream(Long threshold, InputStream delegate) {
        this.threshold = threshold;
        this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
        if(++counter>threshold || threshold ==0){
            thresholdReached = true;
            return  -1;
        }else {
            return delegate.read();
        }
    }

    @Override
    public  void  close() throws  IOException{
        super.close();
        delegate.close();
    }

    boolean isThresholdReached(){
        return thresholdReached;
    }
}
