package com.hugorithm.hopfencraft.dto.user;

import com.hugorithm.hopfencraft.model.CartItem;
import com.hugorithm.hopfencraft.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicationUserDTO {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private List<CartItem> cartItems;
    private List<Order> orders;
}
