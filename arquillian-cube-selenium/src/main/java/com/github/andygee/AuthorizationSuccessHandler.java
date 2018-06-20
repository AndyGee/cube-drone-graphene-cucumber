package com.github.andygee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default handler for successful authorization.
 * <p>
 * This will always be called, as there will always be either an unauthorized Anonymous user
 * or an Authorized JWT user with roles.
 */
public class AuthorizationSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(AuthorizationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest req,
            final HttpServletResponse res,
            final Authentication auth) {

        final User user = User.class.cast(auth.getPrincipal());
        this.log.info("Authorized user '{}' with roles {}", user.getUsername(),user.getAuthorities());
    }

}
