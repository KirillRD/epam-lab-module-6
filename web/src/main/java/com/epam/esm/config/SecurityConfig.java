package com.epam.esm.config;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.security.UserDetailsServiceImpl;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String ROLE = "ROLE_";
    private static final String SUB = "sub";

    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userService);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http.formLogin()
                .defaultSuccessUrl("/successful-authentication", true)
                .and()
                .authenticationProvider(daoAuthenticationProvider);


        http
                .oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/oauth2/authorization/my-application-oidc")
                .defaultSuccessUrl("/successful-authentication", true)
        )
                .oauth2Client(withDefaults());

        http.logout().logoutUrl("/logout");

        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return (authorities) -> {
            GrantedAuthority authority = authorities.stream().findFirst().get();
            OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
            Map<String, Object> attributes = oauth2UserAuthority.getAttributes();

            String username = (String) attributes.get(SUB);
            Optional<User> optionalUser = userService.readUserByLogin(username);
            User user;
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            } else {
                user = new User();
                user.setLogin(username);
                user = userService.registration(user, false);
            }

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>(getAuthorities(user.getRoles()));
            return mappedAuthorities;
        };
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(ROLE + role.getName())).collect(Collectors.toSet());
    }
}
