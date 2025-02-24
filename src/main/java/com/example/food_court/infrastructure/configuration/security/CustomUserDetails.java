package com.example.food_court.infrastructure.configuration.security;

import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.authorities = userEntity.getRole() != null ?
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().getName())) :
                Collections.emptyList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
