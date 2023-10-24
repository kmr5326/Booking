package com.booking.member.global.config;

import com.booking.member.Auth.PrincipalOauth2UserService;
import com.booking.member.global.config.handler.MyAccessDeniedHandler;
import com.booking.member.global.config.handler.MyAuthenticationEntryPoint;
import com.booking.member.members.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("").authenticated()
                //.antMatchers("").hasAuthority(UserRole.ADMIN.name())
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                //.loginPage("")
                .loginProcessingUrl("/api/auth/login")
                .defaultSuccessUrl("/") // 수정 필요
                .failureUrl("/error") // 수정 필요
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .oauth2Login()
//                .loginPage("/security-login/login")
                .loginProcessingUrl("/api/auth/login")
                .defaultSuccessUrl("/security-login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        http
                .exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .accessDeniedHandler(new MyAccessDeniedHandler());
    }
}