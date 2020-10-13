package com.wru.onthi.services;

import com.wru.onthi.entity.User;

import java.util.Optional;


public interface UserService {
    void createUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
    Optional<User> findById(Integer id);
}
