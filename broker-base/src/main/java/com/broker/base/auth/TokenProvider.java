package com.broker.base.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Accessors(chain = true)
@Data
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private String secretKey = "";
    private long tokenValidityInMilliseconds = 7*24*60*1000;

    public TokenProvider(){
        this.secretKey = System.getProperty("broker.auth.signingkey", "");
        this.tokenValidityInMilliseconds = Long.parseLong(System.getProperty("broker.auth.tokenvalid", "10080000"));
    }



    public String createToken(UserJwt userJwt) {
        Date validity = createValidity();
        return Jwts.builder()
                .setSubject("userJwt")
                .claim(UserJwt.APP_KEY, userJwt.getAppKey())
                .claim(UserJwt.USER_AUID, userJwt.getAuid())
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setExpiration(validity)
                .compact();
    }

    private Date createValidity(){
        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);
        return validity;
    }

    public UserJwt getUserJwt(String token) {
        if(!validateToken(token)){
            return new UserJwt();
        }
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return new UserJwt()
                .setAppKey((String)claims.get(UserJwt.APP_KEY))
                .setAuid((String)claims.get(UserJwt.USER_AUID))
                ;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
