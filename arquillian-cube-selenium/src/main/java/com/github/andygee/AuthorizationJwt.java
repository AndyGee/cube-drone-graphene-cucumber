package com.github.andygee;


import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;


/**
 * Converts the concept of Authentication (Delivered by the Gateway) to Authorization (Delivered by the JWT).
 */
public class AuthorizationJwt extends UsernamePasswordAuthenticationToken {

    public AuthorizationJwt(final Principal p, final Claims jwt, final Collection<? extends GrantedAuthority> gas) {
        super(p, jwt, gas);
    }
}