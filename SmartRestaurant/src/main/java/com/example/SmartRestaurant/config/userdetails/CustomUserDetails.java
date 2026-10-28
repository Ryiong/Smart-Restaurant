package com.example.SmartRestaurant.config.userdetails;

import com.example.SmartRestaurant.common.enums.UserStatus;
import com.example.SmartRestaurant.entity.PermissionEntity;
import com.example.SmartRestaurant.entity.RoleEntity;
import com.example.SmartRestaurant.entity.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetails implements UserDetails {
    //đây là file để security hiểu userentity là gì
    //custom vì userdetails không có cái object user entity kia
    //và security chỉ làm việc với userdetails được thôi
    //tự dùng entity của ứng dụng sẽ có thể lấy các thông tin của userentity kia được
    UserEntity user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (RoleEntity role : user.getRoles()) {
            authorities.add(
                    new SimpleGrantedAuthority(role.getName())
            );


            for (PermissionEntity permission : role.getPermissions()) {
                authorities.add(
                        new SimpleGrantedAuthority(permission.getName())
                );
            }
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }
}