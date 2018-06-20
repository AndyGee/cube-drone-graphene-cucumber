package com.github.andygee;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collection;

/**
 * Converts the
 */
@Component
public class AuthenticationProviderJwt extends AbstractUserDetailsAuthenticationProvider {


    @Override
    public boolean supports(final Class<?> authentication) {
        return (AuthorizationJwt.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(final UserDetails userDetails, final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //TODO - Enrich with more fun stuff...
    }

    @Override
    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        final AuthorizationJwt jwt = (AuthorizationJwt) authentication;
        final String un = Principal.class.cast(jwt.getPrincipal()).getName();
         final Collection<GrantedAuthority> authorities = jwt.getAuthorities();

        if (un == null) {
            throw new AuthenticationServiceException("Username is not valid");
        }

        return new User(un, "nopw", authorities);
    }

}
