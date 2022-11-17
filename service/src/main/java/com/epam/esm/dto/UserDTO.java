package com.epam.esm.dto;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleName;
import com.epam.esm.entity.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO extends RepresentationModel<UserDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String login;
    private String name;
    private String address;
    private String phone;
    private Set<RoleName> roles;
    private List<OrderDTO> orders;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(user.getOrders())) {
            this.orders = user.getOrders().stream().map(OrderDTO::new).collect(Collectors.toList());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<RoleName> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleName> roles) {
        this.roles = roles;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(login, userDTO.login) && Objects.equals(name, userDTO.name) && Objects.equals(address, userDTO.address) && Objects.equals(phone, userDTO.phone) && Objects.equals(roles, userDTO.roles) && Objects.equals(orders, userDTO.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, login, name, address, phone, roles, orders);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", roles=" + roles +
                ", orders=" + orders +
                '}';
    }
}
