package com.hugorithm.hopfencraft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipping_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShippingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_details_id")
    private Long shippingDetailsId;
    private String shippingName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;

    // Billing Address fields
    private String billingName;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingPostalCode;
    private String billingCountry;

    public ShippingDetails(String shippingName,
                           String shippingAddress,
                           String shippingCity,
                           String shippingState,
                           String shippingPostalCode,
                           String shippingCountry,
                           String billingName,
                           String billingAddress,
                           String billingCity,
                           String billingState,
                           String billingPostalCode,
                           String billingCountry
    ) {
        this.shippingName = shippingName;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingState = shippingState;
        this.shippingPostalCode = shippingPostalCode;
        this.shippingCountry = shippingCountry;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingState = billingState;
        this.billingPostalCode = billingPostalCode;
        this.billingCountry = billingCountry;
    }
}
