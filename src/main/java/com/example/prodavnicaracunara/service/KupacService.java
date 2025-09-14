package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.dto.KupacDTO;
import com.example.prodavnicaracunara.entity.Kupac;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.KupacRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KupacService {

    private static final Logger logger = LoggerFactory.getLogger(KupacService.class);

    @Autowired
    private KupacRepository kupacRepository;

    /**
     * Creates a new customer
     */
    public KupacDTO createKupac(KupacDTO kupacDTO) {
        logger.info("Creating new customer: {} {}", kupacDTO.getIme(), kupacDTO.getPrezime());
        
        // Check if email already exists
        if (kupacRepository.existsByEmail(kupacDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + kupacDTO.getEmail());
        }
        
        // Check if phone already exists (if provided)
        if (kupacDTO.getTelefon() != null && kupacRepository.existsByTelefon(kupacDTO.getTelefon())) {
            throw new IllegalArgumentException("Phone number already exists: " + kupacDTO.getTelefon());
        }
        
        Kupac kupac = convertToEntity(kupacDTO);
        Kupac savedKupac = kupacRepository.save(kupac);
        
        logger.info("Customer created successfully with ID: {}", savedKupac.getId());
        return convertToDTO(savedKupac);
    }

    /**
     * Gets all customers
     */
    @Transactional(readOnly = true)
    public List<KupacDTO> getAllKupci() {
        logger.debug("Fetching all customers");
        return kupacRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets a customer by ID
     */
    @Transactional(readOnly = true)
    public KupacDTO getKupacById(Long id) {
        logger.debug("Fetching customer with ID: {}", id);
        Kupac kupac = kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));
        return convertToDTO(kupac);
    }

    /**
     * Gets a customer by email
     */
    @Transactional(readOnly = true)
    public KupacDTO getKupacByEmail(String email) {
        logger.debug("Fetching customer with email: {}", email);
        Kupac kupac = kupacRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with email " + email + " not found"));
        return convertToDTO(kupac);
    }

    /**
     * Updates an existing customer
     */
    public KupacDTO updateKupac(Long id, KupacDTO kupacDTO) {
        logger.info("Updating customer with ID: {}", id);
        
        Kupac existingKupac = kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));

        // Check if email is being changed and if new email already exists
        if (!existingKupac.getEmail().equals(kupacDTO.getEmail()) && 
            kupacRepository.existsByEmail(kupacDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + kupacDTO.getEmail());
        }
        
        // Check if phone is being changed and if new phone already exists
        if (kupacDTO.getTelefon() != null && 
            !kupacDTO.getTelefon().equals(existingKupac.getTelefon()) && 
            kupacRepository.existsByTelefon(kupacDTO.getTelefon())) {
            throw new IllegalArgumentException("Phone number already exists: " + kupacDTO.getTelefon());
        }

        existingKupac.setIme(kupacDTO.getIme());
        existingKupac.setPrezime(kupacDTO.getPrezime());
        existingKupac.setEmail(kupacDTO.getEmail());
        existingKupac.setTelefon(kupacDTO.getTelefon());
        existingKupac.setAdresa(kupacDTO.getAdresa());

        Kupac updatedKupac = kupacRepository.save(existingKupac);
        logger.info("Customer updated successfully: {}", updatedKupac.getId());
        
        return convertToDTO(updatedKupac);
    }

    /**
     * Deletes a customer
     */
    public void deleteKupac(Long id) {
        logger.info("Deleting customer with ID: {}", id);
        
        Kupac kupac = kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));
        
        // Check if customer has orders
        if (kupac.getNarudzbe() != null && !kupac.getNarudzbe().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete customer with existing orders");
        }
        
        kupacRepository.deleteById(id);
        logger.info("Customer deleted successfully: {}", id);
    }

    /**
     * Searches customers by first name
     */
    @Transactional(readOnly = true)
    public List<KupacDTO> searchByIme(String ime) {
        logger.debug("Searching customers by first name: {}", ime);
        return kupacRepository.findByImeContainingIgnoreCase(ime)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches customers by last name
     */
    @Transactional(readOnly = true)
    public List<KupacDTO> searchByPrezime(String prezime) {
        logger.debug("Searching customers by last name: {}", prezime);
        return kupacRepository.findByPrezimeContainingIgnoreCase(prezime)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches customers by full name
     */
    @Transactional(readOnly = true)
    public List<KupacDTO> searchByPunoIme(String punoIme) {
        logger.debug("Searching customers by full name: {}", punoIme);
        return kupacRepository.findByPunoIme(punoIme)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches customers by address
     */
    @Transactional(readOnly = true)
    public List<KupacDTO> searchByAdresa(String adresa) {
        logger.debug("Searching customers by address: {}", adresa);
        return kupacRepository.findByAdresaContainingIgnoreCase(adresa)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper methods for conversion
    private KupacDTO convertToDTO(Kupac kupac) {
        KupacDTO dto = new KupacDTO();
        dto.setId(kupac.getId());
        dto.setIme(kupac.getIme());
        dto.setPrezime(kupac.getPrezime());
        dto.setEmail(kupac.getEmail());
        dto.setTelefon(kupac.getTelefon());
        dto.setAdresa(kupac.getAdresa());
        return dto;
    }

    private Kupac convertToEntity(KupacDTO dto) {
        Kupac kupac = new Kupac();
        kupac.setIme(dto.getIme());
        kupac.setPrezime(dto.getPrezime());
        kupac.setEmail(dto.getEmail());
        kupac.setTelefon(dto.getTelefon());
        kupac.setAdresa(dto.getAdresa());
        return kupac;
    }
}