package com.hugorithm.hopfencraft.utils;

import com.hugorithm.hopfencraft.exception.auth.RsaKeyGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorUtility {
    private final static Logger LOGGER = LoggerFactory.getLogger(KeyGeneratorUtility.class);

    public static KeyPair generateRsaKey() {
        KeyPair kp;

        try {
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
            kpGen.initialize(2048);
            kp = kpGen.generateKeyPair();

        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new RsaKeyGenerationException("Failed to generate RSA key pair due to NoSuchAlgorithmException");
        }

        return kp;
    }
}
