package com.swjt.fileManagement.services.services.common.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author AgilePhotonics
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
