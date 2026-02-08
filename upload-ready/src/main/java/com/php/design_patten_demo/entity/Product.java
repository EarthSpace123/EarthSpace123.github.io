package com.php.design_patten_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String author;

    @Column(length = 100)
    private String publisher;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private Integer stock = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 50)
    private String category;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(length = 20)
    private String isbn;

    @Column(name = "publish_date")
    private String publishDate;

    @Column(length = 20)
    private String language = "中文";

    @Column
    private Integer pages;

    @Column(length = 20)
    private String binding;

    @Column(length = 20)
    private String format;

    @Column(precision = 8, scale = 2)
    private java.math.BigDecimal weight;

    @Column
    private Integer sales = 0;

    @Column(precision = 3, scale = 2)
    private java.math.BigDecimal rating = java.math.BigDecimal.ZERO;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
