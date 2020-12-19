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

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2User oAuth2User= (CustomOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getEmail();
        String name= oAuth2User.getName();
        String username= oAuth2User.getusername();

        User user= userService.findByEmail(email);

        if(user== null){
            userService.createUserOAuth2(email,name, AuthenticationProvider.GOOGLE);
        }else {
            userService.updateUser(user);

        }


//        System.out.println("username :"+ username);
//        System.out.println("email :"+ email);
//        System.out.println("name :"+ name);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
