/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

/**
 * @author tuantm18
 */
public class PassWordUtil {
    private static PassWordUtil instance;
    public static final Logger logger = Logger.getLogger(PassWordUtil.class);

    private PassWordUtil() {
    }

    public synchronized String encrypt(String plaintext) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1"); //step 2
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            if (md != null) {
                md.update(plaintext.getBytes("UTF-8")); //step 3
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        byte raw[] = null;
        if (md != null) {
            raw = md.digest(); //step 4
        }
        String hash = (new BASE64Encoder()).encode(raw); //step 5
        return hash; //step 6
    }

    public static synchronized PassWordUtil getInstance() //step 1
    {
        if (instance == null) {
            instance = new PassWordUtil();
        }
        return instance;
    }
}
