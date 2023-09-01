package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.ApplicationUser;

public class LoginResponseDTO {
    private ApplicationUser user;
    private String jwt;

    public LoginResponseDTO(){
    }

    public LoginResponseDTO(ApplicationUser user, String jwt){
        this.user = user;
        this.jwt = jwt;
    }

    public ApplicationUser getUser(){
        return this.user;
    }

    public void setUser(ApplicationUser user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }
}
