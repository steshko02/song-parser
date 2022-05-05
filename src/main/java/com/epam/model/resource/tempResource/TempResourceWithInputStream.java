package com.epam.model.resource.tempResource;

import java.io.FilterInputStream;
import java.io.IOException;

public class TempResourceWithInputStream extends FilterInputStream {

    private  final  TempResource tempResource;

    public TempResourceWithInputStream(TempResource tempResource) {
        super(tempResource.getInputStream());
        this.tempResource = tempResource;
    }

    @Override
    public void close() throws IOException{
        super.close();
        tempResource.close();
    }
}
