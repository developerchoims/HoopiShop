package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "address")
public class Address {
    @Id
    @Column(name = "address_code", nullable = false)
    private String addressCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "code", nullable = false)
    private User code;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Setter
    @ColumnDefault("'Y'")
    @Column(name = "main", length = 1)
    private String main = "Y";

    @JsonManagedReference
    @OneToMany(mappedBy = "addressCode")
    private Set<Delivery> deliveries = new LinkedHashSet<>();

    @Size(max = 24)
    @NotNull
    @Column(name = "address_name", nullable = false, length = 24)
    private String addressName;

    @Size(max = 20)
    @Column(name = "address_phone", length = 20)
    private String addressPhone;

    @Size(max = 20)
    @NotNull
    @Column(name = "postcode", nullable = false, length = 20)
    private String postcode;


    @Builder
    public Address(String addressCode, User code, String address, String addressName, String addressPhone, String postcode) {
        this.addressCode = addressCode;
        this.code = code;
        this.address = address;
        this.addressName = addressName;
        this.addressPhone = addressPhone;
        this.postcode = postcode;
    }


}