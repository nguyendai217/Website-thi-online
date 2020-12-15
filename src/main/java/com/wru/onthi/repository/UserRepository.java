package com.wru.onthi.repository;

import com.wru.onthi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email and u.status=1")
    User getUserByEmail(@Param("email") String email);

    User findByEmail(String email);

    User findByUsername(String username);

    @Query(value = "SELECT * FROM user",nativeQuery = true)
    Page<User> findAllUser(Pageable pageable);

    @Query(value = " SELECT u from User u "
                    +"WHERE u.username like %:username% " +
                    "or u.phone like %:phone% " ,nativeQuery = false)
    Page<User> searchUser(@Param("username") String username,
                          @Param("phone") String phone,
                          Pageable pageable);

    @Modifying
    @Transactional
    @Query("update User u set u.status=0 where u.id=:userId")
    void disableUser(Integer userId);

    @Modifying
    @Transactional
    @Query("update User u set u.status =:status where u.id=:userId")
    void updateStatus(@Param("userId") Integer userId,@Param("status") Integer status);



}
