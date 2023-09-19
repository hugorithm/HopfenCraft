package com.hugorithm.hopfencraft.config;

import com.hugorithm.hopfencraft.enums.AuthProvider;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("google")) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String firstName = attributes.getOrDefault("given_name", "").toString();
            String lastName = attributes.getOrDefault("family_name", "").toString();

            userRepository.findByEmail(email)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User newUser = new DefaultOAuth2User(user.getAuthorities(), attributes, "at_hash");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, user.getAuthorities(), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        Set<Role> roles = new HashSet<>();
                        Optional<Role> role = roleRepository.findByAuthority("USER");
                        role.ifPresent(roles::add);
                        ApplicationUser userEntity = new ApplicationUser();
                        userEntity.setAuthorities(roles);
                        userEntity.setEmail(email);
                        userEntity.setFirstName(firstName);
                        userEntity.setLastName(lastName);
                        userEntity.setUsername(UUID.randomUUID().toString());
                        userEntity.setAuthProvider(AuthProvider.GOOGLE);

                        userRepository.save(userEntity);
                        DefaultOAuth2User newUser = new DefaultOAuth2User(userEntity.getAuthorities(), attributes, "at_hash");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, userEntity.getAuthorities(),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        } else if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("github")) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();
            String username = attributes.getOrDefault("login", "").toString();

            userRepository.findByEmail(email)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User newUser = new DefaultOAuth2User(user.getAuthorities(), attributes, "id");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, user.getAuthorities(), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        Set<Role> roles = new HashSet<>();
                        Optional<Role> role = roleRepository.findByAuthority("USER");
                        role.ifPresent(roles::add);
                        ApplicationUser userEntity = new ApplicationUser();
                        userEntity.setAuthorities(roles);
                        userEntity.setEmail(email);
                        userEntity.setFirstName(name);
                        userEntity.setUsername(username);
                        userEntity.setAuthProvider(AuthProvider.GITHUB);

                        userRepository.save(userEntity);
                        DefaultOAuth2User newUser = new DefaultOAuth2User(userEntity.getAuthorities(), attributes, "id");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, userEntity.getAuthorities(),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl + "/login");
        super.onAuthenticationSuccess(request, response, authentication);

    }
}
