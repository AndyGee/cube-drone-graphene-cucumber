package com.github.andygee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfigurationSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPointJwt unauthorizedHandler;

    @Autowired
    private AuthenticationProviderJwt authenticationProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(this.authenticationProvider));
    }

    @Override
    protected void configure(final HttpSecurity hs) throws Exception {

        hs.csrf().disable();
        hs.cors().disable();

        hs.authorizeRequests()
                .anyRequest().permitAll()
                .and().exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        hs.addFilterAfter(this.getAuthenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);

        hs.headers().cacheControl().disable();
    }

    /**
     * Creates an AuthenticationJwtFilter to use for filtering JWTs
     *
     * @return AuthenticationJwtFilter
     * @throws Exception On error
     */
    @Bean
    public AuthenticationJwtFilter getAuthenticationJwtFilter() throws Exception {
        final AuthenticationJwtFilter authenticationTokenFilter = new AuthenticationJwtFilter();
        authenticationTokenFilter.setAuthenticationManager(this.authenticationManager());
        authenticationTokenFilter.setAuthenticationSuccessHandler(new AuthorizationSuccessHandler());
        return authenticationTokenFilter;
    }
}