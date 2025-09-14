package com.example.prodavnicaracunara.repository;

import com.example.prodavnicaracunara.entity.Kupac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KupacRepository extends JpaRepository<Kupac, Long> {
    
    /**
     * Finds customer by email address
     */
    Optional<Kupac> findByEmail(String email);
    
    /**
     * Finds customers by first name (case-insensitive)
     */
    List<Kupac> findByImeContainingIgnoreCase(String ime);
    
    /**
     * Finds customers by last name (case-insensitive)
     */
    List<Kupac> findByPrezimeContainingIgnoreCase(String prezime);
    
    /**
     * Finds customers by full name (case-insensitive)
     */
    @Query("SELECT k FROM Kupac k WHERE " +
           "LOWER(CONCAT(k.ime, ' ', k.prezime)) LIKE LOWER(CONCAT('%', :punoIme, '%'))")
    List<Kupac> findByPunoIme(@Param("punoIme") String punoIme);
    
    /**
     * Finds customers by phone number
     */
    Optional<Kupac> findByTelefon(String telefon);
    
    /**
     * Finds customers by address containing the given text (case-insensitive)
     */
    List<Kupac> findByAdresaContainingIgnoreCase(String adresa);
    
    /**
     * Checks if email already exists (for validation)
     */
    boolean existsByEmail(String email);
    
    /**
     * Checks if phone number already exists (for validation)
     */
    boolean existsByTelefon(String telefon);
    
    /**
     * Custom search by multiple criteria
     */
    @Query("SELECT k FROM Kupac k WHERE " +
           "(:ime IS NULL OR LOWER(k.ime) LIKE LOWER(CONCAT('%', :ime, '%'))) AND " +
           "(:prezime IS NULL OR LOWER(k.prezime) LIKE LOWER(CONCAT('%', :prezime, '%'))) AND " +
           "(:email IS NULL OR LOWER(k.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:adresa IS NULL OR LOWER(k.adresa) LIKE LOWER(CONCAT('%', :adresa, '%')))")
    List<Kupac> findByCriteria(@Param("ime") String ime,
                              @Param("prezime") String prezime,
                              @Param("email") String email,
                              @Param("adresa") String adresa);
}