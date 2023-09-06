package com.hugorithm.hopfencraft.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Getter
@Setter
@Component
public class RSAKeyProperties {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProperties(){
        KeyPair kp = KeyGeneratorUtility.generateRsaKey();
        this.publicKey = (RSAPublicKey) kp.getPublic();
        this.privateKey = (RSAPrivateKey) kp.getPrivate();
    }
}
