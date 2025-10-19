package com.fufunode.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "8932vny5rt92qyt9q4xfgdhhiruq23qwrqv";

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final long EXPIRATION_MS = 30L * 24 * 60 * 60 * 1000; // 30天

    public static String getToken(Long userId, String username, String role) {
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("id",userId)
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
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static Long parseTokenToUserId(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("id").asLong();  // 从Token中提取用户ID
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public static String parseTokenToRole(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("role").asString();  // 从Token中提取Role
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}