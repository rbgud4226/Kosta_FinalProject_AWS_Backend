package com.example.demo.oracledb.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

// 추가
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration {
	
	private final MyTokenProvider myTokenProvider;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(HttpBasicConfigurer::disable)
		.csrf(CsrfConfigurer::disable)
		.cors(Customizer.withDefaults())
		.authorizeHttpRequests((authz) -> authz
						.requestMatchers("/index_admin").hasRole("ADMIN")
						.requestMatchers("/index_emp").hasRole("EMP")
						.requestMatchers("/css/**","/img/**","/js/**").permitAll()
						.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/auth/**", "/index_**", "/admin/**").authenticated()
						.requestMatchers("/", "/error", "/login", "/idcheck", "/user/**", "/member/**", "/corp/**", "/files/**", "/chat/**","/mail/**").permitAll()
						.anyRequest().permitAll()
						)
//				.formLogin((login) -> login.loginPage("/loginform")
//						.loginProcessingUrl("/login")
//						.failureForwardUrl("/loginerror")
//						.usernameParameter("id")
//						.passwordParameter("pwd")
//						.defaultSuccessUrl("/", true).permitAll()
//						.successHandler(new MySuccessHandler())
//						.failureHandler(new MyFailureHandler())
//						);
//				.and()
//						.logout()
//						.logoutUrl("/logout")
//						.logoutSuccessUrl("/")
			.addFilterBefore(new JwtAuthenticationFilter(myTokenProvider), UsernamePasswordAuthenticationFilter.class);
		http.sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //>>세터사용하지 않게다
		return http.build();
	}
	
}
