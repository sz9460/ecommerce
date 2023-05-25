package com.example.order.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService  {


    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token){
        Claims claims= Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return  claims.get("roles",String.class);

    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String userName,String role) {
        return generateToken(new HashMap<>(), userName,role);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            String username,
            String role
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .claim("roles",role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isTokenValid(String token) {
        return ( !isTokenExpired(token)&&verifySignatureAlgorithm(token,SECRET_KEY,SignatureAlgorithm.HS256));
    }
    public Boolean verifySignatureAlgorithm(String token, String secretKey, SignatureAlgorithm expectedAlgorithm){
        SignatureAlgorithm algorithm = SignatureAlgorithm.valueOf(Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getHeader()
                .getAlgorithm());
        if(algorithm.equals(expectedAlgorithm)){
            return true;
        }
        return false;

    }
    public Integer exctractUserIdFromRequest(@NonNull HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        String  jwt = authHeader.substring(7);
        return  extractUserId(jwt);
    }
    public Integer extractUserId(String token){
        Claims claims= Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return  claims.get("UserId",Integer.class);

    }


}

