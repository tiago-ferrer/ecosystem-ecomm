package br.com.fiap.postech.adjt.checkout.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

class WebConfigTest {

	@InjectMocks
	private WebConfig webConfig;

	@BeforeEach
	void setUp() {
		webConfig = new WebConfig();
	}

	@Test
	void testCorsFilterBeanCreation() {
		FilterRegistrationBean<CorsFilter> corsFilterBean = webConfig.corsFilter();
		assertNotNull(corsFilterBean, "CorsFilter bean should not be null");
		assertEquals(0, corsFilterBean.getOrder(), "CorsFilter order should be 0");
	}

	@Test
	void testCorsConfiguration() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);

		CorsConfiguration corsConfig = source.getCorsConfigurations().get("/**");
		assertNotNull(corsConfig, "CorsConfiguration should not be null");
		assertEquals("*", corsConfig.getAllowedOriginPatterns().get(0), "Allowed origin pattern should be '*'");
		assertEquals("*", corsConfig.getAllowedHeaders().get(0), "Allowed header should be '*'");
		assertEquals(true, corsConfig.getAllowCredentials(), "Allow credentials should be true");
		assertEquals(5, corsConfig.getAllowedMethods().size(), "There should be 5 allowed methods");
	}
}