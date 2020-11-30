package com.wru.onthi.repository;

import com.wru.onthi.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email and u.status=1")
    User getUserByEmail(@Param("email") String email);

    User findByEmail(String email);

    User findByUsername(String username);

    @Query(value = "SELECT * FROM user",nativeQuery = true)
    Page<User> findAllUser(Pageable pageable);

    @Query(value = " SELECT * from User us "
                    +"WHERE us.username like %:username% " +
                    "or us.email like %:email% " +
                    "or us.phone like %:phone% " +
                    "or us.status =:status", nativeQuery = true)
    Page<User> searchUser(@Param("username") String username,
                          @Param("email") String email,
                          @Param("phone") String phone,
                          @Param("status") String status, Pageable pageable);

    @Query("update User u set u.status=0 where u.id=:userId")
    void deleteUser(Integer userId);

    @Modifying
    @Query("update User u set u.status =:status where u.id=:userId")
    void updateStatus(@Param("userId") Integer userId,@Param("status") Integer status);

}
