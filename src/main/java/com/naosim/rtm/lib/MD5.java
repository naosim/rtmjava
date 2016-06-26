package com.naosim.rtm.lib;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {
    public static String md5(String str) {
        try {
            byte[] str_bytes = str.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5_bytes = md.digest(str_bytes);
            BigInteger big_int = new BigInteger(1, md5_bytes);
            return big_int.toString(16);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
