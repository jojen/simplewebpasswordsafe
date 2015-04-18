package com.kaba.academy.service;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

/**
 * Created by jochen on 4/16/15.
 */
public class EncryptionServiceTest {

    public static final String DEFAULT_USER_KEY = "fsdu(ud82$3&%6dka";
    private EncryptionService encryptionService = new EncryptionService();

    @Test
    public void assertEncryption() {
        String secretText = "This is secret";
        String encryptedText = encryptionService.encrypt(DEFAULT_USER_KEY, secretText);
        assertNotSame(secretText, encryptedText);
        assertEquals(secretText, encryptionService.decrypt(DEFAULT_USER_KEY, encryptedText));

    }
}
