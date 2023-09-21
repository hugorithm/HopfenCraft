package com.hugorithm.hopfencraft.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

@Getter
@Setter
@Component
public class RSAKeyProperties {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    @Autowired
    public RSAKeyProperties(@Value("${jwt.rsa.public.modulus}") String publicModulus,
                            @Value("${jwt.rsa.private.modulus}") String privateModulus,
                            @Value("${jwt.rsa.public.exponent}") String publicExponent,
                            @Value("${jwt.rsa.private.exponent}") String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(publicModulus), new BigInteger(publicExponent));
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(privateModulus), new BigInteger(privateExponent));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        this.publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }
}
