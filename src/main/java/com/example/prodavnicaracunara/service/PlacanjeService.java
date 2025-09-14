package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.*;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.NarudzbaRepository;
import com.example.prodavnicaracunara.repository.PlacanjeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlacanjeService {

    private static final Logger logger = LoggerFactory.getLogger(PlacanjeService.class);

    @Autowired
    private PlacanjeRepository placanjeRepository;
    
    @Autowired
    private NarudzbaRepository narudzbaRepository;

    /**
     * Creates a new payment
     */
    public Placanje createPlacanje(Placanje placanje) {
        logger.info("Creating new payment for order ID: {}", placanje.getNarudzba() != null ? placanje.getNarudzba().getId() : "null");
        
        if (placanje.getNarudzba() == null || placanje.getNarudzba().getId() == null) {
            throw new IllegalArgumentException("Order is required for payment");
        }
        
        // Validate order exists
        Narudzba narudzba = narudzbaRepository.findById(placanje.getNarudzba().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with ID " + placanje.getNarudzba().getId() + " not found"));
        
        // Check if payment already exists for this order
        Optional<Placanje> existingPayment = placanjeRepository.findByNarudzba(narudzba);
        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException("Payment already exists for order: " + narudzba.getBrojNarudzbe());
        }
        
        // Create payment
        placanje.setNarudzba(narudzba);
        placanje.setStatus(StatusPlacanja.NEPLACENO);
        placanje.setDatum(LocalDateTime.now());
        
        Placanje savedPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment created successfully with ID: {}", savedPlacanje.getId());
        return savedPlacanje;
    }

    /**
     * Gets all payments
     */
    @Transactional(readOnly = true)
    public List<Placanje> getAllPlacanja() {
        logger.debug("Fetching all payments");
        return placanjeRepository.findAll();
    }

    /**
     * Gets a payment by ID
     */
    @Transactional(readOnly = true)
    public Placanje getPlacanjeById(Long id) {
        logger.debug("Fetching payment with ID: {}", id);
        return placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
    }

    /**
     * Gets payment by order ID
     */
    @Transactional(readOnly = true)
    public Placanje getPlacanjeByNarudzbaId(Long narudzbaId) {
        logger.debug("Fetching payment for order ID: {}", narudzbaId);
        Narudzba narudzba = narudzbaRepository.findById(narudzbaId)
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with ID " + narudzbaId + " not found"));
        return placanjeRepository.findByNarudzba(narudzba)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje for order ID " + narudzbaId + " not found"));
    }

    /**
     * Gets payments by payment method
     */
    @Transactional(readOnly = true)
    public List<Placanje> getPlacanjaByNacinPlacanja(NacinPlacanja nacinPlacanja) {
        logger.debug("Fetching payments by payment method: {}", nacinPlacanja);
        return placanjeRepository.findByNacinPlacanja(nacinPlacanja);
    }

    /**
     * Gets payments by status
     */
    @Transactional(readOnly = true)
    public List<Placanje> getPlacanjaByStatus(StatusPlacanja status) {
        logger.debug("Fetching payments by status: {}", status);
        return placanjeRepository.findByStatus(status);
    }

    /**
     * Gets unpaid payments
     */
    @Transactional(readOnly = true)
    public List<Placanje> getUnpaidPayments() {
        logger.debug("Fetching unpaid payments");
        return placanjeRepository.findByStatus(StatusPlacanja.NEPLACENO);
    }

    /**
     * Gets payments by customer ID
     */
    @Transactional(readOnly = true)
    public List<Placanje> getPlacanjaByKupacId(Long kupacId) {
        logger.debug("Fetching payments for customer ID: {}", kupacId);
        return placanjeRepository.findByKupacId(kupacId);
    }

    /**
     * Gets successful payments in date range
     */
    @Transactional(readOnly = true)
    public List<Placanje> getSuccessfulPayments(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Fetching successful payments between {} and {}", startDate, endDate);
        return placanjeRepository.findSuccessfulPayments(startDate, endDate);
    }

    /**
     * Calculates total revenue in date range
     */
    @Transactional(readOnly = true)
    public Double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Calculating total revenue between {} and {}", startDate, endDate);
        return placanjeRepository.getTotalRevenue(startDate, endDate).orElse(0.0);
    }

    /**
     * Gets payment statistics by method
     */
    @Transactional(readOnly = true)
    public List<Object[]> getPaymentStatisticsByMethod() {
        logger.debug("Fetching payment statistics by method");
        return placanjeRepository.getPaymentStatisticsByMethod();
    }

    /**
     * Gets payment statistics by status
     */
    @Transactional(readOnly = true)
    public List<Object[]> getPaymentStatisticsByStatus() {
        logger.debug("Fetching payment statistics by status");
        return placanjeRepository.getPaymentStatisticsByStatus();
    }

    /**
     * Processes a payment (marks as paid)
     */
    public Placanje processPayment(Long id) {
        logger.info("Processing payment with ID: {}", id);
        
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        
        if (placanje.getStatus() == StatusPlacanja.PLACENO) {
            throw new IllegalArgumentException("Payment is already processed");
        }
        
        placanje.setStatus(StatusPlacanja.PLACENO);
        Placanje processedPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment processed successfully: {}", id);
        return processedPlacanje;
    }

    /**
     * Cancels a payment
     */
    public Placanje cancelPayment(Long id) {
        logger.info("Cancelling payment with ID: {}", id);
        
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        
        if (placanje.getStatus() == StatusPlacanja.PLACENO) {
            throw new IllegalArgumentException("Cannot cancel processed payment");
        }
        
        placanje.setStatus(StatusPlacanja.NEPLACENO);
        Placanje cancelledPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment cancelled successfully: {}", id);
        return cancelledPlacanje;
    }

    /**
     * Updates payment method
     */
    public Placanje updatePaymentMethod(Long id, NacinPlacanja newPaymentMethod) {
        logger.info("Updating payment method for ID: {} to {}", id, newPaymentMethod);
        
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        
        if (placanje.getStatus() == StatusPlacanja.PLACENO) {
            throw new IllegalArgumentException("Cannot change payment method for processed payment");
        }
        
        placanje.setNacinPlacanja(newPaymentMethod);
        Placanje updatedPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment method updated successfully: {}", id);
        return updatedPlacanje;
    }

    /**
     * Deletes a payment
     */
    public void deletePlacanje(Long id) {
        logger.info("Deleting payment with ID: {}", id);
        
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        
        if (placanje.getStatus() == StatusPlacanja.PLACENO) {
            throw new IllegalArgumentException("Cannot delete processed payment");
        }
        
        placanjeRepository.deleteById(id);
        logger.info("Payment deleted successfully: {}", id);
    }
}