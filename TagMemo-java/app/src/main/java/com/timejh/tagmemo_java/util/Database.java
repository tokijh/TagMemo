package com.timejh.tagmemo_java.util;

import java.security.MessageDigest;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

/**
 * Created by tokijh on 2017. 4. 28..
 */

public class Database {
    public static String createID(String keyname) {
        try {
            String before = getCurrentDate() + keyname;
            Random random = new Random();

            int randcount = random.nextInt(10);
            for (int count = 0; count < randcount; count++) {
                before = before + ((char) ((random.nextBoolean() ? 'A' : 'a') + random.nextInt(24)));
            }
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(before.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String createID(Class clazz) {
        return createID(clazz.toString());
    }

    public static String getCurrentDate() {
        return currentTimeMillis() + "";
    }
}
