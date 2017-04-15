package com.minivision.camaraplat.config;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodeConfig {
   public static String EncodeByMd5(String str){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            byte[] digest = messageDigest.digest(str.getBytes("UTF-8"));
            String newStr = new BASE64Encoder().encode(digest);
            return newStr;
        }catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
