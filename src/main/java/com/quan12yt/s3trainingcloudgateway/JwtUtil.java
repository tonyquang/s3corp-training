package com.quan12yt.s3trainingcloudgateway;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JwtUtil {

    private JwtUtil() {
    }

    private static final String SECRET_KEY = "Jason";

    public static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
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