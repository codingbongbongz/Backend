package com.swm.cbz.factory;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class TokenFactory {
    public static String generateAppleToken() {
        try {
            return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, "kid")
                .setIssuer("iss")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (3 * 60 * 1000)))
                .setAudience("aud")
                .setSubject("sub")
                .signWith(
                    SignatureAlgorithm.ES256,getPrivateKey()
                )
                .compact();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error converting private key from String", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }

    private static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] decodedKey = Base64.decodeBase64("sig");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");

        return keyFactory.generatePrivate(keySpec);

    }

}
