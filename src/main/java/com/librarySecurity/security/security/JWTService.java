package com.librarySecurity.security.security;

import com.librarySecurity.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final UserRepository userRepository;

    @Value("${spring.jwt.secret}")
    private String JWT_SECRET;

    @Value("${spring.jwt.jwtExpirationTime}")
    private int JWT_EXPIRATION_TIME;

    public String getGeneratedToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return generatedTokenForUser(claims, userName);
    }

    public String generatedTokenForUser(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET );
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsernameFromToken(String theToken){
        return extractClaim(theToken, Claims::getSubject);
    }

    public Date extractExpirationFromToken(String theToken){
        return extractClaim(theToken, Claims::getExpiration);
    }


    public boolean validateToken(String theToken, UserDetails userDetails){
        final String userName = extractUsernameFromToken(theToken);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(theToken);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private boolean isTokenExpired(String theToken){
        return extractExpirationFromToken(theToken).before(new Date());
    }
}
