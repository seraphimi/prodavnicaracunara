package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.Proizvod;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.ProizvodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProizvodService {

    private static final Logger logger = LoggerFactory.getLogger(ProizvodService.class);

    @Autowired
    private ProizvodRepository proizvodRepository;

    /**
     * Creates a new product
     */
    public Proizvod createProizvod(Proizvod proizvod) {
        logger.info("Creating new product: {}", proizvod.getNaziv());
        
        Proizvod savedProizvod = proizvodRepository.save(proizvod);
        
        logger.info("Product created successfully with ID: {}", savedProizvod.getId());
        return savedProizvod;
    }

    /**
     * Gets all products
     */
    @Transactional(readOnly = true)
    public List<Proizvod> getAllProizvodi() {
        logger.debug("Fetching all products");
        return proizvodRepository.findAll();
    }

    /**
     * Gets a product by ID
     */
    @Transactional(readOnly = true)
    public Proizvod getProizvodById(Long id) {
        logger.debug("Fetching product with ID: {}", id);
        return proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
    }

    /**
     * Updates an existing product
     */
    public Proizvod updateProizvod(Long id, Proizvod proizvod) {
        logger.info("Updating product with ID: {}", id);
        
        Proizvod existingProizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));

        existingProizvod.setNaziv(proizvod.getNaziv());
        existingProizvod.setCpu(proizvod.getCpu());
        existingProizvod.setRam(proizvod.getRam());
        existingProizvod.setGpu(proizvod.getGpu());
        existingProizvod.setCena(proizvod.getCena());
        existingProizvod.setKolicinaUStanju(proizvod.getKolicinaUStanju());

        Proizvod updatedProizvod = proizvodRepository.save(existingProizvod);
        logger.info("Product updated successfully: {}", updatedProizvod.getId());
        
        return updatedProizvod;
    }

    /**
     * Deletes a product
     */
    public void deleteProizvod(Long id) {
        logger.info("Deleting product with ID: {}", id);
        
        if (!proizvodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Proizvod with ID " + id + " not found");
        }
        
        proizvodRepository.deleteById(id);
        logger.info("Product deleted successfully: {}", id);
    }

    /**
     * Searches products by name
     */
    @Transactional(readOnly = true)
    public List<Proizvod> searchByNaziv(String naziv) {
        logger.debug("Searching products by name: {}", naziv);
        return proizvodRepository.findByNazivContainingIgnoreCase(naziv);
    }

    /**
     * Finds products in stock
     */
    @Transactional(readOnly = true)
    public List<Proizvod> getProizvodiInStock() {
        logger.debug("Fetching products in stock");
        return proizvodRepository.findInStock();
    }

    /**
     * Finds products by price range
     */
    @Transactional(readOnly = true)
    public List<Proizvod> getProizvodiByPriceRange(BigDecimal minCena, BigDecimal maxCena) {
        logger.debug("Fetching products in price range: {} - {}", minCena, maxCena);
        return proizvodRepository.findByCenaBetween(minCena, maxCena);
    }

    /**
     * Updates product stock
     */
    public Proizvod updateStock(Long id, Integer newStock) {
        logger.info("Updating stock for product ID: {} to {}", id, newStock);
        
        Proizvod proizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
        
        proizvod.setKolicinaUStanju(newStock);
        Proizvod updatedProizvod = proizvodRepository.save(proizvod);
        
        logger.info("Stock updated successfully for product: {}", id);
        return updatedProizvod;
    }

    /**
     * Reduces product stock (for orders)
     */
    public void reduceStock(Long id, Integer quantity) {
        logger.info("Reducing stock for product ID: {} by {}", id, quantity);
        
        Proizvod proizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
        
        if (proizvod.getKolicinaUStanju() < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + proizvod.getKolicinaUStanju() + ", Required: " + quantity);
        }
        
        proizvod.setKolicinaUStanju(proizvod.getKolicinaUStanju() - quantity);
        proizvodRepository.save(proizvod);
        
        logger.info("Stock reduced successfully for product: {}", id);
    }
}