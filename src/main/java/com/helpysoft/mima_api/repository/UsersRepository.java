package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.trackingId = :trackingId")
    Optional<Users> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
