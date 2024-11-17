package com.yanvelasco.ecommerce.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yanvelasco.ecommerce.domain.user.entities.RoleEntity;
import com.yanvelasco.ecommerce.domain.user.entities.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsIpm implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private UUID id;

    private String userName;

    @Getter
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsIpm(UUID id, String userName, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsIpm build(UserEntity user) {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for (RoleEntity role : user.getRoles()) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getRoleName().name());
            list.add(simpleGrantedAuthority);
        }

        return new UserDetailsIpm(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                list
        );
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
        return userName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetailsIpm user = (UserDetailsIpm) o;
        return id.equals(user.id);
    }

}