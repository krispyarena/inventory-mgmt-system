package com.ivms.ims.repository;

import com.ivms.ims.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE role = :role AND created_at >= NOW() - INTERVAL 30 DAY",
    nativeQuery = true)
    List<User> findByRole(@Param("role") String role);

}
