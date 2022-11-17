package com.epam.esm.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Audited
public class Order extends RepresentationModel<Order> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    @DecimalMin(value = "0.01", message = "error.validation.digits.min")
    @Digits(integer = 9, fraction = 2, message = "error.validation.order-price")
    private BigDecimal price;

    @Column(name = "purchase_time")
    @CreationTimestamp
    private Timestamp purchaseTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderDetail> orderDetails;

    public Order() {
    }

    public Order(Long id, @DecimalMin(value = "0.01", message = "error.validation.digits.min") @Digits(integer = 5, fraction = 2, message = "error.validation.digits") BigDecimal price, Timestamp purchaseTime, User user, List<OrderDetail> orderDetails) {
        this.id = id;
        this.price = price;
        this.purchaseTime = purchaseTime;
        this.user = user;
        this.orderDetails = orderDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Timestamp purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(price, order.price) && Objects.equals(purchaseTime, order.purchaseTime) && Objects.equals(user, order.user) && Objects.equals(orderDetails, order.orderDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, price, purchaseTime, user, orderDetails);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", purchaseTime=" + purchaseTime +
                ", user=" + user +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
