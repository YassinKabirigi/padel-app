package be.ephec.padel_backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // Cle secrete de demo - a deplacer dans application.properties avant remise finale
    private static final String SECRET_KEY = "cette-cle-doit-faire-au-moins-256-bits-pour-hs256-padel-2026";
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 8; // 8 heures

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String genererToken(String matricule, String typeMembre) {
        return Jwts.builder()
                .subject(matricule)
                .claim("type", typeMembre)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public String extraireMatricule(String token) {
        return extraireClaim(token, Claims::getSubject);
    }

    public String extraireTypeMembre(String token) {
        return extraireClaim(token, claims -> claims.get("type", String.class));
    }

    public boolean isTokenValide(String token, String matricule) {
        String matriculeExtrait = extraireMatricule(token);
        return matriculeExtrait.equals(matricule) && !isTokenExpire(token);
    }

    private boolean isTokenExpire(String token) {
        return extraireClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extraireClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }
}