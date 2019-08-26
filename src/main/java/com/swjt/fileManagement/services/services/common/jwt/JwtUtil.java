package com.swjt.fileManagement.services.services.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.swjt.fileManagement.services.services.common.PBKDF2;


import java.util.Date;

/**
 * @author AgilePhotonics
 */

/**
 * @author AgilePhotonics
 */
public class JwtUtil {

    /**
     * 过期时间，毫秒
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;


    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static DecodedJWT verify(String token, String username, String secret) {
        return JWT.require(Algorithm.HMAC256(secret))
                .withClaim("username", username)
                .build()
                .verify(token);
    }


    /**
     * 签名验证
     * @param token
     * @return
     */
    public static boolean verify(String token){

        PBKDF2 pbkdf2 = new PBKDF2();

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(pbkdf2.algorithm)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("issuer: " + jwt.getIssuer());
            System.out.println("username: " + jwt.getClaim("username").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }

    }


    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }

    /**
     * 生成签名,EXPIRE_TIME后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(secret));
    }
}
