package com.flow.manager.utils;

import java.util.Random;

public class Utils {

    public static String generateRandomString() {
        return generateRandomString(20);
    }

    public static String generateRandomString(int length){

    char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    StringBuilder sb = new StringBuilder(length);
    Random random = new Random();
    for (int i = 0; i < length; i++) {
        char c = chars[random.nextInt(chars.length)];
        sb.append(c);
    }
    return sb.toString();
    }
}
