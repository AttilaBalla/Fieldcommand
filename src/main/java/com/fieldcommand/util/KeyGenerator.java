package com.fieldcommand.util;

import java.util.Random;

public class KeyGenerator {

    public static String generateKey() {
        Random random = new Random();
        char[] word = new char[16];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(26));
        }
        return new String(word);
    }
}
