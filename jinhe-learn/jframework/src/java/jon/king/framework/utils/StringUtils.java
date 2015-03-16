package jon.king.framework.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import jon.king.framework.excption.FrameworkException;

import org.apache.log4j.Logger;

/**
 * @author 金普俊 2006-6-19
 * 
 * 加密字符串
 */
public class StringUtils {

    private final static Logger log = Logger.getLogger(StringUtils.class);

    /**
     * 根据指定的加密方式实现单向加密，加密方式有"MD5","SHA-1"
     * 
     * @param password
     * @param algorithm
     *            "MD5"/"SHA-1"等
     * 
     * @return 加密的password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();//getBytes("UTF-8"));

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("用 " + algorithm + " 加密时出错，Exception: " + e);
            return password;
        }
        md.reset();
        md.update(unencodedPassword);

        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * Encode a string using Base64 encoding. Used when storing passwords as
     * cookies.
     * 
     * 这是一种弱加密方式，任何人都可以用BASE64Decoder进行解密
     * 
     * @param str
     * @return String
     */
    public static String encodeString(String str) {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encodeBuffer(str.getBytes()).trim();
    }   

    /**
     * Decode a string using Base64 encoding.
     * 
     * @param str
     * @return String
     */
    public static String decodeString(String str) {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(str));
        } catch (IOException ioe) {
            throw new FrameworkException(ioe.getMessage(), ioe.getCause());
        }
    }
    
    /**
     * Decode a string using URLEncoder.
     * @param str
     * @param charSet
     * @return
     */
    public static String encodeString(String str, String charSet) {
        try {
            return URLEncoder.encode(str, charSet);
        } catch (UnsupportedEncodingException e) {
            throw new FrameworkException(e.getMessage(), e.getCause());
        }
    }
    
    public static String decodeString(String str, String charSet) {
        try {
            return URLDecoder.decode(str, charSet);
        } catch (UnsupportedEncodingException e) {
            throw new FrameworkException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 非对称加密DSA数字签名通过使用密钥对来实现。
     * 根据非对称加密的原理，分发公钥并用其加密，私钥保密，用于解密。
     * 而DSA的数字签名则是利用私钥加密，公钥解密，用以保证不可否认性和完整性。
     * 以DSA数字签名为例：首先需要生成一对密钥： 
     * 
     * @param key
     */
    public static void generateKeyPairByDSA(String key) {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("DSA");

            SecureRandom sr = new SecureRandom();
            sr.setSeed(key.getBytes());// 密钥种子
            keygen.initialize(512, sr);

            KeyPair keys = keygen.generateKeyPair();
            PublicKey publicKey = keys.getPublic();
            PrivateKey privateKey = keys.getPrivate();

            // 将生成的密钥对序列化到文件
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("src/config/resources/temp/prikey.dat"));
            out.writeObject(privateKey);
            out.close();

            out = new ObjectOutputStream(new FileOutputStream(
                    "src/config/resources/temp/pubkey.dat"));
            out.writeObject(publicKey);
            out.close();
        } catch (Exception e) {
            log.error("用 DSA 生成公钥，密钥对时出错，Exception: " + e);
        }
    }

}
