package com.pradeep.smart_parking_api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pradeep.smart_parking_api.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/users").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/slots/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/slots/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/slots/**").hasRole("ADMIN")
						.requestMatchers("/api/users/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/bookings", "/api/bookings/user/**",
								"/api/bookings/slot/**")
						.hasRole("ADMIN").anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, e) -> {
					response.setStatus(401);
					response.setContentType("application/json");
					response.getWriter()
							.write("{\"status\":401,\"message\":\"Login required: token missing or invalid\"}");
				}).accessDeniedHandler((request, response, e) -> {
					response.setStatus(403);
					response.setContentType("application/json");
					response.getWriter()
							.write("{\"status\":403,\"message\":\"You do not have permission for this action\"}");
				})).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}