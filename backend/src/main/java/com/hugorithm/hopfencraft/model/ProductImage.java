package com.hugorithm.hopfencraft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String path;


    public ProductImage(String name, String type, String path) {
        this.name = name;
        this.type = type;
        this.path = path;
    }
}
