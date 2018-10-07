package com.chen4393c.vicinity.utils;

import com.google.android.gms.common.util.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class SecurityUtils {
    public static String md5Encryption(final String input) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes(Charset.forName("UTF8")));
            byte[] resultByte = messageDigest.digest();
            result = Hex.bytesToStringUppercase(resultByte);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
