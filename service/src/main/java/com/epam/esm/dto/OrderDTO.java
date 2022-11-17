package com.epam.esm.dto;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderDetail;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class OrderDTO extends RepresentationModel<OrderDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private BigDecimal price;
    private Timestamp purchaseTime;
    private List<OrderDetail> orderDetails;

    public OrderDTO() {
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.price = order.getPrice();
        this.purchaseTime = order.getPurchaseTime();
        this.orderDetails = order.getOrderDetails();
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
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id) && Objects.equals(price, orderDTO.price) && Objects.equals(purchaseTime, orderDTO.purchaseTime) && Objects.equals(orderDetails, orderDTO.orderDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, price, purchaseTime, orderDetails);
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", price=" + price +
                ", purchaseTime=" + purchaseTime +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
