package com.onbelay.dagnabitapp.auth.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class DagUser {

    private String name;
    private Collection<? extends GrantedAuthority> authorities;

    public DagUser(
            String name,
            Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.authorities = authorities;
    }

    public String getName() {
        return name;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
