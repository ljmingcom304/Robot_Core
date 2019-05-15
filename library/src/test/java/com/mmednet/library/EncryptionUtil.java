package com.mmednet.library;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * @Description: 加密工具类，包含DES，AES，DESede
 * @author: lixu
 * @create: 2018/11/23
 */
public class EncryptionUtil {

    private static final int keysizeDES = 56;
    private static final int keysizeAES = 128;
    private static final int keysizeDESede = 168;

    private EncryptionUtil() {}


    /**
     * 进行加密
     * res 数据
     * key 加密钥匙
     * method 加密算法（包含DES，AES，DESede）
     * charset 编码格式，默认null为UTF-8
     */
    public static String encode(String res, String key,String method,String charset) {
        return keyGeneratorES(res, method, key, true,charset);
    }

    /**
     *  进行解密
     * res 数据
     * key 加密钥匙
     * method 加密算法（包含DES，AES，DESede）
     * charset 编码格式，默认null为UTF-8
     */
    public static String decode(String res, String key,String method,String charset) {
        return keyGeneratorES(res, method, key, false,charset);
    }

    // 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
    private static String keyGeneratorES(String res, String method, String key, boolean isEncode,String charset) {

        int keysize = 0;
        if("DES".equals(method)){
            keysize = keysizeDES;
        }else if("AES".equals(method)){
            keysize = keysizeAES;
        }else if("DESede".equals(method)){
            keysize = keysizeDESede;
        }
        if(charset == null){
            charset = "UTF-8";
        }

        try {
            KeyGenerator kg = KeyGenerator.getInstance(method);
            if (keysize == 0) {
                byte[] keyBytes = key.getBytes(charset);
                kg.init(new SecureRandom(keyBytes));
            } else if (key == null) {
                kg.init(keysize);
            } else {
                byte[] keyBytes = key.getBytes(charset);
                kg.init(keysize, new SecureRandom(keyBytes));
            }
            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), method);
            Cipher cipher = Cipher.getInstance(method);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(parseHexStr2Byte(res)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将二进制转换成16进制
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // 将16进制转换为二进制
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
