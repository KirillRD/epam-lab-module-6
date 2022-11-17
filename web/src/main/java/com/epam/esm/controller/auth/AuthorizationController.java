package com.epam.esm.controller.auth;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    private final UserService userService;

    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/registration")
    public ResponseEntity<Void> registration(@RequestBody User user) {
        userService.registration(user, true);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/successful-authentication")
    public ResponseEntity<Void> successfulAuthentication() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
