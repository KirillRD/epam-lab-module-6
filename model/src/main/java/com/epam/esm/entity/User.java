package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.Audited;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Audited
public class User extends RepresentationModel<User> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true)
    @Size(max = 45, message = "error.validation.size")
    @NotBlank(message = "error.validation.blank")
    @Email(message = "error.validation.email")
    private String login;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "name")
    @Size(max = 45, message = "error.validation.size")
    private String name;

    @Column(name = "address")
    @Size(max = 45, message = "error.validation.size")
    private String address;

    @Column(name = "phone")
    @Pattern(regexp="^$|^\\+(?:[0-9] ?){6,14}[0-9]$", message = "error.validation.phone")
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "users_have_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User() {
    }

    public User(Long id, @Size(max = 45, message = "error.validation.size") @Email(message = "error.validation.email") String login, String password, @NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String name, @Size(max = 45, message = "error.validation.size") String address, @Pattern(regexp = "^$|^\\+(?:[0-9] ?){6,14}[0-9]$", message = "error.validation.phone") String phone, Set<Role> roles, List<Order> orders) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.roles = roles;
        this.orders = orders;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(address, user.address) && Objects.equals(phone, user.phone) && Objects.equals(roles, user.roles) && Objects.equals(orders, user.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, login, password, name, address, phone, roles, orders);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", roles=" + roles +
                '}';
    }
}
