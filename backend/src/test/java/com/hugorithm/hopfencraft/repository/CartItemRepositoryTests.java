package com.hugorithm.hopfencraft.repository;

import com.hugorithm.hopfencraft.enums.AuthProvider;
import com.hugorithm.hopfencraft.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartItemRepositoryTests {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void CartItemRepository_SaveAll_ReturnSavedCartItem() {
        //Arrange
        Role role = new Role("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles, "Test", "test", AuthProvider.LOCAL);
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"), user);
        CartItem cartItem = new CartItem(product, user, 2, new BigDecimal("4.78"));

        //Act
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        //Assert
        Assertions.assertThat(savedCartItem).isNotNull();
        Assertions.assertThat(savedCartItem.getCartItemId()).isGreaterThan(0);
    }

    @Test
    public void CartItemRepository_GetAll_ReturnMoreThanOneCartItem() {
        //Arrange
        Role role = new Role("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles, "Test", "test", AuthProvider.LOCAL);
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"), user);
        CartItem cartItem = new CartItem(product, user, 2, new BigDecimal("4.78"));
        CartItem cartItem2 = new CartItem(product, user, 3, new BigDecimal("7.17"));

        //Act
        productRepository.save(product);
        roleRepository.save(role);
        userRepository.save(user);
        cartItemRepository.save(cartItem);
        cartItemRepository.save(cartItem2);

        List<CartItem> cartItems = cartItemRepository.findAll();

        //Assert
        Assertions.assertThat(cartItems).isNotNull();
        Assertions.assertThat(cartItems.size()).isEqualTo(2);

        CartItem firstCartItem = cartItems.get(0);
        Assertions.assertThat(firstCartItem.getProduct()).isEqualTo(product);
        Assertions.assertThat(firstCartItem.getUser()).isEqualTo(user);
    }

    @Test
    public void CartItemRepository_FindById_ReturnCartItem() {
        //Arrange
        Role role = new Role("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles, "Test", "test", AuthProvider.LOCAL);
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"), user);
        CartItem cartItem = new CartItem(product, user, 2, new BigDecimal("4.78"));

        //Act
        cartItemRepository.save(cartItem);
        CartItem repoCartItem = cartItemRepository.findById(cartItem.getCartItemId()).get();

        //Assert
        Assertions.assertThat(repoCartItem).isNotNull();
    }

    @Test
    public void CartItemRepository_UpdateCartItem_ReturnCartItemNotNull() {
        //Arrange
        Role role = new Role("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles, "Test", "test", AuthProvider.LOCAL);
        ApplicationUser user2 = new ApplicationUser("user2", "Password123!", "email@example.com", roles, "Test", "test", AuthProvider.LOCAL);
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"), user);
        Product product2 = new Product("Paulaner", "Paulaner Helles", "Lager", 10, new BigDecimal("2.59"), user);
        CartItem cartItem = new CartItem(product, user, 2, new BigDecimal("4.78"));
        int q = cartItem.getQuantity();
        Product p = cartItem.getProduct();
        ApplicationUser u = cartItem.getUser();
        LocalDateTime dt = cartItem.getAddedDateTime();

        //Act
        cartItemRepository.save(cartItem);

        CartItem repoCartItem = cartItemRepository.findById(cartItem.getCartItemId()).get();
        repoCartItem.setAddedDateTime(LocalDateTime.now());
        repoCartItem.setQuantity(5);
        repoCartItem.setProduct(product2);
        repoCartItem.setUser(user2);


        CartItem updatedCartItem = cartItemRepository.save(repoCartItem);

        //Assert
        Assertions.assertThat(updatedCartItem.getQuantity()).isNotNegative();
        Assertions.assertThat(updatedCartItem.getProduct()).isNotNull();
        Assertions.assertThat(updatedCartItem.getUser()).isNotNull();
        Assertions.assertThat(updatedCartItem.getQuantity()).isNotEqualTo(q);
        Assertions.assertThat(updatedCartItem.getProduct()).isNotEqualTo(p);
        Assertions.assertThat(updatedCartItem.getUser()).isNotEqualTo(u);
        Assertions.assertThat(updatedCartItem.getAddedDateTime()).isNotEqualTo(dt);
    }

    @Test
    public void CartItemRepository_CartItemDelete_ReturnCartItemIsEmpty() {
        //Arrange
        Role role = new Role("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        ApplicationUser user = new ApplicationUser("user1", "Password123!", "email@example.com", roles, "Test", "test", AuthProvider.LOCAL);
        Product product = new Product("Paulaner", "Paulaner Weissbier", "Weiss", 10, new BigDecimal("2.39"), user);
        CartItem cartItem = new CartItem(product, user, 2, new BigDecimal("4.78"));

        //Act
        cartItemRepository.save(cartItem);

        cartItemRepository.deleteById(cartItem.getCartItemId());
        Optional<CartItem> deletedCartItem = cartItemRepository.findById(cartItem.getCartItemId());

        //Assert
        Assertions.assertThat(deletedCartItem).isEmpty();
    }
}
