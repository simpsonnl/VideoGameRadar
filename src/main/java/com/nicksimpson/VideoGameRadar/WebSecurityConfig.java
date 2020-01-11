package com.nicksimpson.VideoGameRadar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;


//  protected void configure(HttpSecurity http) throws Exception {
//
//    http.authorizeRequests().antMatchers("/edit/**")
//        .access("hasRole('ADMIN')").and().formLogin()
//        .loginPage("/login").failureUrl("/login?error")
//        .usernameParameter("username")
//        .passwordParameter("password")
//        .and().logout().logoutSuccessUrl("/login?logout")
//        .and().csrf()
//        .and().exceptionHandling().accessDeniedPage("/403");
//  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
    accessDeniedHandler.setErrorPage("/403");
    return accessDeniedHandler;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/edit/**").hasAnyRole("USER","ADMIN")
        .and()
        .authorizeRequests()
        .antMatchers("/upload").hasAnyRole("USER","ADMIN")
        .and()
        .authorizeRequests()
        .antMatchers("/delete/**").hasAnyRole("USER","ADMIN")
        .and()
        .formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password").permitAll()
        .successForwardUrl("/postLogin")
        .failureUrl("/login?error")
        .and()
        .logout().logoutUrl("/doLogout").logoutSuccessUrl("/logout").permitAll()
        .and()
        .csrf();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}