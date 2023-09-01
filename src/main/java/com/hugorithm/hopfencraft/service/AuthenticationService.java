package com.hugorithm.hopfencraft.service;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.UserRepository;
import com.hugorithm.hopfencraft.dto.LoginResponseDTO;
import com.hugorithm.hopfencraft.repository.RoleRepository;

import javax.management.relation.RoleNotFoundException;

@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ApplicationUser registerUser(String username, String password){
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new NoSuchElementException("Role not Found"));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        return userRepository.save(new ApplicationUser(0L, username, encodedPassword, authorities));
    }

    public LoginResponseDTO login(String username, String password){

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found")), token);

        } catch(AuthenticationException e){
            throw new UsernameNotFoundException("user not found");
        }
    }

}