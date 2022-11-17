package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gift_certificates")
@Audited
public class GiftCertificate extends RepresentationModel<GiftCertificate> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NotBlank(message = "error.validation.blank")
    @Size(max = 45, message = "error.validation.size")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "error.validation.blank")
    @Size(max = 45, message = "error.validation.size")
    private String description;

    @Column(name = "price")
    @DecimalMin(value = "0.01", message = "error.validation.digits.min")
    @Digits(integer = 5, fraction = 2, message = "error.validation.price")
    private BigDecimal price;

    @Column(name = "duration")
    @Min(value = 1, message = "error.validation.min")
    private int duration;

    @Column(name = "create_date")
    @CreationTimestamp
    private Timestamp createDate;

    @Column(name = "last_update_date")
    @UpdateTimestamp
    private Timestamp lastUpdateDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "gift_certificates_have_tags",
            joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private List<Tag> tags;

    @JsonIgnore
    @OneToMany(mappedBy = "giftCertificate")
    private List<OrderDetail> orderDetails;

    public GiftCertificate() {
    }

    public GiftCertificate(@NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String name, @NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String description, @DecimalMin(value = "0.01", message = "error.validation.digits.min") @Digits(integer = 5, fraction = 2, message = "error.validation.digits") BigDecimal price, @Min(value = 1, message = "error.validation.min") int duration, Timestamp createDate, Timestamp lastUpdateDate, List<Tag> tags, List<OrderDetail> orderDetails) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
        this.orderDetails = orderDetails;
    }

    public GiftCertificate(Long id, @NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String name, @NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String description, @DecimalMin(value = "0.01", message = "error.validation.digits.min") @Digits(integer = 5, fraction = 2, message = "error.validation.digits") BigDecimal price, @Min(value = 1, message = "error.validation.min") int duration, Timestamp createDate, Timestamp lastUpdateDate, List<Tag> tags, List<OrderDetail> orderDetails) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
        this.orderDetails = orderDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
        GiftCertificate that = (GiftCertificate) o;
        return duration == that.duration && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate) && Objects.equals(tags, that.tags) && Objects.equals(orderDetails, that.orderDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, price, duration, createDate, lastUpdateDate, tags, orderDetails);
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
    }
}
