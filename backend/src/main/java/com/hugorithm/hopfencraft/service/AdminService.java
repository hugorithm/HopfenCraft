package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.user.ApplicationUserDTO;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService {
    private final UserRepository userRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;

    public ResponseEntity<List<ApplicationUserDTO>> getUsers() {
        try {
            List<ApplicationUser> users = userRepository.findAll();

            List<ApplicationUserDTO> applicationUserDTOS = users.stream()
                    .map(applicationUser -> new ApplicationUserDTO(
                            applicationUser.getUserId(),
                            applicationUser.getUsername(),
                            applicationUser.getEmail(),
                            applicationUser.getFirstName(),
                            applicationUser.getLastName(),
                            applicationUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                            shoppingCartService.convertCartItemListToCartItemDTOList(applicationUser.getCartItems()),
                            orderService.convertOrderListIntoOrderDTOList(applicationUser.getOrders())
                    ))
                    .toList();

            return ResponseEntity.ok(applicationUserDTOS);
        } catch (UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<ApplicationUserDTO> getUserById(Long userId) {
        try {
            ApplicationUser user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return ResponseEntity.ok(new ApplicationUserDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                    shoppingCartService.convertCartItemListToCartItemDTOList(user.getCartItems()),
                    orderService.convertOrderListIntoOrderDTOList(user.getOrders())
            ));
        } catch (UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<ApplicationUserDTO> getUserByUsername(String username) {
        try {
            ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return ResponseEntity.ok(new ApplicationUserDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                    shoppingCartService.convertCartItemListToCartItemDTOList(user.getCartItems()),
                    orderService.convertOrderListIntoOrderDTOList(user.getOrders())
            ));
        } catch (UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        }
    }

}
