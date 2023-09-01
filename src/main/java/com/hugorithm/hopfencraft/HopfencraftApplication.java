package com.hugorithm.hopfencraft;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Product;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.ProductRepository;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
public class HopfencraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(HopfencraftApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ProductRepository productRepository) {
		return args -> {
			//Add Roles
			if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			// Add Admin
			//Ugly way of adding an admin and roles.
			ApplicationUser admin = new ApplicationUser(1L, "admin", passwordEncoder.encode("password"), roles);

			userRepository.save(admin);

			//Add products

			Product p1 = new Product(1L, "Paulaner", "Weiss", 10, new BigDecimal("2.39"));
			Product p2 = new Product(2L, "Franziskaner", "Weiss", 31, new BigDecimal("2.29"));
			Product p3 = new Product(3L, "La Choufe", "Belgium Gold", 14, new BigDecimal("3.57"));
			Product p4 = new Product(4L, "Benediktiner", "Weiss", 10, new BigDecimal("2.39"));
			Product p5 = new Product(5L, "Spaten", "Weiss", 31, new BigDecimal("2.29"));
			Product p6 = new Product(6L, "Ayinger", "Belgium Gold", 14, new BigDecimal("3.57"));
			Product p7 = new Product(7L, "Krombacher", "Weiss", 10, new BigDecimal("2.39"));
			Product p8 = new Product(8L, "Erdinger", "Weiss", 31, new BigDecimal("2.29"));
			Product p9 = new Product(9L, "Augistiner", "Belgium Gold", 14, new BigDecimal("3.57"));
			Product p10 = new Product(10L, "Kapunziner", "Weiss", 10, new BigDecimal("2.39"));
			Product p11 = new Product(11L, "Munchener", "Weiss", 31, new BigDecimal("2.29"));
			Product p12 = new Product(12L, "La Choufe 2", "Belgium Gold", 14, new BigDecimal("3.57"));

			List<Product> ps = Arrays.asList(p1, p2, p3,p4,p5,p6,p7,p8,p9,p10,p11,p12);
			productRepository.saveAll(ps);
		};
	}
}
