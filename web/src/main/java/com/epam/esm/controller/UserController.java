package com.epam.esm.controller;

import com.epam.esm.entity.RoleName;
import com.epam.esm.exception.AuthorizationException;
import com.epam.esm.util.PageUtil;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.hateoas.LinkBuilder;
import com.epam.esm.service.UserService;
import com.epam.esm.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PageUtil pageUtil;
    private final LinkBuilder<UserDTO> linkBuilder;
    private final SecurityUtil securityUtil;

    @Autowired
    public UserController(
            UserService userService,
            PageUtil pageUtil,
            LinkBuilder<UserDTO> linkBuilder,
            SecurityUtil securityUtil) {
        this.userService = userService;
        this.pageUtil = pageUtil;
        this.linkBuilder = linkBuilder;
        this.securityUtil = securityUtil;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> readById(@PathVariable Long id) {
        if (securityUtil.hasOneRole(RoleName.USER)) {
            if (!id.equals(userService.readByLogin(securityUtil.getUsername()).getId())) {
                throw new AuthorizationException();
            }
        }
        UserDTO user = userService.readById(id);
        linkBuilder.buildLinks(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Iterable<UserDTO>> readAll() {
        Iterable<UserDTO> users = userService.readAll();
        users.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Iterable<UserDTO>> read(
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "size", required = false) String size) {
        Iterable<UserDTO> users = userService.read(pageUtil.getPage(page), pageUtil.getSize(size));
        users.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
