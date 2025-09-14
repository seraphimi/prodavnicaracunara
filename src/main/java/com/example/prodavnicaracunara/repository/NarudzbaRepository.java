package com.example.prodavnicaracunara.repository;

import com.example.prodavnicaracunara.entity.Kupac;
import com.example.prodavnicaracunara.entity.Narudzba;
import com.example.prodavnicaracunara.entity.StatusNarudzbe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NarudzbaRepository extends JpaRepository<Narudzba, Long> {
    
    /**
     * Finds order by order number
     */
    Optional<Narudzba> findByBrojNarudzbe(String brojNarudzbe);
    
    /**
     * Finds orders by customer
     */
    List<Narudzba> findByKupac(Kupac kupac);
    
    /**
     * Finds orders by customer ID
     */
    List<Narudzba> findByKupacId(Long kupacId);
    
    /**
     * Finds orders by status
     */
    List<Narudzba> findByStatus(StatusNarudzbe status);
    
    /**
     * Finds orders created within a date range
     */
    List<Narudzba> findByDatumKreiranjaBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Finds orders by customer and status
     */
    List<Narudzba> findByKupacAndStatus(Kupac kupac, StatusNarudzbe status);
    
    /**
     * Gets all orders for monitoring (orders that are not delivered or cancelled)
     */
    @Query("SELECT n FROM Narudzba n WHERE n.status IN ('U_OBRADI', 'POSLATA')")
    List<Narudzba> findActiveOrders();
    
    /**
     * Gets orders that need status update (older than specified minutes)
     */
    @Query("SELECT n FROM Narudzba n WHERE n.status = 'U_OBRADI' AND n.datumKreiranja < :cutoffTime")
    List<Narudzba> findOrdersForStatusUpdate(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * Gets orders by customer email
     */
    @Query("SELECT n FROM Narudzba n WHERE n.kupac.email = :email")
    List<Narudzba> findByKupacEmail(@Param("email") String email);
    
    /**
     * Gets order statistics by status
     */
    @Query("SELECT n.status, COUNT(n) FROM Narudzba n GROUP BY n.status")
    List<Object[]> getOrderStatistics();
    
    /**
     * Finds recent orders (last N days)
     */
    @Query("SELECT n FROM Narudzba n WHERE n.datumKreiranja >= :startDate ORDER BY n.datumKreiranja DESC")
    List<Narudzba> findRecentOrders(@Param("startDate") LocalDateTime startDate);
    
    /**
     * Checks if order number already exists
     */
    boolean existsByBrojNarudzbe(String brojNarudzbe);
    
    /**
     * Gets orders containing specific product
     */
    @Query("SELECT DISTINCT n FROM Narudzba n JOIN n.proizvodi p WHERE p.id = :proizvodId")
    List<Narudzba> findOrdersContainingProduct(@Param("proizvodId") Long proizvodId);
}