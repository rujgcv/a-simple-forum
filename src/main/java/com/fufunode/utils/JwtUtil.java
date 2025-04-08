package com.fufunode.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "8932vny5rt92qyt9q4hdiqhiruq23qwrqv"; // 至少32字符
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final long EXPIRATION_MS = 30L * 24 * 60 * 60 * 1000; // 30天

    public static String getToken(String username, String role) {
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("role", role)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                    .sign(ALGORITHM);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Token生成失败", e);
        }
    }

    public static boolean parseToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT jwt = verifier.verify(token);
            String username = jwt.getSubject();
            String role = jwt.getClaim("role").asString();
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}