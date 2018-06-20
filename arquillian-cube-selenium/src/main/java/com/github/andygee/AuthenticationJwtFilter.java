package com.github.andygee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * JSON Web Token Filter.
 * <p>
 * A request is anonymous, unless a validated jwt is found.
 */
public class AuthenticationJwtFilter extends AbstractAuthenticationProcessingFilter {

    public static final String BEARER_ = "Bearer ";
    private final Logger log = LoggerFactory.getLogger(AuthenticationJwtFilter.class);

    @Value("${jwt.secret:f26c5d6d5ca84d466d4c9c505f73971a1d280cace274d5b9}")
    private String tokenSecret;

    @Value("${jwt.cookie:pxc-scs-cookie}")
    private String cookie;

    @Value("${jwt.header:Authorization}")
    private String header;

    /**
     * Default consructor that defines the filter for every single request
     * In the real world we don't want static elements here.
     */
    protected AuthenticationJwtFilter() {
        super("/**");
    }

/*    @Path("hello")
    public String method(){
        if(in("role1")){
//do this
        }else{
//do that
        }
    }*/

    /**
     * The SCS does not actually Authenticate, rather Validate & Authorize.
     */
    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {

        Claims body = null;

        try {
            final Cookie cookie = Cookie.class.cast(WebUtils.getCookie(request, this.cookie));
            String jwt = (null != cookie ? cookie.getValue() : "");

            if (StringUtils.isEmpty(jwt)) {
                //Try the header
                String header = request.getHeader(this.header);
                header = (null != header ? header.replace(BEARER_, "") : "");
                jwt = header;
            }

            //This is io.jsonwebtoken, but could be any JWT lib
            body = null;

            if (!StringUtils.isEmpty(jwt)) {

                this.log.info("Cookie token: " + jwt);

                if (jwt.contains(".")) {

                    try {
                        body = Jwts.parser()
                                .setSigningKey(this.tokenSecret.getBytes("UTF-8"))
                                .parseClaimsJws(jwt)
                                .getBody();
                    } catch (final UnsupportedEncodingException e) {
                        throw new Exception("Decodde error", e);
                    }
                } else {
                    //Basic un:role,role
                    body = new DefaultClaims();
                    final String[] split = this.decode(jwt).split(":");
                    body.put("name", split[0]);
                    body.put("roles", Arrays.asList(split[1].split("\\s*,\\s*")));
                }
            }
        } catch (final Exception e) {
            this.log.warn("Error processing token cookie", e);

            //This request will be handled as 'anonymousUser'
        }

        final String un = this.getUsername(body);
        final Collection<SimpleGrantedAuthority> roles = this.getAuthorities(body);

        return this.getAuthenticationManager().authenticate(new AuthorizationJwt(() -> un, body, roles));
    }

    private String decode(final String s) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(Base64.decodeBase64(s));
    }

    /**
     * Get the parsed user or 'anonymousUser' if no token exists, or the token is invalid.
     *
     * @param body Claims or null
     * @return User or 'anonymousUser'
     */
    private String getUsername(final Claims body) {
        final String uid = null != body ? body.get("name", String.class) : null;
        return StringUtils.isEmpty(uid) ? "anonymousUser" : uid;
    }

    /**
     * Get a collection of GrantedAuthority, which are basically the roles.
     *
     * @param body Claims or null
     * @return GrantedAuthority collection, may be empty.
     */
    private Collection<SimpleGrantedAuthority> getAuthorities(final Claims body) {

        final Collection<SimpleGrantedAuthority> roles = new HashSet<>();

        if (null != body) {
            final Collection list = body.get("roles", Collection.class);
            if (null != list) {
                for (final Object ro : list) {
                    roles.add(new SimpleGrantedAuthority(ro.toString()));
                }
            }
        }

        return roles;
    }

    /**
     * After checking for Authorization the request chain will continue.
     *
     * @param req        HttpServletRequest
     * @param res        HttpServletResponse
     * @param chain      FilterChain
     * @param authResult Authentication
     * @throws IOException      On IO errors
     * @throws ServletException On Servlet errors
     */
    @Override
    protected void successfulAuthentication(
            final HttpServletRequest req,
            final HttpServletResponse res,
            final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {

        super.successfulAuthentication(req, res, chain, authResult);
        chain.doFilter(req, res);
    }
}