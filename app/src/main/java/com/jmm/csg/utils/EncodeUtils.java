package com.jmm.csg.utils;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncodeUtils {

    private static byte[] getRawKey(byte[] seed) {
        byte[] rawKey = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed);
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            rawKey = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
        }
        return rawKey;
    }

    public static byte[] encrypt(String key, byte[] src) {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(key.getBytes()), "AES");
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String key, byte[] encrypted) {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(key.getBytes()), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
