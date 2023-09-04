package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final UserRepository userRepository;
    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public ApplicationUser getUserFromJwt(Jwt jwt) {
        String username = jwt.getSubject();
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
