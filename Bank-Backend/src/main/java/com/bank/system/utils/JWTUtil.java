package com.bank.system.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Generate token with additional claims (name, role, userId)
    public String generateToken(String userEmail, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userEmail) 
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1  day
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username (subject) from the token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract all claims from the token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract a specific claim from the token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Get signing key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName != null && userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generate a refresh token
    public String generateRefreshToken(String userEmail, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String getAccessTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    
    public boolean isTokenIncluded(String req){
        if(req!=null)
            return true;
        return false;
    }
    
}
