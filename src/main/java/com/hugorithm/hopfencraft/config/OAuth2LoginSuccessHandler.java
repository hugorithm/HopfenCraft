package com.hugorithm.hopfencraft.config;

import com.hugorithm.hopfencraft.enums.AuthProvider;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.RoleRepository;
import com.hugorithm.hopfencraft.repository.UserRepository;
import com.hugorithm.hopfencraft.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;

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
            String username = attributes.getOrDefault("sub", "").toString();

            userRepository.findByUsername(username)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User newUser = new DefaultOAuth2User(user.getAuthorities(), attributes, "sub");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, user.getAuthorities(), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        ApplicationUser userEntity = createUser(email, firstName);
                        userEntity.setLastName(lastName);
                        userEntity.setUsername(username);
                        userEntity.setAuthProvider(AuthProvider.GOOGLE);
                        userEntity.setAttributes(convertMapToStringValues(attributes));
                        userRepository.save(userEntity);
                        DefaultOAuth2User newUser = new DefaultOAuth2User(userEntity.getAuthorities(), attributes, "sub");
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
            String id = attributes.getOrDefault("id", "").toString();

            userRepository.findByUsername(id)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User newUser = new DefaultOAuth2User(user.getAuthorities(), attributes, "id");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, user.getAuthorities(), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        ApplicationUser userEntity = createUser(email, name);
                        userEntity.setUsername(id);
                        userEntity.setAttributes(convertMapToStringValues(attributes));
                        userEntity.setAuthProvider(AuthProvider.GITHUB);

                        userRepository.save(userEntity);
                        DefaultOAuth2User newUser = new DefaultOAuth2User(userEntity.getAuthorities(), attributes, "id");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, userEntity.getAuthorities(),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }


        String targetUrl = determineTargetUrl(request, response, authentication);
        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(targetUrl);

        super.onAuthenticationSuccess(request, response, authentication);

    }

    private ApplicationUser createUser(String email, String firstName) {
        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleRepository.findByAuthority("USER");
        role.ifPresent(roles::add);
        ApplicationUser userEntity = new ApplicationUser();
        userEntity.setAuthorities(roles);
        userEntity.setEmail(email);
        userEntity.setFirstName(firstName);
        return userEntity;
    }


    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = HttpCookieOAuth2AuthorizationRequestRepository.CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);


        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = tokenService.generateOAuth2Jwt(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    public static Map<String, String> convertMapToStringValues(Map<String, Object> originalMap) {
        Map<String, String> convertedMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Convert the value to a String
            String stringValue = (value != null) ? value.toString() : null;

            // Add the key-value pair to the new map
            convertedMap.put(key, stringValue);
        }

        return convertedMap;
    }

}
