package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.Kupac;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.KupacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KupacService {

    @Autowired
    private KupacRepository kupacRepository;

    /**
     * Creates a new customer
     */
    public Kupac createKupac(Kupac kupac) {
        // Check if email already exists
        if (kupacRepository.existsByEmail(kupac.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + kupac.getEmail());
        }
        
        // Check if phone already exists (if provided)
        if (kupac.getTelefon() != null && kupacRepository.existsByTelefon(kupac.getTelefon())) {
            throw new IllegalArgumentException("Phone number already exists: " + kupac.getTelefon());
        }
        
        return kupacRepository.save(kupac);
    }

    /**
     * Gets all customers
     */
    @Transactional(readOnly = true)
    public List<Kupac> getAllKupci() {
        return kupacRepository.findAll();
    }

    /**
     * Gets a customer by ID
     */
    @Transactional(readOnly = true)
    public Kupac getKupacById(Long id) {
        return kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));
    }

    /**
     * Gets a customer by email
     */
    @Transactional(readOnly = true)
    public Kupac getKupacByEmail(String email) {
        return kupacRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with email " + email + " not found"));
    }

    /**
     * Updates an existing customer
     */
    public Kupac updateKupac(Long id, Kupac kupac) {
        Kupac existingKupac = kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));

        // Check if email is being changed and if new email already exists
        if (!existingKupac.getEmail().equals(kupac.getEmail()) && 
            kupacRepository.existsByEmail(kupac.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + kupac.getEmail());
        }
        
        // Check if phone is being changed and if new phone already exists
        if (kupac.getTelefon() != null && 
            !kupac.getTelefon().equals(existingKupac.getTelefon()) && 
            kupacRepository.existsByTelefon(kupac.getTelefon())) {
            throw new IllegalArgumentException("Phone number already exists: " + kupac.getTelefon());
        }

        existingKupac.setIme(kupac.getIme());
        existingKupac.setPrezime(kupac.getPrezime());
        existingKupac.setEmail(kupac.getEmail());
        existingKupac.setTelefon(kupac.getTelefon());
        existingKupac.setAdresa(kupac.getAdresa());

        return kupacRepository.save(existingKupac);
    }

    /**
     * Deletes a customer
     */
    public void deleteKupac(Long id) {
        Kupac kupac = kupacRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + id + " not found"));
        
        // Check if customer has orders
        if (kupac.getNarudzbe() != null && !kupac.getNarudzbe().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete customer with existing orders");
        }
        
        kupacRepository.deleteById(id);
    }

    /**
     * Searches customers by first name
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByIme(String ime) {
        return kupacRepository.findByImeContainingIgnoreCase(ime);
    }

    /**
     * Searches customers by last name
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByPrezime(String prezime) {
        return kupacRepository.findByPrezimeContainingIgnoreCase(prezime);
    }

    /**
     * Searches customers by full name
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByPunoIme(String punoIme) {
        return kupacRepository.findByPunoIme(punoIme);
    }

    /**
     * Searches customers by address
     */
    @Transactional(readOnly = true)
    public List<Kupac> searchByAdresa(String adresa) {
        return kupacRepository.findByAdresaContainingIgnoreCase(adresa);
    }
}