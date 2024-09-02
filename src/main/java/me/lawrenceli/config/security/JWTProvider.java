package me.lawrenceli.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component("tokenProvider")
public class JWTProvider {

    private static final String AUTHORITIES_KEY = "roles";
    private static final long VALIDITY_MS = 12 * 3600 * 1000; // 12 Hours
    private static final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyz"; // change me for private key

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        var secret = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        var claimsBuilder = Jwts.claims().subject(username);
        if (!authorities.isEmpty()) {
            claimsBuilder.add(AUTHORITIES_KEY, authorities.stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        }

        var claims = claimsBuilder.build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_MS);

        return Jwts.builder().claims(claims).issuedAt(now).expiration(validity)
                .signWith(secretKey, Jwts.SIG.HS256).compact();

    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();
        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null
                ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(this.secretKey)
                    .build().parseSignedClaims(token);
            // parseClaimsJws will check expiration date. No need do here.
            //            log.info("expiration date: {}", claims.getPayload().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException ignore) {
            //            log.info("Invalid JWT token: {}", e.getMessage());
            //            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
