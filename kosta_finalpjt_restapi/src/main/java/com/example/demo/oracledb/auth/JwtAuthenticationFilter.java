package com.example.demo.oracledb.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
	private final MyTokenProvider myTokenProvider;
	private final List<String> allowedOrigins = Arrays.asList("http://localhost:3000"); 

	@Override // 필터가 할일 구현
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		HttpServletResponse res = (HttpServletResponse) response;
////		res.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
//		res.setHeader("Access-Control-Allow-Origin", "*");
//		res.setHeader("Access-Control-Allow-Credentials", "true");
//		res.setHeader("Access-Control-Allow-Methods", "*");
//		res.setHeader("Access-Control-Allow-Max-Age", "3600");
//		res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
//		res.setStatus(HttpServletResponse.SC_OK);
		
		// TODO Auto-generated method stub
    HttpServletResponse res = (HttpServletResponse) response;
    HttpServletRequest req = (HttpServletRequest) request;
    res.setHeader("Access-Control-Allow-Origin", "http://localhost:8081");
//				res.setHeader("Access-Control-Allow-Origin", "*");
     String origin = req.getHeader("Origin");
         res.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "");
         res.setHeader("Vary", "Origin");

    res.setHeader("Access-Control-Allow-Credentials", "true");
    res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
    res.setHeader("Access-Control-Allow-Max-Age", "3600");
    res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    res.setStatus(HttpServletResponse.SC_OK);

		String token = myTokenProvider.resolveToken((HttpServletRequest) request);
		if (token != null && myTokenProvider.validateToken(token)) {
			// 토큰으로 인증하고 그 결과인 Authentication 객체 반환
			Authentication authentication = myTokenProvider.getAuthenticatiln(token);

			// 반환된 Authentication 객체를 context에 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}

}
