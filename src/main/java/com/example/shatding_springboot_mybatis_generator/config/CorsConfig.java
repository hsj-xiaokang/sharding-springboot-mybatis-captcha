package com.example.shatding_springboot_mybatis_generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Spring boot 完美解决ajax跨域请求问题
 * 
 * @author heshengjin
 * 
 */
@Configuration
public class CorsConfig {
	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
		corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
		corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4 对接口配置跨域设置
		return new CorsFilter(source);
	}
}
