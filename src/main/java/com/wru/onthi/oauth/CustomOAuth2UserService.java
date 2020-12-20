package com.wru.onthi.oauth;

import com.wru.onthi.entity.User;
import com.wru.onthi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    UserRepository userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user= super.loadUser(userRequest);
        String provider= userRequest.getClientRegistration().getClientName();
        //provider Facebook, Google
        return new CustomOAuth2User(user,provider);
    }

    public String getUserName(String email, String provider){
        User user = userService.findByEmailAndProvider(email,provider);
        String username= user.getUsername();
        return username;
    }


}
