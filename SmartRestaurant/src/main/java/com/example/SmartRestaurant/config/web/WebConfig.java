package com.example.SmartRestaurant.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/auth/**").addResourceLocations("classpath:/templates/auth");
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/templates/admin/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/auth/login.html");
        registry.addRedirectViewController("/login", "/auth/login.html");
    }
}
