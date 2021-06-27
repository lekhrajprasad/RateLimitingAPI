package com.lpras.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Bucket4jRateLimitApp implements CommandLineRunner, WebMvcConfigurer {
	static final Logger LOG = LoggerFactory.getLogger(Bucket4jRateLimitApp.class);

	public static void main(String[] args) {
		SpringApplication.run(Bucket4jRateLimitApp.class, args);
	}

	@Override
	public void run(String... args) {
		// Start-UP Tasks
		LOG.info("Bucket4jRateLimitApp - Launched.... ");
	}

	@Autowired
	@Lazy
	private RateLimitHandlerInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor).addPathPatterns("/api/v1/area/**");
	}
}
