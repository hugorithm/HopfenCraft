package com.hugorithm.hopfencraft.init;

import com.hugorithm.hopfencraft.enums.AuthProvider;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.model.ProductImage;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.ProductImageRepository;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final ProductImageRepository productImageRepository;

    @Value("${upload.directory}")
    private String FOLDER_PATH;

    @Autowired
    public DataInitialization(RoleRepository roleRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              ProductRepository productRepository,
                              ProductImageRepository productImageRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
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

        //Add images
        ProductImage pi1 = new ProductImage("p1.png","image/png",FOLDER_PATH + "p1.png");
        ProductImage pi2 = new ProductImage("p2.png","image/png",FOLDER_PATH + "p2.png");
        ProductImage pi3 = new ProductImage("p3.png","image/png",FOLDER_PATH + "p3.png");
        ProductImage pi4 = new ProductImage("p4.png","image/webp",FOLDER_PATH + "p4.webp");
        ProductImage pi5 = new ProductImage("p5.png","image/webp",FOLDER_PATH + "p5.webp");
        ProductImage pi6 = new ProductImage("p6.png","image/png",FOLDER_PATH + "p6.png");
        ProductImage pi7 = new ProductImage("p7.png","image/png",FOLDER_PATH + "p7.png");
        ProductImage pi8 = new ProductImage("p8.png","image/png",FOLDER_PATH + "p8.png");
        ProductImage pi9 = new ProductImage("p9.png","image/png",FOLDER_PATH + "p9.png");
        ProductImage pi10 = new ProductImage("p10.png","image/png",FOLDER_PATH + "p10.png");
        ProductImage pi11 = new ProductImage("p11.png","image/png",FOLDER_PATH + "p11.png");
        ProductImage pi12 = new ProductImage("p12.png","image/png",FOLDER_PATH + "p12.png");


        //Add products

        Product p1 = new Product("Paulaner", "Paulaner Weissbier", "German Hefe Weissbier, 5,5%. 50 cl", 10, new BigDecimal("2.06"), admin, pi1);
        Product p2 = new Product("Weihenstephaner", "Weihenstephaner Hefeweissbier", "Blonde beer from Gemrany. 5,4%. 50 cl. ", 31, new BigDecimal("1.90"), admin, pi2);
        Product p3 = new Product("Franziskaner", "Franziskaner Weissbier Naturtrüb", "German wheat beer, Weissbier style, 5%. 50 cl", 14, new BigDecimal("1.57"), admin, pi3);
        Product p4 = new Product("Schneider", "Schneider Weisse Aventinus Tap 6", "Bavaria's oldest wheat Doppelbock, 8,2%. 50 cl", 10, new BigDecimal("2.19"), admin, pi4);
        Product p5 = new Product("Ayinger", "Ayinger Lager Hell", "Typical Munich Hell Lager, mild and very pleasant taste, 4.9%. 50 cl.", 31, new BigDecimal("2.09"), admin, pi5);
        Product p6 = new Product("Ayinger", "Ayinger Braüweisse", "Classic Bavarian wheat-beer, 5,1%. 50 cl", 14, new BigDecimal("2.50"), admin, pi6);
        Product p7 = new Product("Schneider", "Schneider Weisse Tap 1 Meine helle Weisse", "German Hefeweizen, 5,2%. 50 cl", 10, new BigDecimal("1.98"), admin, pi7);
        Product p8 = new Product("Warsteiner", "Warsteiner Premium Beer", "German Pilsner made from the highest quality ingredients, 4.8%, 33 cl", 31, new BigDecimal("2.01"), admin, pi8);
        Product p9 = new Product("Spaten", "Spaten Münchner Hell", "German wheat beer brewed with German hops, 5.2%. 50 cl.", 14, new BigDecimal("2.06"), admin, pi9);
        Product p10 = new Product("Ayinger", "Ayinger Urweisse", "Wheat beer with an amber colour and an intense and complex character, 5.8%. 50 cl.", 10, new BigDecimal("2.53"), admin, pi10);
        Product p11 = new Product("Chouffe", "La Chouffe", "Belgian Strong Pale Ale, 8%. 33 cl.", 31, new BigDecimal("3.79"), admin, pi11);
        Product p12 = new Product("Erdinger", "Erdinger Weissbier", "Classic Bavarian Weissbier, fermented in the bottle 5.3%, 50 cl", 14, new BigDecimal("2.11"), admin, pi12);

        List<Product> ps = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
        productRepository.saveAll(ps);
    }
}
