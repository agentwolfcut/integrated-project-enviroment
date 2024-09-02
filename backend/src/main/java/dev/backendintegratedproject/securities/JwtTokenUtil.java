// JwtTokenUtil.java
package dev.backendintegratedproject.securities;

import dev.backendintegratedproject.userManage.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private SecretKey secretKey;
    private final long expiration = 1800L; // 30 minutes in seconds

    @PostConstruct
    public void init() {
        // Generate a secure key for HS256
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(UserEntity user) {
        Map<String, Object> information = new HashMap<>();
        information.put("name", user.getName());
        information.put("oid", user.getOid());
        information.put("email", user.getEmail());
        information.put("role", user.getRole());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // Explicitly set the token type as JWT
                .setClaims(information)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/kw3/") // Set the correct issuer
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(secretKey) // Sign with HS256
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}