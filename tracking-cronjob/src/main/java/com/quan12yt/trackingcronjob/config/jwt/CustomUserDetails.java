package com.quan12yt.trackingcronjob.config.jwt;

import com.quan12yt.trackingcronjob.model.Accounts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Accounts accounts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = accounts.getRoles()
                .stream()
                .map(i -> new SimpleGrantedAuthority(i.getRoleName()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return accounts.getPassword();
    }

    @Override
    public String getUsername() {
        return accounts.getUserName();
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