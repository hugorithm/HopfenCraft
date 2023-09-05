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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
@EnableAsync
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
			ApplicationUser admin = new ApplicationUser("admin", passwordEncoder.encode("password"), "admin@admin.com", roles);

			userRepository.save(admin);

			//Add products
			LocalDateTime registerDateTime = LocalDateTime.now();

			Product p1 = new Product("Paulaner", "Paulaner", "Weiss", 10, new BigDecimal("2.39"), registerDateTime);
			Product p2 = new Product("Franziskaner", "Franziskaner", "Weiss", 31, new BigDecimal("2.29"), registerDateTime);
			Product p3 = new Product("La Choufe", "La Choufe", "Belgium Gold", 14, new BigDecimal("3.57"), registerDateTime);
			Product p4 = new Product("Benediktiner", "Benediktiner", "Weiss", 10, new BigDecimal("2.39"), registerDateTime);
			Product p5 = new Product("Spaten", "Spaten", "Weiss", 31, new BigDecimal("2.29"), registerDateTime);
			Product p6 = new Product("Ayinger", "Ayinger", "Belgium Gold", 14, new BigDecimal("3.57"), registerDateTime);
			Product p7 = new Product("Krombacher", "Krombacher", "Weiss", 10, new BigDecimal("2.39"), registerDateTime);
			Product p8 = new Product("Erdinger", "Erdinger", "Weiss", 31, new BigDecimal("2.29"), registerDateTime);
			Product p9 = new Product("Augistiner", "Augistiner", "Belgium Gold", 14, new BigDecimal("3.57"), registerDateTime);
			Product p10 = new Product("Kapunziner", "Kapunziner", "Weiss", 10, new BigDecimal("2.39"), registerDateTime);
			Product p11 = new Product("Munchener", "Munchener", "Weiss", 31, new BigDecimal("2.29"), registerDateTime);
			Product p12 = new Product("La Choufe 2", "La Choufe 2", "Belgium Gold", 14, new BigDecimal("3.57"), registerDateTime);

			List<Product> ps = Arrays.asList(p1, p2, p3,p4,p5,p6,p7,p8,p9,p10,p11,p12);
			productRepository.saveAll(ps);
		};
	}
}
