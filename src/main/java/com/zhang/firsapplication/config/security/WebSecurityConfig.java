package com.zhang.firsapplication.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandler;

    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()// 弹出框认证
                .loginPage("/login")// 登录页
                .loginProcessingUrl("/auth/login")// 登录请求地址
                .defaultSuccessUrl("/index")
                .failureHandler(authenticationFailureHandler)
                .successHandler(authenticationSuccessHandler)
                .and()
                .authorizeRequests()// 身份认证请求
//                .mvcMatchers()
//                .permitAll()
                .antMatchers("/css/**", "/js/**", "/lib/**", "/webjars/**", "/swagger-ui.html", "/swagger-resources/**", "/login")
                .permitAll()
                .anyRequest() // 所有请求
                .authenticated(); // 身份认证

        http.rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenRepository(persistentTokenRepository)
                .userDetailsService(myUserDetailsService);

        http.csrf()
                .disable()
                .logout();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(myUserDetailsService);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
