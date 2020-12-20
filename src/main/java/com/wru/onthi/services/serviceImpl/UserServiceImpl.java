package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.AuthenticationProvider;
import com.wru.onthi.entity.Role;
import com.wru.onthi.entity.User;
import com.wru.onthi.repository.RoleRepository;
import com.wru.onthi.repository.UserRepository;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void disableUser(Integer userId) {
        userRepository.disableUser(userId);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return userRepository.findAllUser(pageable);
    }

    @Override
    public Page<User> searchUser(String username,String phone, Pageable pageable) {
        if(username==""){
            username= null;
        }
        if(phone==""){
            phone= null;
        }
        return userRepository.searchUser(username,phone,pageable);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateStatus(Integer userId, Integer status) {
        userRepository.updateStatus(userId,status);
    }

    @Override
    public long getCountUser() {
        return userRepository.count();
    }

    @Override
    public void createUserOAuth2(String email, String fullname,String username, AuthenticationProvider provider) {
        User user = new User();
        user.setStatus(1);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setUsername(username);
        user.setCreateDate(new Date());
        user.setImage("default_avatar.png");
        user.setAuthProvider(provider);
        Role role = roleRepository.findByRole("USER");
        user.setRoles(Arrays.asList(role));

        userRepository.save(user);
    }

    @Override
    public void updateUserOauth(String email, String fullname,String username, AuthenticationProvider provider) {
        User user= userRepository.findByEmailAndProvider(email,provider.toString());
        user.setUsername(username);
        user.setFullname(fullname);
        user.setStatus(1);
        user.setUpdateDate(new Date());
        user.setImage("default_avatar.png");
        user.setAuthProvider(provider);
        userRepository.save(user);
    }

    @Override
    public User findByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email,provider);
    }
}
