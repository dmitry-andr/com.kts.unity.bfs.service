package com.kts.unity.config.web.backend.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CypherUtility {

    public static String getMD5HexFromString(String textToConvert) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(textToConvert.getBytes());
            byte byteData[] = md.digest();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        return sb.toString();
    }
}
