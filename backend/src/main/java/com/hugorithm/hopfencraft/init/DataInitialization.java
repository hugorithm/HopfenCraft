package com.hugorithm.hopfencraft.init;

import com.hugorithm.hopfencraft.enums.AuthProvider;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitialization implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

    @Autowired
    public DataInitialization(RoleRepository roleRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              ProductRepository productRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // Add Roles
        if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
        Role adminRole = roleRepository.save(new Role("ADMIN"));
        Role userRole = roleRepository.save(new Role("USER"));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        // Add Admin and Test User
        // Ugly way of adding an admin and roles.
        ApplicationUser admin = new ApplicationUser("admin", passwordEncoder.encode("Password123!"), "admin@admin.com", roles, "Hugo", "Silva", AuthProvider.LOCAL);
        ApplicationUser user = new ApplicationUser("testuser", passwordEncoder.encode("Password123!"), "user@test.com", userRoles, "Test", "tester", AuthProvider.LOCAL);

        userRepository.save(admin);
        userRepository.save(user);

        //Add products

        Product p1 = new Product("Paulaner", "Paulaner Weissbier", "German Hefe Weissbier, 5,5%. 50 cl", 10, new BigDecimal("2.06"), admin);
        Product p2 = new Product("Weihenstephaner", "Weihenstephaner Hefeweissbier", "Blonde beer from Gemrany. 5,4%. 50 cl. ", 31, new BigDecimal("1.90"), admin);
        Product p3 = new Product("Franziskaner", "Franziskaner Weissbier Naturtrüb", "German wheat beer, Weissbier style, 5%. 50 cl", 14, new BigDecimal("1.57"), admin);
        Product p4 = new Product("Schneider", "Schneider Weisse Aventinus Tap 6", "Bavaria's oldest wheat Doppelbock, 8,2%. 50 cl", 10, new BigDecimal("2.19"), admin);
        Product p5 = new Product("Ayinger", "Ayinger Lager Hell", "Typical Munich Hell Lager, mild and very pleasant taste, 4.9%. 50 cl.", 31, new BigDecimal("2.09"), admin);
        Product p6 = new Product("Ayinger", "Ayinger Braüweisse", "Classic Bavarian wheat-beer, 5,1%. 50 cl", 14, new BigDecimal("2.50"), admin);
        Product p7 = new Product("Schneider", "Schneider Weisse Tap 1 Meine helle Weisse", "German Hefeweizen, 5,2%. 50 cl", 10, new BigDecimal("1.98"), admin);
        Product p8 = new Product("Warsteiner", "Warsteiner Premium Beer", "German Pilsner made from the highest quality ingredients, 4.8%, 33 cl", 31, new BigDecimal("2.01"), admin);
        Product p9 = new Product("Spaten", "Spaten Münchner Hell", "German wheat beer brewed with German hops, 5.2%. 50 cl.", 14, new BigDecimal("2.06"), admin);
        Product p10 = new Product("Ayinger", "Ayinger Urweisse", "Wheat beer with an amber colour and an intense and complex character, 5.8%. 50 cl.", 10, new BigDecimal("2.53"), admin);
        Product p11 = new Product("Chouffe", "La Chouffe", "Belgian Strong Pale Ale, 8%. 33 cl.", 31, new BigDecimal("3.79"), admin);
        Product p12 = new Product("Erdinger", "Erdinger Weissbier", "Classic Bavarian Weissbier, fermented in the bottle 5.3%, 50 cl", 14, new BigDecimal("2.11"), admin);

        List<Product> ps = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
        productRepository.saveAll(ps);
    }
}
