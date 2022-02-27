package com.eventmanagementsystem.EventManagementSystem.util;

import com.eventmanagementsystem.EventManagementSystem.exception.GlobalExceptionHandler;
import com.eventmanagementsystem.EventManagementSystem.exception.JwtTokenExpiredException;
import com.eventmanagementsystem.EventManagementSystem.model.Admin;
import com.eventmanagementsystem.EventManagementSystem.model.UserLogin;
import com.eventmanagementsystem.EventManagementSystem.repository.AdminRepository;
import com.eventmanagementsystem.EventManagementSystem.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

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
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e)
        {
            throw new JwtTokenExpiredException("JWT Token Expired");
        }
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
    public Boolean validateAdminToken(String token) {
        try {
            final String emailId = getUsernameFromToken(token);
            return (adminRepository.findByEmailId(emailId) != null && !isTokenExpired(token));
        }
        catch (IllegalArgumentException | SignatureException | MalformedJwtException e)
        {
            return false;
        }
    }

    public Boolean validateUserToken(String token) {
        try {
            final String emailId = getUsernameFromToken(token);
            return (userRepository.findByEmailId(emailId) != null && !isTokenExpired(token));
        }
        catch (IllegalArgumentException | SignatureException | MalformedJwtException e)
        {
            return false;
        }
    }
}
