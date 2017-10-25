package com.viettel.fw.security;


import com.viettel.fw.security.ConverterUtil;
import org.apache.commons.configuration.Configuration;
import java.io.UnsupportedEncodingException;
import org.apache.commons.configuration.PropertiesConfiguration;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

/**
 * Created by thienlt on 08/05/2016.
 */

public class EncryptDecryptUtils {
    public SecretKey getKeyFileAES(String fileConfig) throws Exception {
        Configuration bundle = new PropertiesConfiguration(fileConfig);
        String pathFull = "";
        if (bundle != null) {
            pathFull = bundle.getString("key");
        }
        SecretKey securKey = ConverterUtil.createSecretKey(pathFull);
        return securKey;
    }

    public String encryptByAES(String fileConfig, String strNeedEnCrypted) throws Exception {
        SecretKey securKey = getKeyFileAES(fileConfig);
        return ConverterUtil.aesEncrypt(securKey, strNeedEnCrypted);
    }

    public String decryptbyAES(String fileConfig, String strNeedDeCrypted) throws Exception {
        SecretKey securKey = getKeyFileAES(fileConfig);
        return ConverterUtil.aesDecrypt(securKey, strNeedDeCrypted);
    }

//    public static void main(String[] args) {
//        try{
//            SecretKey securKey = ConverterUtil.createSecretKey("60ADB92C65185DE565064EDE967FAC0D");
//            String out  = ConverterUtil.aesEncrypt(securKey, "thien");
//            System.out.println(out);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String MD5(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }


    public static String randomstring(int lo, int hi) {
        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('a', 'z');
        }
        return new String(b, 0);
    }

    private static int rand(int lo, int hi) {
        java.util.Random rn = new java.util.Random();
        int n = hi - lo + 1;
        int i = rn.nextInt(n);
        if (i < 0) {
            i = -i;
        }
        return lo + i;
    }

    public static String randomstring() {
        return randomstring(8, 8);
    }

}

