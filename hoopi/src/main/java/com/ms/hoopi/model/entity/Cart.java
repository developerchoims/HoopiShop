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

    @Size(max = 1)
    @Column(name = "status", nullable = false, length = 1)
    private String status = "N";


    @Builder
    public Cart(String cartCode, User code, String status) {
        this.cartCode = cartCode;
        this.code = code;
        this.status = status;
    }



}