package com.epam.esm.controller;

import com.epam.esm.entity.RoleName;
import com.epam.esm.entity.User;
import com.epam.esm.exception.AuthorizationException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.PageUtil;
import com.epam.esm.entity.Order;
import com.epam.esm.hateoas.LinkBuilder;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PageUtil pageUtil;
    private final LinkBuilder<Order> linkBuilder;
    private final UserService userService;
    private final SecurityUtil securityUtil;

    @Autowired
    public OrderController(
            OrderService orderService,
            PageUtil pageUtil,
            LinkBuilder<Order> linkBuilder,
            UserService userService,
            SecurityUtil securityUtil) {
        this.orderService = orderService;
        this.pageUtil = pageUtil;
        this.linkBuilder = linkBuilder;
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Order order) {
        Long id = userService.readByLogin(securityUtil.getUsername()).getId();
        if (order.getUser() == null) {
            order.setUser(new User());
            order.getUser().setId(id);
        } else if (!order.getUser().getId().equals(id)) {
            throw new AuthorizationException();
        }
        orderService.create(order);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Order> readById(@PathVariable Long id) {
        Order order = orderService.readById(id);
        if (securityUtil.hasOneRole(RoleName.USER)) {
            if (!order.getUser().getId().equals(userService.readByLogin(securityUtil.getUsername()).getId())) {
                throw new AuthorizationException();
            }
        }
        linkBuilder.buildLinks(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Iterable<Order>> readAll() {
        Iterable<Order> orders = orderService.readAll();
        orders.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Iterable<Order>> read(
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "size", required = false) String size) {
        Iterable<Order> orders = orderService.read(pageUtil.getPage(page), pageUtil.getSize(size));
        orders.forEach(linkBuilder::buildLinks);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        Order order = orderService.readById(id);
        if (!order.getUser().getId().equals(userService.readByLogin(securityUtil.getUsername()).getId())) {
            throw new AuthorizationException();
        }
        orderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
