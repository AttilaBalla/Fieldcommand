package com.fieldcommand.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**/*.{js,html}");
        web.debug(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // temporary; WIP
        http
            .authorizeRequests()
            .antMatchers("/admin/**").permitAll()
            .antMatchers("/about").permitAll()
            .antMatchers("/releases").permitAll()
            .antMatchers("/swrstatus").permitAll()
            .antMatchers("/activate").permitAll()
            .antMatchers("/").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .logoutSuccessUrl("/login?logout")
            .permitAll()
            .and()
            .csrf().disable();
    }

}

