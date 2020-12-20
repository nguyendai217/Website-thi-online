package com.wru.onthi.oauth;

import com.wru.onthi.entity.AuthenticationProvider;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2User oAuth2User= (CustomOAuth2User) authentication.getPrincipal();
        String provider = oAuth2User.getProvider();

        String email = oAuth2User.getEmail();
        String fullname= oAuth2User.getFullname();
        String username= oAuth2User.getName();

        User user = userService.findByEmailAndProvider(email,provider.toUpperCase());

        System.out.println("provider :" + provider);

        if(provider.toUpperCase().equals("FACEBOOK")){
            if(user== null){
                userService.createUserOAuth2(email,fullname,username, AuthenticationProvider.FACEBOOK);
            }else {
                userService.updateUserOauth(email,fullname, username, AuthenticationProvider.FACEBOOK);
            }
        }else {
            if(user== null){
                userService.createUserOAuth2(email,fullname, username,AuthenticationProvider.GOOGLE);
            }else {
                userService.updateUserOauth(email, fullname,username, AuthenticationProvider.GOOGLE);
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
