package org.example.utils;


import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import org.example.models.users;

import java.time.ZonedDateTime;


public class JwtUtils {
    private static final String secret_key = "secret";
    private static final long expiration_time = 3600;

    public static String generateToken(users user ) {

        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        try{
            JWT jwt = new JWT();

            jwt.setSubject(user.getName());
            jwt.setExpiration(ZonedDateTime.now().plusSeconds(expiration_time));
            jwt.setIssuedAt(ZonedDateTime.now());

            jwt.addClaim("Login", user.getLogin());
            jwt.addClaim("privAdmin", user.getPrivAdmin());
            jwt.addClaim("numCuenta", user.getCuenta().getNumCuenta());

            return JWT.getEncoder().encode(jwt, HMACSigner.newSHA256Signer(secret_key));
        } catch (Exception e) {
            throw new RuntimeException("error generating token", e);
        }
    }

    public static JWT validacionToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("token cannot be null or empty");
        }
        try {
            JWT jwt = JWT.getDecoder().decode(token, HMACVerifier.newVerifier(secret_key));

            if (jwt.isExpired()) {
                throw new IllegalArgumentException("token expired");
            }

            return jwt;

        }catch (Exception e){
            return null;

        }
    }

    public static Boolean valAdmin(JWT jwt) {
        String PrivAdmin = jwt.getString("privAdmin");
        if (PrivAdmin == null || PrivAdmin.isEmpty()) {
            return false;
        }
        return true;
    }
}


