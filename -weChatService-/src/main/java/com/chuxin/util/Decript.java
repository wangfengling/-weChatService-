package com.chuxin.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzhen on 2017/6/30.
 */
public class Decript {
    public static boolean initialized = false;

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            //create Hex String
            String hexString = toHexString(messageDigest);
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
       return "";
    }

    public static String MD5(Map<String,Object> map,String key){
        if (map.size() < 0){
            return "";
        }
        String results = "";
        List<String> list = new ArrayList<String>(map.keySet());
        Collections.sort(list);
        for (String str : list){
            String str1 = str+"="+map.get(str)+"&";
            results = results + str1;
        }
        String result;
        if (key == null || key.equals("")){
            result = results.substring(0,results.length()-1);
        }else {
            result = results.substring(0,results.length()-1)+"&key="+key;
        }
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte messageDigest[] = digest.digest(result.getBytes("UTF-8"));
            //create Hex String
            String hexString = toHexString(messageDigest);
            return hexString;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toHexString(byte[] messageDigest){
        StringBuffer hexString = new StringBuffer();
        for(int i = 0; i < messageDigest.length; i++){
            String shaHex = Integer.toHexString((messageDigest[i] & 0xFF) | 0x100).substring(1, 3);
            if (shaHex.length() < 2){
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

    public static String Aes256Decode(byte[] cryptograph, byte[] sKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(sKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(cryptograph));

    }



}
