package com.eventmanagementsystem.EventManagementSystem.util;

import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 10 * 60 * 60;

    private final String secret = "secret";

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserLogin userLogin) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userLogin.getEmailId());
    }

    public String generateToken(Admin adminLogin) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, adminLogin.getEmailId());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    public void validateToken(String token) throws Exception {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        }
        catch (Exception e)
        {
            throw new Exception();
        }
    }

}
