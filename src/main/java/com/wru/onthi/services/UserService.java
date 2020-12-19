package com.wru.onthi.services;

import com.wru.onthi.entity.AuthenticationProvider;
import com.wru.onthi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface UserService {
    void createUser(User user);
    User updateUser(User user);
    void disableUser(Integer userId);
    void deleteUser(User user);
    Optional<User> findById(Integer id);
    User findUserByName(String name);
    Page<User> getAllUser(Pageable pageable);
    Page<User> searchUser(String username,String phone,Pageable pageable);
    User findByEmail(String email);
    User findByUsername(String username);
    void updateStatus(Integer userId, Integer status);
    long getCountUser();

    void createUserOAuth2(String email, String name, AuthenticationProvider provider);
}
