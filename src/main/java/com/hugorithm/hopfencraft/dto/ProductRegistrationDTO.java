package com.hugorithm.hopfencraft.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductRegistrationDTO {
    private String brand;
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;

    public ProductRegistrationDTO() {
    }

    public ProductRegistrationDTO(String brand, String name, String description, int quantity, BigDecimal price) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRegistrationDTO that = (ProductRegistrationDTO) o;
        return quantity == that.quantity && Objects.equals(brand, that.brand) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, name, description, quantity, price);
    }

    @Override
    public String toString() {
        return "ProductRegistrationDTO{" +
                "brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
