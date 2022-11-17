package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tags")
@Audited
public class Tag extends RepresentationModel<Tag> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NotBlank(message = "error.validation.blank")
    @Size(max = 45, message = "error.validation.size")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<GiftCertificate> giftCertificates;

    public Tag() {
    }

    public Tag(@NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String name) {
        this.name = name;
    }

    public Tag(@NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String name, List<GiftCertificate> giftCertificates) {
        this.name = name;
        this.giftCertificates = giftCertificates;
    }

    public Tag(Long id, @NotBlank(message = "error.validation.blank") @Size(max = 45, message = "error.validation.size") String name, List<GiftCertificate> giftCertificates) {
        this.id = id;
        this.name = name;
        this.giftCertificates = giftCertificates;
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

    public List<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificate> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(giftCertificates, tag.giftCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, giftCertificates);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
