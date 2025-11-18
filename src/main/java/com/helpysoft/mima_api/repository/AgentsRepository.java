package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.MarinerStatus;
import com.helpysoft.mima_api.entity.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentsRepository extends JpaRepository<Agents, Long> {

    @Query("SELECT a FROM Agents a WHERE a.trackingId = :trackingId")
    Optional<Agents> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT a FROM Agents a WHERE a.registrationNo = :registrationNo")
    Optional<Agents> findByRegistrationNo(@Param("registrationNo") String registrationNo);

    @Query("SELECT a FROM Agents a WHERE a.status = :status")
    List<Agents> findByStatus(@Param("status") MarinerStatus status);

    @Query("SELECT a FROM Agents a WHERE a.unit = :unit")
    List<Agents> findByUnit(@Param("unit") Units unit);

    @Query("SELECT a FROM Agents a LEFT JOIN FETCH a.unit")
    List<Agents> findAllWithUnit();

    @Query("SELECT a FROM Agents a WHERE a.unit = :unit AND a.status = :status")
    List<Agents> findByUnitAndStatus(@Param("unit") Units unit, @Param("status") MarinerStatus status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Agents a WHERE a.registrationNo = :registrationNo")
    boolean existsByRegistrationNo(@Param("registrationNo") String registrationNo);

    @Query("SELECT COUNT(a) FROM Agents a WHERE a.status = :status")
    Long countByStatus(@Param("status") MarinerStatus status);

    @Query("SELECT a FROM Agents a LEFT JOIN FETCH a.unit WHERE " +
            "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.registrationNo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.seafarerBookNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Agents> searchAgents(@Param("searchTerm") String searchTerm);
}
