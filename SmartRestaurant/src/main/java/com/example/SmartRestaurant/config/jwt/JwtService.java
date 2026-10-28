package com.example.SmartRestaurant.config.jwt;

import com.example.SmartRestaurant.common.enums.HostDeviceStatus;
import com.example.SmartRestaurant.config.userdetails.CustomUserDetails;
import com.example.SmartRestaurant.exception.ExpiredJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${access-token.secret-key}")
    String secretToken;
    @Value("${access-token.expiration}")
    Long expiration;
    @Value("${refresh-token.secret-key}")
    String refreshSecretToken;


    @Value("${host-device-token.secret}")
    String hostDeviceTokenSecret;


    //access token
    public String generateAccessToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .toList();

        List<String> permissions = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("PERM_"))
                .toList();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getAccessSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractAccessUsername(String token) {
        return extractAccessAllClaims(token).getSubject();
    }

//    public boolean validateAccessToken(String token, UserDetails userDetails) {
//        final String username = extractAccessUsername(token);
//        return username.equals(userDetails.getUsername());
//    }

    private Claims extractAccessAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getAccessSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException("Access");
        }
    }

    private Key getAccessSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //refresh token
    public String generateRefreshToken(CustomUserDetails userDetails, Date expiryDate) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractRefreshUsername(String token) {
        return extractRefreshAllClaims(token).getSubject();
    }

//    public boolean validateRefreshToken(String token, UserDetails userDetails) {
//        final String username = extractRefreshUsername(token);
//        return username.equals(userDetails.getUsername());
//    }

    private Claims extractRefreshAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getRefreshSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException("Refresh");
        }
    }

    private Key getRefreshSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(refreshSecretToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //host-device
    public String generateHostDeviceRefreshToken(Long hostId,
                                                 String name,
                                                 Long shopId,
                                                 HostDeviceStatus status) {
        return Jwts.builder()
                .setSubject(String.valueOf(hostId))
                .setIssuedAt(new Date())
                .signWith(getHostDeviceSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getHostDeviceSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(hostDeviceTokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractHostDeviceAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getHostDeviceSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractHostDeviceId(String token) {
        Claims claims = extractHostDeviceAllClaims(token);
        return Long.valueOf(claims.getSubject());
    }
}