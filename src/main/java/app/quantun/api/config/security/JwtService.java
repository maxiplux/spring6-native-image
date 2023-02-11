package app.quantun.api.config.security;

import app.quantun.api.exceptions.InvalidTokenException;
import app.quantun.api.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  @Value("${app.config.jwt.secret}")
  private  String secretKey;

  @Value("${app.config.jwt.time.to.live}")
  private Integer tokeTimeToLive;

  @Value("${app.config.jwt.time.to.refresh.live}")
  private Integer tokeTimeToRefreshLive;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(User userDetails)
  {
    Map<String, Object> info = new HashMap<>();

    info.put("firtsName", userDetails.getFirstname());
    info.put("lastName", userDetails.getLastname());
    info.put("email", userDetails.getEmail());
    info.put("roles", userDetails.getAuthorities());
    return generateToken(info, userDetails);
  }

  public String generateFreshToken(User userDetails)
  {
    return generateToken(new HashMap<>(), userDetails);
  }



  public String generateToken(Map<String, Object> extraClaims,    User userDetails  ) {
    if (extraClaims.isEmpty())
    {
      return Jwts
              .builder()
              .setClaims(extraClaims)
              .setSubject(userDetails.getUsername())
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + 1000 *60* this.tokeTimeToRefreshLive))
              .signWith(getSignInKey(), SignatureAlgorithm.HS256)
              .compact();

    }
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 *60* this.tokeTimeToLive))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
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
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
  private String refreshToken(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public String getUsernameFromToken(String refreshToken) {
   return extractUsername(refreshToken);
  }

  public void isNotAuthorizationToken(String jwt) {
    var claims=  extractAllClaims(jwt);
    if (claims.get("email") == null)
    {
      throw new InvalidTokenException("Invalid token");
    }

  }
}
