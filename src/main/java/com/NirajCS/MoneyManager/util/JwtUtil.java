package com.NirajCS.MoneyManager.util;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /* ==================== TOKEN GENERATION ==================== */



    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ==================== TOKEN VALIDATION ==================== */

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isExpired = isTokenExpired(token);
            
            logger.debug("Token validation - Username in token: {}, User details username: {}, Expired: {}", 
                    username, userDetails.getUsername(), isExpired);
            
            if (username == null) {
                logger.warn("Failed to extract username from token");
                return false;
            }
            
            if (!username.equals(userDetails.getUsername())) {
                logger.warn("Token username '{}' does not match user details username '{}'", 
                        username, userDetails.getUsername());
                return false;
            }
            
            if (isExpired) {
                logger.warn("Token has expired for user: {}", username);
                return false;
            }
            
            logger.debug("Token validation successful for user: {}", username);
            return true;
        } catch (Exception e) {
            logger.error("Token validation exception: {}", e.getMessage(), e);
            return false;
        }
    }

    /* ==================== EXTRACT METHODS ==================== */

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            return null;
        }
    }

    public Date extractExpiration(String token) {
    try {
        return extractAllClaims(token).getExpiration();
    } catch (Exception e) {
        return null;
    }
}


    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /* ==================== INTERNAL HELPERS ==================== */

    private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
}


 private Boolean isTokenExpired(String token) {
    Date expiration = extractExpiration(token);
    return expiration == null || expiration.before(new Date());
}



    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
