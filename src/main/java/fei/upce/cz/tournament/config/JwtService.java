package fei.upce.cz.tournament.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Service třída pro práci s JWT tokeny.
 * Zajišťuje generování, validaci a parsování JWT tokenů
 * používaných pro autentizaci uživatelů.
 */
@Service
public class JwtService {

    /** Tajný klíč použitý pro podepisování tokenů */
    private static final String SECRET = "tajny-klic-pro-jwt-tournament-app-2024-dlouhy";

    /** Doba platnosti tokenu v milisekundách (24 hodin) */
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    /**
     * Vytvoří kryptografický klíč z tajného řetězce.
     *
     * @return SecretKey pro podepisování JWT
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Vygeneruje JWT token pro přihlášeného uživatele.
     * Token obsahuje uživatelské jméno, role a dobu platnosti.
     *
     * @param userDetails přihlášený uživatel
     * @return JWT token jako řetězec
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    /**
     * Extrahuje uživatelské jméno z JWT tokenu.
     *
     * @param token JWT token
     * @return uživatelské jméno
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Ověří platnost JWT tokenu pro daného uživatele.
     * Token je platný pokud odpovídá uživateli a není expirovaný.
     *
     * @param token JWT token
     * @param userDetails uživatel
     * @return true pokud je token platný
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Zkontroluje zda je token expirovaný.
     *
     * @param token JWT token
     * @return true pokud je token expirovaný
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Parsuje a vrátí claims (data) z JWT tokenu.
     *
     * @param token JWT token
     * @return Claims objekt s daty tokenu
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}