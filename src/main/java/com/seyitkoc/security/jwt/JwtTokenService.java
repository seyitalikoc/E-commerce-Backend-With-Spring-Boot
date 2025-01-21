package com.seyitkoc.security.jwt;

import com.seyitkoc.config.GlobalProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtTokenService {

    @Autowired
    private GlobalProperties globalProperties;

    private long expirationTime = 10 * 60 * 1000; // 1 hour

    public String findUsernameFromToken(String token){
        return exportToken(token, Claims::getSubject);
    }

    public <T> T exportToken(String token, Function<Claims,T> claimsTFunction){
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(globalProperties.getSecret_key()).build().parseClaimsJws(token).getBody();

        return claimsTFunction.apply(claims);
    }

    public boolean tokenControl(String token, UserDetails userDetails){
        String username = findUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && new Date().before(exportToken(token, Claims::getExpiration));
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getKey(){
        byte[] key = Decoders.BASE64.decode(globalProperties.getSecret_key());
        return Keys.hmacShaKeyFor(key);
    }

}

