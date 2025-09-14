package com.example.prodavnicaracunara.repository;

import com.example.prodavnicaracunara.entity.NacinPlacanja;
import com.example.prodavnicaracunara.entity.Narudzba;
import com.example.prodavnicaracunara.entity.Placanje;
import com.example.prodavnicaracunara.entity.StatusPlacanja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlacanjeRepository extends JpaRepository<Placanje, Long> {
    
    /**
     * Finds payment by order
     */
    Optional<Placanje> findByNarudzba(Narudzba narudzba);
    
    /**
     * Finds payment by order ID
     */
    Optional<Placanje> findByNarudzbaId(Long narudzbaId);
    
    /**
     * Finds payments by payment method
     */
    List<Placanje> findByNacinPlacanja(NacinPlacanja nacinPlacanja);
    
    /**
     * Finds payments by status
     */
    List<Placanje> findByStatus(StatusPlacanja status);
    
    /**
     * Finds payments within a date range
     */
    List<Placanje> findByDatumBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Finds payments by payment method and status
     */
    List<Placanje> findByNacinPlacanjaAndStatus(NacinPlacanja nacinPlacanja, StatusPlacanja status);
    
    /**
     * Gets all unpaid payments
     */
    @Query("SELECT p FROM Placanje p WHERE p.status = 'NEPLACENO'")
    List<Placanje> findUnpaidPayments();
    
    /**
     * Gets payment statistics by method
     */
    @Query("SELECT p.nacinPlacanja, COUNT(p), SUM(p.narudzba.ukupnaCena) FROM Placanje p WHERE p.status = 'PLACENO' GROUP BY p.nacinPlacanja")
    List<Object[]> getPaymentStatisticsByMethod();
    
    /**
     * Gets payment statistics by status
     */
    @Query("SELECT p.status, COUNT(p) FROM Placanje p GROUP BY p.status")
    List<Object[]> getPaymentStatisticsByStatus();
    
    /**
     * Finds payments for a specific customer
     */
    @Query("SELECT p FROM Placanje p WHERE p.narudzba.kupac.id = :kupacId")
    List<Placanje> findByKupacId(@Param("kupacId") Long kupacId);
    
    /**
     * Gets successful payments in date range
     */
    @Query("SELECT p FROM Placanje p WHERE p.status = 'PLACENO' AND p.datum BETWEEN :startDate AND :endDate")
    List<Placanje> findSuccessfulPayments(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    /**
     * Gets total revenue for a date range
     */
    @Query("SELECT SUM(p.narudzba.ukupnaCena) FROM Placanje p WHERE p.status = 'PLACENO' AND p.datum BETWEEN :startDate AND :endDate")
    Optional<Double> getTotalRevenue(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
}