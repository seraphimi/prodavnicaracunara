package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.Proizvod;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.ProizvodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProizvodService {

    @Autowired
    private ProizvodRepository proizvodRepository;

    /**
     * Creates a new product
     */
    public Proizvod createProizvod(Proizvod proizvod) {
        return proizvodRepository.save(proizvod);
    }

    /**
     * Gets all products
     */
    @Transactional(readOnly = true)
    public List<Proizvod> getAllProizvodi() {
        return proizvodRepository.findAll();
    }

    /**
     * Gets a product by ID
     */
    @Transactional(readOnly = true)
    public Proizvod getProizvodById(Long id) {
        return proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
    }

    /**
     * Updates an existing product
     */
    public Proizvod updateProizvod(Long id, Proizvod proizvod) {
        Proizvod existingProizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));

        existingProizvod.setNaziv(proizvod.getNaziv());
        existingProizvod.setCpu(proizvod.getCpu());
        existingProizvod.setRam(proizvod.getRam());
        existingProizvod.setGpu(proizvod.getGpu());
        existingProizvod.setCena(proizvod.getCena());
        existingProizvod.setKolicinaUStanju(proizvod.getKolicinaUStanju());

        return proizvodRepository.save(existingProizvod);
    }

    /**
     * Deletes a product
     */
    public void deleteProizvod(Long id) {
        if (!proizvodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proizvod with ID " + id + " not found");
        }
        
        proizvodRepository.deleteById(id);
    }

    /**
     * Searches products by name
     */
    @Transactional(readOnly = true)
    public List<Proizvod> searchByNaziv(String naziv) {
        return proizvodRepository.findByNazivContainingIgnoreCase(naziv);
    }

    /**
     * Finds products in stock
     */
    @Transactional(readOnly = true)
    public List<Proizvod> getProizvodiInStock() {
        return proizvodRepository.findInStock();
    }

    /**
     * Finds products by price range
     */
    @Transactional(readOnly = true)
    public List<Proizvod> getProizvodiByPriceRange(BigDecimal minCena, BigDecimal maxCena) {
        return proizvodRepository.findByCenaBetween(minCena, maxCena);
    }

    /**
     * Updates product stock
     */
    public Proizvod updateStock(Long id, Integer newStock) {
        Proizvod proizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
        
        proizvod.setKolicinaUStanju(newStock);
        return proizvodRepository.save(proizvod);
    }

    /**
     * Reduces product stock (for orders)
     */
    public void reduceStock(Long id, Integer quantity) {
        Proizvod proizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
        
        if (proizvod.getKolicinaUStanju() < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + proizvod.getKolicinaUStanju() + ", Required: " + quantity);
        }
        
        proizvod.setKolicinaUStanju(proizvod.getKolicinaUStanju() - quantity);
        proizvodRepository.save(proizvod);
    }
}