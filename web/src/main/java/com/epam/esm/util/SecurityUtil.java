package com.epam.esm.util;

import com.epam.esm.entity.RoleName;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityUtil {

    private static final String ROLE = "ROLE_";
    private static final String ANONYMOUS = "ANONYMOUS";

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities();
    }

    public boolean hasRole(RoleName roleName) {
        return getRoles().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equals(ROLE + roleName));
    }

    public boolean hasOneRole(RoleName roleName) {
        Collection<? extends GrantedAuthority> roles = getRoles();
        return roles.size() == 1 &&
               roles.stream().map(GrantedAuthority::getAuthority).findFirst().get().equals(ROLE + roleName);
    }

    public boolean isAnonymous() {
        Collection<? extends GrantedAuthority> roles = getRoles();
        return roles.size() == 1 &&
               roles.stream().map(GrantedAuthority::getAuthority).findFirst().get().equals(ROLE + ANONYMOUS);
    }
}
