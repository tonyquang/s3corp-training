package com.quan12yt.dbmicroservice.utils;

import com.quan12yt.dbmicroservice.config.jwt.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JWTokenProvider {

    private static final String JWT_SECRET = "Jason";
    private static final Long JWT_EXPIRATION = 604800L;

    public static String generateJWT(CustomUserDetails userDetails) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getAccounts().getAccountId()))
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public static Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error(ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error(ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }


}
