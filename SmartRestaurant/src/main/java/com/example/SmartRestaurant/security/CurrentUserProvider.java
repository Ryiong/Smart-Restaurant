package com.example.SmartRestaurant.security;

import com.example.SmartRestaurant.config.userdetails.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    public CustomUserDetails getCurrentUser() {
        return (CustomUserDetails)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getUser().getId();
    }

    public String getCurrentUsername() {
        return getCurrentUser().getUser().getName();
    }
}
