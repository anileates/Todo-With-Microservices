package com.aea.authservice.security;

import com.aea.authservice.model.User;
import com.aea.authservice.repository.UserRepository;
import com.aea.authservice.service.CustomUserDetailsService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    public Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private Boolean enabled = false;

    private CustomUserDetails(Long id, String username, String password, Boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER"));

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.isEnabled(), authorityList);
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
        return enabled;
    }

}
