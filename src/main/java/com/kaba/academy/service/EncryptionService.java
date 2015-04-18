package com.kaba.academy.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

@Service
public class EncryptionService {

    public final int DEFAULT_KEY_SIZE = 16; // 128 bit key
    public final String systemKey = "7&hdjkm52bJ!37";

    public String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(getHash(systemKey));
            SecretKeySpec skeySpec = new SecretKeySpec(getHash(key), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(String key, String encrypted) {
        if (encrypted == null) {
            return null;
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(getHash(systemKey));
            SecretKeySpec skeySpec = new SecretKeySpec(getHash(key), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getHash(String key) {
        if (StringUtils.isNotBlank(key)) {
            String ret = DigestUtils.sha1Hex(key);
            try {
                return ret.substring(0, DEFAULT_KEY_SIZE).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        throw new PasswordNotFoundException();
    }
}