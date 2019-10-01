package com.example.demo.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource datasource;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		 auth.jdbcAuthentication().dataSource(datasource)
		.usersByUsernameQuery("select u.username as principal, u.password as credentials,u.enabled from users u where u.username=?" )
		.authoritiesByUsernameQuery("select u.username as principal,  r.role_name as role from users u, roles r where u.user_id = r.user_id and u.username=? ")
		.rolePrefix("ROLE_").passwordEncoder(new MessageDigestPasswordEncoder("MD5") );	
		 
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().defaultSuccessUrl("/");
		http.authorizeRequests().antMatchers("/*").hasAnyRole("ADMIN","USER");
		http.logout().invalidateHttpSession(true);
		http.logout().clearAuthentication(true);
		
		


	}	

}
