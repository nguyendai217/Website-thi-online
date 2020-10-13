package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.User;
import com.wru.onthi.repository.UserRepository;
import com.wru.onthi.utils.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(s);
        if (user != null){
            System.out.println(user.getEmail());
        }else
            {
            throw new UsernameNotFoundException("User not found !");
        }
        return new MyUserDetails(user);
    }
}
