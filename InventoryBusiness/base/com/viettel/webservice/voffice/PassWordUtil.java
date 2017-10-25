/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.webservice.voffice;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;

/**
 *
 * @author tuantm18
 */
public class PassWordUtil {
    private static PassWordUtil instance;

  private PassWordUtil()
  {
  }

  public synchronized String encrypt(String plaintext) throws Exception
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA-1"); //step 2
    }
    catch(NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    try
    {
      md.update(plaintext.getBytes("UTF-8")); //step 3
    }
    catch(UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }

    byte raw[] = md.digest(); //step 4
    String hash = (new BASE64Encoder()).encode(raw); //step 5
    return hash; //step 6
  }
  
  public static synchronized PassWordUtil getInstance() //step 1
  {
    if(instance == null)
    {
       instance = new PassWordUtil(); 
    } 
    return instance;
  }
}
