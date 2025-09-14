package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.dto.ProizvodDTO;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class ProizvodService {

    private static final Logger logger = LoggerFactory.getLogger(ProizvodService.class);

    @Autowired
    private ProizvodRepository proizvodRepository;

    /**
     * Creates a new product
     */
    public ProizvodDTO createProizvod(ProizvodDTO proizvodDTO) {
        logger.info("Creating new product: {}", proizvodDTO.getNaziv());
        
        Proizvod proizvod = convertToEntity(proizvodDTO);
        Proizvod savedProizvod = proizvodRepository.save(proizvod);
        
        logger.info("Product created successfully with ID: {}", savedProizvod.getId());
        return convertToDTO(savedProizvod);
    }

    /**
     * Gets all products
     */
    @Transactional(readOnly = true)
    public List<ProizvodDTO> getAllProizvodi() {
        logger.debug("Fetching all products");
        return proizvodRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets a product by ID
     */
    @Transactional(readOnly = true)
    public ProizvodDTO getProizvodById(Long id) {
        logger.debug("Fetching product with ID: {}", id);
        Proizvod proizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
        return convertToDTO(proizvod);
    }

    /**
     * Updates an existing product
     */
    public ProizvodDTO updateProizvod(Long id, ProizvodDTO proizvodDTO) {
        logger.info("Updating product with ID: {}", id);
        
        Proizvod existingProizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));

        existingProizvod.setNaziv(proizvodDTO.getNaziv());
        existingProizvod.setCpu(proizvodDTO.getCpu());
        existingProizvod.setRam(proizvodDTO.getRam());
        existingProizvod.setGpu(proizvodDTO.getGpu());
        existingProizvod.setCena(proizvodDTO.getCena());
        existingProizvod.setKolicinaUStanju(proizvodDTO.getKolicinaUStanju());

        Proizvod updatedProizvod = proizvodRepository.save(existingProizvod);
        logger.info("Product updated successfully: {}", updatedProizvod.getId());
        
        return convertToDTO(updatedProizvod);
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
    public List<ProizvodDTO> searchByNaziv(String naziv) {
        logger.debug("Searching products by name: {}", naziv);
        return proizvodRepository.findByNazivContainingIgnoreCase(naziv)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds products in stock
     */
    @Transactional(readOnly = true)
    public List<ProizvodDTO> getProizvodiInStock() {
        logger.debug("Fetching products in stock");
        return proizvodRepository.findInStock()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds products by price range
     */
    @Transactional(readOnly = true)
    public List<ProizvodDTO> getProizvodiByPriceRange(BigDecimal minCena, BigDecimal maxCena) {
        logger.debug("Fetching products in price range: {} - {}", minCena, maxCena);
        return proizvodRepository.findByCenaBetween(minCena, maxCena)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates product stock
     */
    public ProizvodDTO updateStock(Long id, Integer newStock) {
        logger.info("Updating stock for product ID: {} to {}", id, newStock);
        
        Proizvod proizvod = proizvodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proizvod with ID " + id + " not found"));
        
        proizvod.setKolicinaUStanju(newStock);
        Proizvod updatedProizvod = proizvodRepository.save(proizvod);
        
        logger.info("Stock updated successfully for product: {}", id);
        return convertToDTO(updatedProizvod);
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

    // Helper methods for conversion
    private ProizvodDTO convertToDTO(Proizvod proizvod) {
        ProizvodDTO dto = new ProizvodDTO();
        dto.setId(proizvod.getId());
        dto.setNaziv(proizvod.getNaziv());
        dto.setCpu(proizvod.getCpu());
        dto.setRam(proizvod.getRam());
        dto.setGpu(proizvod.getGpu());
        dto.setCena(proizvod.getCena());
        dto.setKolicinaUStanju(proizvod.getKolicinaUStanju());
        return dto;
    }

    private Proizvod convertToEntity(ProizvodDTO dto) {
        Proizvod proizvod = new Proizvod();
        proizvod.setNaziv(dto.getNaziv());
        proizvod.setCpu(dto.getCpu());
        proizvod.setRam(dto.getRam());
        proizvod.setGpu(dto.getGpu());
        proizvod.setCena(dto.getCena());
        proizvod.setKolicinaUStanju(dto.getKolicinaUStanju());
        return proizvod;
    }
}