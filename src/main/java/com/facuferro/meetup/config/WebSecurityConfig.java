package com.facuferro.meetup.config;

import com.facuferro.meetup.security.JWTAuthorizationFilter;
import com.facuferro.meetup.security.SecurityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JWTAuthorizationFilter jWTAuthorizationFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.csrf().disable()
                .addFilterAfter(jWTAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/forgotPassword", "/passwordReset", "/register", "/registerStudent", "/resources/**", "/js/**", "**/js/**", "/static/**", "/login", "/", "/api/**").permitAll()
                .antMatchers("/locations/**").hasAuthority(SecurityConstant.ROLE_ADMIN)
                .antMatchers("/meetups/**/beers/calculate").hasAuthority(SecurityConstant.ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/meetups/").hasAuthority(SecurityConstant.ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/meetups").hasAuthority(SecurityConstant.ROLE_ADMIN)
                .antMatchers("/meetups/**/temperature").permitAll()
                .antMatchers("/meetups").authenticated()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.POST, "/users/").permitAll()
                .antMatchers("/users").hasAuthority(SecurityConstant.ROLE_ADMIN)
                .antMatchers("/users/*").hasAuthority(SecurityConstant.ROLE_ADMIN)
                .antMatchers( "/users/**/confirm/**").permitAll();

    }


}

