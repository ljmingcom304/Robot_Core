package com.mmednet.library.http.code;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Title:Encrypt
 * <p>
 * Description:加密工具类
 * </p>
 * Author Jming.L
 * Date 2018/11/23 15:09
 */
public class Encrypt {

    /**
     * 进行加密
     * res 数据
     * key 加密钥匙
     * method 加密算法（包含DES，AES，DESede）
     * charset 编码格式，默认null为UTF-8
     */
    public static String encode(String res, String key, String method) {
        return keyGeneratorES(res, method, key, true);
    }

    /**
     * 进行解密
     * res 数据
     * key 加密钥匙
     * method 加密算法（包含DES，AES，DESede）
     * charset 编码格式，默认null为UTF-8
     */
    public static String decode(String res, String key, String method) {
        return keyGeneratorES(res, method, key, false);
    }

    // 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
    private static String keyGeneratorES(String res, String method, String key, boolean isEncode) {
        try {
            String charset = "UTF-8";
            SecretKeySpec sks = new SecretKeySpec(key.getBytes(), method);
            Cipher cipher = Cipher.getInstance(method);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = res.getBytes(charset);
                BASE64Encoder base = new BASE64Encoder();
                return base.encode(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] c = decoder.decodeBuffer(res);
                byte[] doFinal = cipher.doFinal(c);
                return new String(doFinal, charset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


