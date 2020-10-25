package com.wru.onthi.repository;

import com.wru.onthi.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    User findByEmail(String email);

    User findByUsername(String username);

    @Query(value = "SELECT * FROM user ",nativeQuery = true)
    Page<User> findAllUser(Pageable pageable);

    @Query(value = "SELECT u from User u "
                    +"WHERE (u.username like :username) "
                    + "OR (u.email like :email) "
                    + "OR (u.phone like :phone) ", nativeQuery = false)

    Page<User> searchUser(@Param("username") String username,
                          @Param("email") String email,
                          @Param("phone") String phone, Pageable pageable);

}
