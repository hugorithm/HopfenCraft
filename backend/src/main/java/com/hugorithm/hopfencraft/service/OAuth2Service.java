package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.user.OAuth2ApplicationUserDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OAuth2Service {
    private final JwtService jwtService;
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final static Logger LOGGER = LoggerFactory.getLogger(OAuth2Service.class);

    public ResponseEntity<OAuth2ApplicationUserDTO> getOAuth2User(Jwt jwt) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);

            return ResponseEntity.ok(
                    new OAuth2ApplicationUserDTO(
                            user.getUserId(),
                            user.getName(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getFirstName(),
                            user.getLastName(),
                            shoppingCartService.convertCartItemListToCartItemDTOList(user.getCartItems()),
                            orderService.ConvertOrderListIntoOrderDTOList(user.getOrders()),
                            user.getAttributes()
                    ));
        } catch (UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
