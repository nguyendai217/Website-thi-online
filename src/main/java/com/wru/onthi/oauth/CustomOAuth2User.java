package com.wru.onthi.oauth;

import com.wru.onthi.entity.User;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;
    private String provider;



    public OAuth2User getoAuth2User() {
        return oAuth2User;
    }

    public void setoAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public CustomOAuth2User(OAuth2User oAuth2User, String provider) {
        this.oAuth2User = oAuth2User;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        String email= oAuth2User.getAttribute("email");
//        CustomOAuth2UserService customOAuth2UserService= new CustomOAuth2UserService();
//        String provider= this.provider;
//        String username= customOAuth2UserService.getUserName(email,this.provider.toUpperCase());
//        return username;
        return email;
    }

    public String getUsername(){
        Date date= new Date();
        long time= date.getTime();
        String strName= String.valueOf(time);
        String username= "user"+strName;
        return username;
    }

    public String getFullname(){
        return oAuth2User.getAttribute("name");
    }
    public String getEmail(){
        return oAuth2User.getAttribute("email");
    }

    public String getImage(){
        return oAuth2User.getAttribute("picture");
    }
}
