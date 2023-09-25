package com.hugorithm.hopfencraft.config;

import com.hugorithm.hopfencraft.init.DataInitialization;
import com.hugorithm.hopfencraft.repository.ProductImageRepository;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestConfig {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public TestConfig(
            RoleRepository roleRepository,
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

    @Bean
    @Primary
    public DataInitialization testDataInitialization() {
        return new DataInitialization(
                roleRepository,
                userRepository,
                passwordEncoder,
                productRepository,
                productImageRepository
        );
    }
}
