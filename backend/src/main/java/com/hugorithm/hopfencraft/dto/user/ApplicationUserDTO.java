package com.hugorithm.hopfencraft.dto.user;

import com.hugorithm.hopfencraft.dto.cart.CartItemDTO;
import com.hugorithm.hopfencraft.dto.order.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<String> roles;
    private List<CartItemDTO> cartItems;
    private List<OrderDTO> orders;
}
