package dev.backendintegratedproject.securities;

import dev.backendintegratedproject.userManage.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private SecretKey secretKey;
    private final long expiration = 18000L;  // Token expiration time in seconds (30 minutes)

    @PostConstruct
    public void init() {
        // Generate secure key for HS256
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // Generate JWT with user information
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("oid", user.getOid());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())  // Subject is the username or email
                .setIssuer("https://intproj23.sit.kmutt.ac.th/kk3/")  // Set the correct issuer
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))  // Expiration time
                .signWith(secretKey)  // Sign with HS256
                .compact();
    }

    // Parse claims from the JWT
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate token by comparing subject and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final Claims claims = getClaimsFromToken(token);
        final String username = claims.getSubject();
        final Date expirationDate = claims.getExpiration();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(expirationDate));
    }

    private Boolean isTokenExpired(Date expirationDate) {
        return expirationDate.before(new Date());
    }

    // Retrieve username from token (subject)
    public String getUsernameFromToken(String jwtToken) {
        return getClaimsFromToken(jwtToken).getSubject();
    }
}
