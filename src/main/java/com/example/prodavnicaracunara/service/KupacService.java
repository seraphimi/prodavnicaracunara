package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.Kupac;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.KupacRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KupacService {

    private static final Logger logger = LoggerFactory.getLogger(KupacService.class);

    @Autowired
    private KupacRepository kupacRepository;

    /**
     * Creates a new customer
     */
    public Kupac createKupac(Kupac kupac) {
        logger.info("Creating new customer: {} {}", kupac.getIme(), kupac.getPrezime());
        
        // Check if email already exists
        if (kupacRepository.existsByEmail(kupac.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + kupac.getEmail());
        }
        
        // Check if phone already exists (if provided)
        if (kupac.getTelefon() != null && kupacRepository.existsByTelefon(kupac.getTelefon())) {
            throw new IllegalArgumentException("Phone number already exists: " + kupac.getTelefon());
        }
        
        Kupac savedKupac = kupacRepository.save(kupac);
        
        logger.info("Customer created successfully with ID: {}", savedKupac.getId());
        return savedKupac;
    }

    /**
     * Gets all customers
     */
    @Transactional(readOnly = true)
    public List<Kupac> getAllKupci() {
        logger.debug("Fetching all customers");
        return kupacRepository.findAll();
    }

    /**
     * Gets a customer by ID
     */
    @Transactional(readOnly = true)
    public Kupac getKupacById(Long id) {
        logger.debug("Fetching customer with ID: {}", id);
        return kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));
    }

    /**
     * Gets a customer by email
     */
    @Transactional(readOnly = true)
    public Kupac getKupacByEmail(String email) {
        logger.debug("Fetching customer with email: {}", email);
        return kupacRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with email " + email + " not found"));
    }

    /**
     * Updates an existing customer
     */
    public Kupac updateKupac(Long id, Kupac kupac) {
        logger.info("Updating customer with ID: {}", id);
        
        Kupac existingKupac = kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));

        // Check if email is being changed and if new email already exists
        if (!existingKupac.getEmail().equals(kupac.getEmail()) && kupacRepository.existsByEmail(kupac.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + kupac.getEmail());
        }
        
        // Check if phone is being changed and if new phone already exists
        if (kupac.getTelefon() != null && !kupac.getTelefon().equals(existingKupac.getTelefon()) 
                && kupacRepository.existsByTelefon(kupac.getTelefon())) {
            throw new IllegalArgumentException("Phone number already exists: " + kupac.getTelefon());
        }

        existingKupac.setIme(kupac.getIme());
        existingKupac.setPrezime(kupac.getPrezime());
        existingKupac.setEmail(kupac.getEmail());
        existingKupac.setTelefon(kupac.getTelefon());
        existingKupac.setAdresa(kupac.getAdresa());

        Kupac updatedKupac = kupacRepository.save(existingKupac);
        logger.info("Customer updated successfully: {}", updatedKupac.getId());
        
        return updatedKupac;
    }

    /**
     * Deletes a customer
     */
    public void deleteKupac(Long id) {
        logger.info("Deleting customer with ID: {}", id);
        
        if (!kupacRepository.existsById(id)) {
            throw new ResourceNotFoundException("Kupac with ID " + id + " not found");
        }
        
        kupacRepository.deleteById(id);
        logger.info("Customer deleted successfully: {}", id);
    }

    /**
     * Searches customers by first name
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByIme(String ime) {
        logger.debug("Searching customers by first name: {}", ime);
        return kupacRepository.findByImeContainingIgnoreCase(ime);
    }

    /**
     * Searches customers by last name
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByPrezime(String prezime) {
        logger.debug("Searching customers by last name: {}", prezime);
        return kupacRepository.findByPrezimeContainingIgnoreCase(prezime);
    }

    /**
     * Searches customers by full name
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByPunoIme(String punoIme) {
        logger.debug("Searching customers by full name: {}", punoIme);
        return kupacRepository.findByPunoIme(punoIme);
    }

    /**
     * Searches customers by address
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByAdresa(String adresa) {
        logger.debug("Searching customers by address: {}", adresa);
        return kupacRepository.findByAdresaContainingIgnoreCase(adresa);
    }
}