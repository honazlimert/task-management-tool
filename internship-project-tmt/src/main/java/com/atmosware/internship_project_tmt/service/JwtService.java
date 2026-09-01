package com.atmosware.internship_project_tmt.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // 256-bit (32 byte) gizli imza anahtarımız (gerçek projelerde gizli tutulur)
    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // generate token
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email) // user e-mail
                .issuedAt(new Date(System.currentTimeMillis())) // veriliş tarihi
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 saat geçerlilik süresi
                .signWith(getSigningKey()) // secret key ile imzala
                .compact(); // JSON web token metnine dönüştür
    }

    // extract e-mail
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // dogrulamada kullanilacak secret key oluşturur
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}