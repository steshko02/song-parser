package com.epam.model.resource.threshold;

import com.epam.model.resource.tempResource.ByteArrayTempResource;
import com.epam.model.resource.tempResource.FileTempResource;
import com.epam.model.resource.tempResource.TempResource;
import com.epam.model.resource.tempResource.TempResourceFactory;
import com.epam.model.resource.tempResource.tempfactory.ByteArrayTempResourceFactory;
import com.epam.model.resource.tempResource.tempfactory.FileTempFactory;

import java.io.*;
import java.nio.file.Files;

public class ThresholdBasedTempResourceFactory implements TempResourceFactory {

   private  final  TempResourceFactory memoryResourceFactory;

   private  final  TempResourceFactory fileResourceFactory;

   private  final  Long threshold;

    public ThresholdBasedTempResourceFactory(TempResourceFactory memoryResourceFactory, TempResourceFactory fileResourceFactory, Long thresholder) {
        this.memoryResourceFactory = memoryResourceFactory;
        this.fileResourceFactory = fileResourceFactory;
        this.threshold = thresholder;
    }

    public   static  ThresholdBasedTempResourceFactory defaults(){
        return new ThresholdBasedTempResourceFactory(new ByteArrayTempResourceFactory(),
                new FileTempFactory(),
                (long) (32768 * 16));
    }

    public  ThresholdBasedTempResourceFactory useThreshold(Long threshold){
        return  new ThresholdBasedTempResourceFactory(this.memoryResourceFactory,this.fileResourceFactory, threshold);
    }

//    public ThresholdBasedTempResourceFactory create
//
    @Override
    public TempResource createTempResource(InputStream inputStream) throws IOException {

        ThresholdBaseInputStream thresholdBaseInputStream =
                new ThresholdBaseInputStream(threshold, inputStream);
        TempResource tempResource = memoryResourceFactory.createTempResource(thresholdBaseInputStream);
        if (thresholdBaseInputStream.isThresholdReached()){
            tempResource = fileResourceFactory.createTempResource(
                    new SequenceInputStream(tempResource.getInputStream(),inputStream));
        }
        return  tempResource;
    }

    @Override
    public TempResource createTempResource(TempResourceWriter tempResourceWriter) throws IOException {
        File tmpFile = File.createTempFile(
                FileTempFactory.FILE_PREFIX,
                FileTempFactory.FILE_POSTFIX);
        boolean isThresholdReached = false;
        ByteArrayOutputStream memoryOS = new ByteArrayOutputStream();
        try(ThresholdBasedOutputStream os =
                    new ThresholdBasedOutputStream( memoryOS,
                            new BufferedOutputStream(new FileOutputStream(tmpFile)),
                            threshold)){
                        tempResourceWriter.write(os);
                        isThresholdReached = os.isThresholdReached();
                    }catch (IOException e) {
            deleteTempFile(tmpFile);
        }
        if(isThresholdReached){
            return  new FileTempResource(tmpFile);
        } else {
            deleteTempFile(tmpFile);
            return new ByteArrayTempResource(memoryOS.toByteArray());
        }

    }

    private void deleteTempFile(File tmpFile) {
        try {
            Files.delete(tmpFile.toPath());
        } catch (IOException e) {
            //залогить
        }
    }
}
