package com.fieldcommand.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fieldcommand.role.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class UserPrincipal implements UserDetails{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Long id;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    private List<String> simpleAuthorities = new ArrayList<>();

    private String roleType;

    private int rolePower;

    private UserPrincipal(Long id, String name, String roleType, int rolePower, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {

        this.id = id;
        this.username = username;
        this.roleType = roleType;
        this.rolePower = rolePower;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        createSimpleAuthList(authorities);
    }

    static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRole().getRoleType().getAuthorities()
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleType().toString()));

        return new UserPrincipal(
                user.getId(),
                "",
                user.getRole().getRoleType().toString(),
                user.getRole().getPower(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    private void createSimpleAuthList(Collection<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority: authorities
             ) {
            this.simpleAuthorities.add(authority.getAuthority());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return !this.roleType.equals(String.valueOf(RoleType.ROLE_DISABLED));
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public List<String> getSimpleAuthorities() {
        return simpleAuthorities;
    }

    public void setSimpleAuthorities(List<String> simpleAuthorities) {
        this.simpleAuthorities = simpleAuthorities;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public int getRolePower() {
        return rolePower;
    }

    public void setRolePower(int rolePower) {
        this.rolePower = rolePower;
    }
}
