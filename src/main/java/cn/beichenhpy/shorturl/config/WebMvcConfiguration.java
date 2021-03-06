package cn.beichenhpy.shorturl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author beichenhpy
 * @version 1.0
 * @description webMVC设置跨域
 * @since 2021/3/1 11:05
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		/* 是否允许请求带有验证信息 */
		corsConfiguration.setAllowCredentials(true);
		/* 允许访问的客户端域名 */
		corsConfiguration.addAllowedOrigin("*");
		/* 允许服务端访问的客户端请求头 */
		corsConfiguration.addAllowedHeader("*");
		/* 允许访问的方法名,GET POST等 */
		corsConfiguration.addAllowedMethod("*");
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
