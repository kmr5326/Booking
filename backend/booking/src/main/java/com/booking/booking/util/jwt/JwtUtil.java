package com.booking.booking.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {

    @Value("${jwt.secret}")
    private static String secret;
    public static String getLoginEmailByToken(String token) {

        //bearer
        token = token.substring(7);

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        var key = Keys.hmacShaKeyFor(keyBytes);

        Jws<Claims> claims = Jwts.parserBuilder()
                                 .setSigningKey(key) // 여기에 서명 확인을 위한 키 설정
                                 .build()
                                 .parseClaimsJws(token);

        // Subject 가져오기
        return claims.getBody().getSubject();
    }

}
