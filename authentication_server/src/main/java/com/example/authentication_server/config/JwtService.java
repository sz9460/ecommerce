package com.example.authentication_server.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class JwtService  {


  private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractRole(String token){
    Claims claims=Jwts
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

  public String generateToken(String userName,String role,Integer userid) {
    return generateToken(new HashMap<>(), userName,role,userid);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      String username,
      String role,Integer userid
  ) {
    return Jwts
            .builder()
              .setClaims(extraClaims)
              .setSubject(username)
              .claim("roles",role)
              .claim("UserId",userid)
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .setHeaderParam("alg", "HS256")
            .setHeaderParam("typ", "JWT")
              .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
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
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) &&verifySignatureAlgorithm(token,SECRET_KEY,SignatureAlgorithm.HS256);
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


}
