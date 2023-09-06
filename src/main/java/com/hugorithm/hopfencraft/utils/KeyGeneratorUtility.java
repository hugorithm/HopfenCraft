package com.hugorithm.hopfencraft.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGeneratorUtility {
    public static KeyPair generateRsaKey(){
        KeyPair kp;

        try {
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
            kpGen.initialize(2048);
            kp = kpGen.generateKeyPair();
        } catch (Exception e){
            throw new IllegalStateException();
        }

        return kp;
    }
}
