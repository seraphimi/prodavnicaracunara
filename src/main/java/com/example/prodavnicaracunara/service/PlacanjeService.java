package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.Placanje;
import com.example.prodavnicaracunara.entity.NacinPlacanja;
import com.example.prodavnicaracunara.entity.StatusPlacanja;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.PlacanjeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PlacanjeService {

    @Autowired
    private PlacanjeRepository placanjeRepository;

    /**
     * Creates a new payment
     */
    public Placanje createPlacanje(Placanje placanje) {
        placanje.setDatum(LocalDateTime.now());
        if (placanje.getStatus() == null) {
            placanje.setStatus(StatusPlacanja.NEPLACENO);
        }
        return placanjeRepository.save(placanje);
    }

    /**
     * Gets all payments
     */
    @Transactional(readOnly = true)
    public List<Placanje> getAllPlacanja() {
        return placanjeRepository.findAll();
    }

    /**
     * Gets payment by ID
     */
    @Transactional(readOnly = true)
    public Placanje getPlacanjeById(Long id) {
        return placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
    }

    /**
     * Gets payment by order ID
     */
    @Transactional(readOnly = true)
    public Placanje getPlacanjeByNarudzbaId(Long narudzbaId) {
        return placanjeRepository.findByNarudzbaId(narudzbaId)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje for order ID " + narudzbaId + " not found"));
    }

    /**
     * Gets payments by payment method
     */
    @Transactional(readOnly = true)
    public List<Placanje> getPlacanjaByNacinPlacanja(NacinPlacanja nacinPlacanja) {
        return placanjeRepository.findByNacinPlacanja(nacinPlacanja);
    }

    /**
     * Gets payments by status
     */
    @Transactional(readOnly = true)
    public List<Placanje> getPlacanjaByStatus(StatusPlacanja status) {
        return placanjeRepository.findByStatus(status);
    }

    /**
     * Gets pending payments
     */
    @Transactional(readOnly = true)
    public List<Placanje> getPendingPlacanja() {
        return placanjeRepository.findUnpaidPayments();
    }

    /**
     * Gets completed payments
     */
    @Transactional(readOnly = true)
    public List<Placanje> getCompletedPlacanja() {
        return placanjeRepository.findByStatus(StatusPlacanja.PLACENO);
    }

    /**
     * Gets total revenue
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        // Calculate based on completed payments
        return placanjeRepository.findByStatus(StatusPlacanja.PLACENO)
                .stream()
                .map(p -> p.getNarudzba().getUkupnaCena())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets revenue by period
     */
    @Transactional(readOnly = true)
    public BigDecimal getRevenueByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return placanjeRepository.getTotalRevenue(startDate, endDate)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Updates payment status
     */
    public Placanje updatePlacanjeStatus(Long id, StatusPlacanja noviStatus) {
        Placanje placanje = getPlacanjeById(id);
        placanje.setStatus(noviStatus);
        return placanjeRepository.save(placanje);
    }

    /**
     * Updates payment
     */
    public Placanje updatePlacanje(Long id, Placanje placanje) {
        Placanje existingPlacanje = getPlacanjeById(id);
        
        existingPlacanje.setNacinPlacanja(placanje.getNacinPlacanja());
        existingPlacanje.setStatus(placanje.getStatus());
        // Keep original date and order reference
        
        return placanjeRepository.save(existingPlacanje);
    }

    /**
     * Deletes payment
     */
    public void deletePlacanje(Long id) {
        if (!placanjeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Placanje with ID " + id + " not found");
        }
        placanjeRepository.deleteById(id);
    }
}