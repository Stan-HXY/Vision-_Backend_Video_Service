package com.stan.vision.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stan.vision.domain.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {


    private static final String ISSUER = "Stan";

    public static String generateToken(Long userID) throws Exception{
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        // 生成JWT过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 30);

        return JWT.create().withKeyId(String.valueOf(userID))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    public static Long verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userID = jwt.getKeyId();
            return Long.valueOf(userID);
        }
        catch (TokenExpiredException e){
            throw new ConditionException("555", "Token Expired!");
        }
        catch (Exception e){
            throw new ConditionException("Invalid user token!");
        }
    }
}
