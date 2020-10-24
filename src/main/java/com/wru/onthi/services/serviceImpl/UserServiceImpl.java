package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.User;
import com.wru.onthi.repository.UserRepository;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
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
    public Page<User> searchUser(String username, String email, String phone,Pageable pageable) {
        return userRepository.searchUser(username,email,phone,pageable);
    }

    @Override
    public User findByUsernameOrEmail(String username,String email) {
        return userRepository.findByUsernameOrEmail(username,email);
    }


}
