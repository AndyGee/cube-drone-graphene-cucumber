package com.github.andygee;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class methods are called if authorization fails
 */
@Component
public class AuthenticationEntryPointJwt implements AuthenticationEntryPoint, Serializable {


    private static final long serialVersionUID = -1;

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {

        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to

        //TODO - There IS a login page, we just need to define it...
        //response.sendRedirect("TODO login URL");

        LoggerFactory.getLogger(AuthenticationEntryPointJwt.class).warn("Error: " + authException);

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
