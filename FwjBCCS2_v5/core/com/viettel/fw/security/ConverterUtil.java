//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.viettel.fw.security;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class ConverterUtil {
    public static final String ASCII = "ASCII";
    public static final String AES_METHOD = "AES";
    private static Random random = new Random();

    public ConverterUtil() {
    }

    public static String padleft(String s, int len, char c) throws Exception {
        s = s.trim();
        if(s.length() > len) {
            throw new Exception("invalid len " + s.length() + "/" + len);
        } else {
            StringBuffer d = new StringBuffer(len);
            int fill = len - s.length();

            while(fill-- > 0) {
                d.append(c);
            }

            d.append(s);
            return d.toString();
        }
    }

    public static String zeropad(String s, int len) throws Exception {
        return padleft(s, len, '0');
    }

    public static String strpad(String s, int len) {
        StringBuffer d = new StringBuffer(s);

        while(d.length() < len) {
            d.append(' ');
        }

        return d.toString();
    }

    public static byte[] str2bcd(String s, boolean padLeft) {
        int len = s.length();
        byte[] d = new byte[len + 1 >> 1];
        int start = (len & 1) == 1 && padLeft?1:0;

        for(int i = start; i < len + start; ++i) {
            d[i >> 1] = (byte)(d[i >> 1] | s.charAt(i - start) - 48 << ((i & 1) == 1?0:4));
        }

        return d;
    }

    public static String bcd2str(byte[] b, int offset, int len, boolean padLeft) {
        StringBuffer d = new StringBuffer(len);
        int start = (len & 1) == 1 && padLeft?1:0;

        for(int i = start; i < len + start; ++i) {
            int shift = (i & 1) == 1?0:4;
            d.append(b[offset + (i >> 1)] >> shift & 15);
        }

        return d.toString();
    }

    public static String hexString(byte[] b) {
        StringBuffer d = new StringBuffer(b.length * 2);

        for(int i = 0; i < b.length; ++i) {
            char hi = Character.forDigit(b[i] >> 4 & 15, 16);
            char lo = Character.forDigit(b[i] & 15, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }

        return d.toString();
    }

    public static String bitSet2String(BitSet b) {
        int len = b.size();
        StringBuffer d = new StringBuffer(len);

        for(int i = 0; i < len; ++i) {
            d.append((char)(b.get(i)?'1':'0'));
        }

        return d.toString();
    }

    public static byte[] bitSet2byte(BitSet b) {
        int len = b.size() >> 3 << 3;
        byte[] d = new byte[len >> 3];

        for(int i = 0; i < len; ++i) {
            if(b.get(i + 1)) {
                d[i >> 3] = (byte)(d[i >> 3] | 128 >> i % 8);
            }
        }

        if(len > 64) {
            d[0] = (byte)(d[0] | 128);
        }

        return d;
    }

    public static BitSet byte2BitSet(byte[] b, int offset) {
        int len = (b[offset] & 128) == 128?128:64;
        BitSet bmap = new BitSet(len);

        for(int i = 0; i < len; ++i) {
            if((b[offset + (i >> 3)] & 128 >> i % 8) > 0) {
                bmap.set(i + 1);
            }
        }

        return bmap;
    }

    public static BitSet hex2BitSet(byte[] b, int offset) {
        int len = (Character.digit((char)b[offset], 16) & 8) == 8?128:64;
        BitSet bmap = new BitSet(len);

        for(int i = 0; i < len; ++i) {
            int digit = Character.digit((char)b[offset + (i >> 2)], 16);
            if((digit & 8 >> i % 4) > 0) {
                bmap.set(i + 1);
            }
        }

        return bmap;
    }

    public static byte[] hex2byte(byte[] b) {
        int len = b.length / 2;
        byte[] d = new byte[len];

        for(int i = 0; i < len * 2; ++i) {
            int shift = i % 2 == 1?0:4;
            d[i >> 1] = (byte)(d[i >> 1] | Character.digit((char)b[i], 16) << shift);
        }

        return d;
    }

    public static String formatDouble(double d, int len) {
        String prefix = Long.toString((long)d);
        String suffix = Integer.toString((int)(d * 100.0D % 100.0D));

        try {
            prefix = padleft(prefix, len - 3, ' ');
            suffix = zeropad(suffix, 2);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return prefix + "." + suffix;
    }

    public static String getMD5(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(value.getBytes(), 0, value.length());
        byte[] md5hash = md.digest();
        return hexString(md5hash);
    }

    public static String genChallenge() {
        return Integer.toHexString(random.nextInt());
    }

    public static String getDateString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static String getCurrentDateLongString() {
        return getDateString(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
    }

    public static String getDateLongString(Date date) {
        return getDateString(date, "yyyyMMddHHmmss");
    }

    public static String getDateShortString(Date date) {
        return getDateString(date, "yyyyMMdd");
    }

    public static Date getDateFromLongString(String value) {
        return getDate(value, "yyyyMMddHHmmss");
    }

    public static Date getSmartlinkDate(String value) {
        return getDate(Calendar.getInstance().get(1) + value, "yyyyMMddHHmmss");
    }

    public static String getSmartlinkString(Date date) {
        return getDateString(date, "MMddHHmmss");
    }

    public static Date getDateFromShortString(String value) {
        return getDate(value, "yyyyMMdd");
    }

    public static Date getDate(String value, String dateFormat) {
        try {
            return (new SimpleDateFormat(dateFormat)).parse(value);
        } catch (ParseException var3) {
            return null;
        }
    }

    public static String getCurrentDateShortString() {
        return getDateString(Calendar.getInstance().getTime(), "yyyyMMdd");
    }

    public static byte[] stringToByte(String strVal) {
        byte[] bVal = new byte[strVal.length()];
        char[] chBuffer = strVal.toCharArray();

        for(int i = 0; i < strVal.length(); ++i) {
            bVal[i] = (byte)chBuffer[i];
        }

        return bVal;
    }

    public static String byteToString(byte[] bVal) {
        StringBuffer strBuffer = new StringBuffer();

        for(int i = 0; i < bVal.length; ++i) {
            strBuffer.append((char)bVal[i]);
        }

        return strBuffer.toString();
    }

    public static String aesEncrypt(SecretKey key, String value) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        try {
            byte[] e = value.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, skeySpec);
            byte[] encrypted = cipher.doFinal(e);
            byte[] encoded = Base64.encodeBase64(encrypted);
            return new String(encoded, "ASCII");
        } catch (UnsupportedEncodingException var7) {
            return null;
        }
    }

    public static String base64Encode(String value) {
        try {
            byte[] e = value.getBytes("ASCII");
            return new String(Base64.encodeBase64(e), "ASCII");
        } catch (UnsupportedEncodingException var2) {
            return null;
        }
    }

    public static String base64Encode(byte[] value) {
        try {
            return new String(Base64.encodeBase64(value), "ASCII");
        } catch (UnsupportedEncodingException var2) {
            return null;
        }
    }

    public static String base64Decode(String value) throws Exception {
        try {
            return new String(base64DecodeBytes(value), "ASCII");
        } catch (UnsupportedEncodingException var2) {
            return null;
        }
    }

    public static byte[] base64DecodeBytes(String value) throws Exception {
        try {
            byte[] e = value.getBytes("ASCII");
            if(!Base64.isArrayByteBase64(e)) {
                throw new Exception("input string isn\'t correct base64 format");
            } else {
                return Base64.decodeBase64(e);
            }
        } catch (UnsupportedEncodingException var2) {
            return new byte[0];
        }
    }

    public static String aesDecrypt(SecretKey key, String value) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
        try {
            byte[] e = value.getBytes("ASCII");
            if(!Base64.isArrayByteBase64(e)) {
                throw new Exception("input string to aesDecrypt not valid base64 format");
            } else {
                byte[] planInput = Base64.decodeBase64(e);
                SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(2, skeySpec);
                byte[] decrypted = cipher.doFinal(planInput);
                return new String(decrypted, "ASCII");
            }
        } catch (UnsupportedEncodingException var7) {
            return null;
        }
    }

    public static SecretKey createSecretKey(String raw) {
        return new SecretKeySpec(hex2byte(raw.getBytes()), "AES");
    }

    public static String generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return hexString(raw);
    }

    public static String smax(String value, int maxsize) {
        return value.length() > maxsize?value.substring(0, maxsize - 6) + " [...]":value;
    }

    public static long getFreeSpace(String path) {
        if(path == null) {
            throw new NullPointerException("path cannot be null");
        } else {
            File file = new File(path);
            return !file.exists()?-1L:file.getUsableSpace();
        }
    }
}
