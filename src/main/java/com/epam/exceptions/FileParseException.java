package com.epam.exceptions;



public class FileParseException extends Exception{

    public FileParseException(Class clazz) {
        super(FileParseException.generateMessage(clazz.getSimpleName()));
    }

    private static String generateMessage(String classname) {
        return  classname + " failed parse file";
    }
}
