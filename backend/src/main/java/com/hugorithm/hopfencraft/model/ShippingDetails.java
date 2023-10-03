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

}
