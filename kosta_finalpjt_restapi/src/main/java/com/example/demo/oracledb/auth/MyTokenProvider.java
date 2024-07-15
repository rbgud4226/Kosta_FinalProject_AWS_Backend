package com.example.demo.oracledb.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.oracledb.users.UsersDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MyTokenProvider {
	private final long expiredTime = 1000 * 60 * 60 * 1L; // 유효시간 1시간
	Key key = Keys.hmacShaKeyFor("asdfdfghjhgukjhljkdyhywsrgegsrthtyjguil;yujrthyjfhgkeargaegthaegahukgjhlsfghdghg"
			.getBytes(StandardCharsets.UTF_8));

	@Autowired
	private final MyUserDetailsService tokenUserDetailsService;

	// 토큰 생성하여 반환
	public String getToken(UsersDto dto) {
		Date now = new Date();
		return Jwts.builder().setSubject(dto.getId()).setHeader(createHeader()).setClaims(createClaims(dto))
				.setExpiration(new Date(now.getTime() + expiredTime)).signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public Map<String, Object> createHeader() {
		Map<String, Object> map = new HashMap<>();
		map.put("typ", "JWT");
		map.put("alg", "HS256");
		map.put("regDate", System.currentTimeMillis());
		return map;
	}

	public Map<String, Object> createClaims(UsersDto dto) {
		Map<String, Object> map = new HashMap();
		map.put("username", dto.getId());
		map.put("roles", dto.getType());
		return map;
	}

	// 토큰을 파싱하여 크레임정보만 반환
	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	// claims에서 username 추출
	public String getUserName(String token) {
		return (String) getClaims(token).get("username");
	}

	// claims에서 roles 추출
	public String getRoles(String token) {
		return (String) getClaims(token).get("roles");
	}

	// 요청 헤더에서 토큰 추출
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("auth_token");
	}

	// 토큰 유효성 검사
	public boolean validateToken(String token) {
		try {
			Claims claims = getClaims(token);
			return !claims.getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	// 토큰 인증
	public Authentication getAuthentication(String token) {
		// 토큰에 저장된 username을 꺼내서 db 검색
		UserDetails user = tokenUserDetailsService.loadUserByUsername(getUserName(token));
		return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
	}
}
