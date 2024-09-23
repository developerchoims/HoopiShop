package com.ms.hoopi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_img")
public class ProductImg {
    @Id
    @Column(name = "product_img_code", nullable = false)
    private String productImgCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_code", nullable = false)
    private Product productCode;

    @Size(max = 255)
    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Size(max = 255)
    @Column(name = "img_path")
    private String imgPath;

    @Size(max = 500)
    @NotNull
    @Column(name = "img_key", nullable = false)
    private String imgKey;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(name = "main", nullable = false)
    private Integer main;



    @Builder
    public ProductImg(String productImgCode, Product productCode, String imgPath, String fileName, String imgKey, Integer main) {
        this.productImgCode = productImgCode;
        this.productCode = productCode;
        this.imgPath = imgPath;
        this.fileName = fileName;
        this.imgKey = imgKey;
        this.main = main;
    }
}