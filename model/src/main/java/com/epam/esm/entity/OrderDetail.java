package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "orders_details")
@Audited
public class OrderDetail extends RepresentationModel<OrderDetail> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    @DecimalMin(value = "0.01", message = "error.validation.digits.min")
    @Digits(integer = 5, fraction = 2, message = "error.validation.price")
    private BigDecimal price;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gift_certificate_id", nullable = false)
    private GiftCertificate giftCertificate;

    public OrderDetail() {
    }

    public OrderDetail(Long id, @DecimalMin(value = "0.01", message = "error.validation.digits.min") @Digits(integer = 5, fraction = 2, message = "error.validation.price") BigDecimal price, Order order, GiftCertificate giftCertificate) {
        this.id = id;
        this.price = price;
        this.order = order;
        this.giftCertificate = giftCertificate;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(id, that.id) && Objects.equals(price, that.price) && Objects.equals(order, that.order) && Objects.equals(giftCertificate, that.giftCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, price, order, giftCertificate);
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", price=" + price +
                ", giftCertificate=" + giftCertificate +
                '}';
    }
}
