package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @Column(name = "cart_code", nullable = false)
    private String cartCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "code", nullable = false)
    private User code;

    @JsonManagedReference
    @OneToMany(mappedBy = "cartCode")
    private Set<CartDetail> cartDetails = new LinkedHashSet<>();

    @Builder
    public Cart(String cartCode, User code) {
        this.cartCode = cartCode;
        this.code = code;
    }

}