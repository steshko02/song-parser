package com.epam.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

@UtilityClass
public class CheckSumImpl  {

    public static String calculate(InputStream stream, MessageDigest md) throws IOException {
        try (
                var fis = stream;
                var bis = new BufferedInputStream(fis);
                var dis = new DigestInputStream(bis, md)
        ) {
            while (dis.read() != -1) ;
            md = dis.getMessageDigest();
        }
        var result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static boolean check(String storageFile, String resourceFile) throws IOException {
        return storageFile.equals(resourceFile);
    }


    public static String create(MessageDigest md) {
        StringBuilder res = new StringBuilder();
        for (byte b : md.digest()) {
            res.append(String.format("%02x", b));
        }
        return  res.toString();
    }
}
