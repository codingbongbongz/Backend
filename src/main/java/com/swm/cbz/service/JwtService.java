package com.swm.cbz.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.time.Duration.ofDays;
import static java.time.Duration.ofHours;

import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.controller.exception.UnauthorizedException;
import com.swm.cbz.dto.authorization.response.TokenServiceVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    private static final Long ACCESS_TOKEN_VALID_TIME = ofHours(4).toMillis();
    private static final Long REFRESH_TOKEN_VALID_TIME = ofDays(14).toMillis();


    @PostConstruct // 한번만 생성되도록 하는
    protected void init() {
        jwtSecret = Base64.getEncoder()
            .encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(Long userId, Long tokenValidTime, String tokenType) {
        Claims claims = Jwts.claims()
            .setSubject(tokenType);

        claims.put("userId", userId);

        return Jwts.builder()
            .setClaims(claims)
            .setHeaderParam("type", tokenType)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(HS256, jwtSecret.getBytes())
            .compact();
    }

    public TokenServiceVO createServiceToken(Long userId) {
        return TokenServiceVO.builder()
            .accessToken(createAccessToken(userId))
            .refreshToken(createRefreshToken(userId))
            .build();
    }

    public String createAccessToken(Long userId) {
        return createJwt(userId, ACCESS_TOKEN_VALID_TIME, ACCESS_TOKEN);
    }

    public String createRefreshToken(Long userId) {
        return createJwt(userId, REFRESH_TOKEN_VALID_TIME, REFRESH_TOKEN);
    }

    public boolean isExpired(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return true;
        }

        return false;
    }

    public boolean verifyToken(String token) {
        try {
            final Claims claims = getBody(token);
            return true;
        } catch (RuntimeException e) {
            if (e instanceof ExpiredJwtException) {
                throw new UnauthorizedException(ErrorMessage.EXPIRED_TOKEN);
            }
            return false;
        }
    }

    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public Long getUserId(String token)
        throws ExpiredJwtException, MalformedJwtException, SignatureException,
        IllegalArgumentException {

        JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(jwtSecret.getBytes())
            .build();

        String userId = parser.parseClaimsJws(token)
            .getBody()
            .get("userId").toString();

        return Long.valueOf(userId);
    }
    public String getJwtContents(String token) {
        final Claims claims = getBody(token);
        Object userId = claims.get("userId");
        return userId != null ? userId.toString() : null;
    }
}
