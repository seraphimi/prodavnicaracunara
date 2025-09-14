package com.example.prodavnicaracunara.repository;

import com.example.prodavnicaracunara.entity.Proizvod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProizvodRepository extends JpaRepository<Proizvod, Long> {
    
    /**
     * Finds products by name containing the given text (case-insensitive)
     */
    List<Proizvod> findByNazivContainingIgnoreCase(String naziv);
    
    /**
     * Finds products within a price range
     */
    List<Proizvod> findByCenaBetween(BigDecimal minCena, BigDecimal maxCena);
    
    /**
     * Finds products with stock greater than zero
     */
    List<Proizvod> findByKolicinaUStanjuGreaterThan(Integer kolicina);
    
    /**
     * Finds products that are in stock
     */
    @Query("SELECT p FROM Proizvod p WHERE p.kolicinaUStanju > 0")
    List<Proizvod> findInStock();
    
    /**
     * Finds products with specific CPU specification
     */
    List<Proizvod> findByCpuContainingIgnoreCase(String cpu);
    
    /**
     * Finds products with specific RAM specification
     */
    List<Proizvod> findByRamContainingIgnoreCase(String ram);
    
    /**
     * Finds products with specific GPU specification
     */
    List<Proizvod> findByGpuContainingIgnoreCase(String gpu);
    
    /**
     * Custom query to find products by specifications
     */
    @Query("SELECT p FROM Proizvod p WHERE " +
           "(:naziv IS NULL OR LOWER(p.naziv) LIKE LOWER(CONCAT('%', :naziv, '%'))) AND " +
           "(:cpu IS NULL OR LOWER(p.cpu) LIKE LOWER(CONCAT('%', :cpu, '%'))) AND " +
           "(:ram IS NULL OR LOWER(p.ram) LIKE LOWER(CONCAT('%', :ram, '%'))) AND " +
           "(:gpu IS NULL OR LOWER(p.gpu) LIKE LOWER(CONCAT('%', :gpu, '%'))) AND " +
           "(:minCena IS NULL OR p.cena >= :minCena) AND " +
           "(:maxCena IS NULL OR p.cena <= :maxCena)")
    List<Proizvod> findBySpecifications(@Param("naziv") String naziv,
                                       @Param("cpu") String cpu,
                                       @Param("ram") String ram,
                                       @Param("gpu") String gpu,
                                       @Param("minCena") BigDecimal minCena,
                                       @Param("maxCena") BigDecimal maxCena);
}