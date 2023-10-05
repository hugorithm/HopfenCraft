package com.hugorithm.hopfencraft.dto.shippingDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetailsDTO {
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
