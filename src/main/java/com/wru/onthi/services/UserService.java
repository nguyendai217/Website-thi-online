package com.wru.onthi.services;

import com.wru.onthi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface UserService {
    void createUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
    Optional<User> findById(Integer id);
    User findUserByName(String name);
    Page<User> getAllUser(Pageable pageable);
    Page<User> searchUser(String username, String email, String phone,Pageable pageable);
    User findByUsernameOrEmail(String username , String email);
}
