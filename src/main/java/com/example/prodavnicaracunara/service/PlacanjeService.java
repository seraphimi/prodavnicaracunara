package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.dto.PlacanjeDTO;
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
import java.util.stream.Collectors;

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
    public PlacanjeDTO createPlacanje(PlacanjeDTO placanjeDTO) {
        logger.info("Creating new payment for order ID: {}", placanjeDTO.getNarudzbaId());
        
        // Validate order exists
        Narudzba narudzba = narudzbaRepository.findById(placanjeDTO.getNarudzbaId())
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with ID " + placanjeDTO.getNarudzbaId() + " not found"));
        
        // Check if payment already exists for this order
        Optional<Placanje> existingPayment = placanjeRepository.findByNarudzba(narudzba);
        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException("Payment already exists for order: " + narudzba.getBrojNarudzbe());
        }
        
        // Create payment
        Placanje placanje = new Placanje();
        placanje.setNarudzba(narudzba);
        placanje.setNacinPlacanja(placanjeDTO.getNacinPlacanja());
        placanje.setStatus(StatusPlacanja.NEPLACENO);
        placanje.setDatum(LocalDateTime.now());
        
        Placanje savedPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment created successfully with ID: {}", savedPlacanje.getId());
        return convertToDTO(savedPlacanje);
    }

    /**
     * Gets all payments
     */
    @Transactional(readOnly = true)
    public List<PlacanjeDTO> getAllPlacanja() {
        logger.debug("Fetching all payments");
        return placanjeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets a payment by ID
     */
    @Transactional(readOnly = true)
    public PlacanjeDTO getPlacanjeById(Long id) {
        logger.debug("Fetching payment with ID: {}", id);
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        return convertToDTO(placanje);
    }

    /**
     * Gets payment by order ID
     */
    @Transactional(readOnly = true)
    public PlacanjeDTO getPlacanjeByNarudzbaId(Long narudzbaId) {
        logger.debug("Fetching payment for order ID: {}", narudzbaId);
        Placanje placanje = placanjeRepository.findByNarudzbaId(narudzbaId)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje for order ID " + narudzbaId + " not found"));
        return convertToDTO(placanje);
    }

    /**
     * Gets payments by payment method
     */
    @Transactional(readOnly = true)
    public List<PlacanjeDTO> getPlacanjaByNacinPlacanja(NacinPlacanja nacinPlacanja) {
        logger.debug("Fetching payments by payment method: {}", nacinPlacanja);
        return placanjeRepository.findByNacinPlacanja(nacinPlacanja)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets payments by status
     */
    @Transactional(readOnly = true)
    public List<PlacanjeDTO> getPlacanjaByStatus(StatusPlacanja status) {
        logger.debug("Fetching payments by status: {}", status);
        return placanjeRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets unpaid payments
     */
    @Transactional(readOnly = true)
    public List<PlacanjeDTO> getUnpaidPayments() {
        logger.debug("Fetching unpaid payments");
        return placanjeRepository.findUnpaidPayments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Processes a payment (marks as paid)
     */
    public PlacanjeDTO processPayment(Long id) {
        logger.info("Processing payment with ID: {}", id);
        
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        
        if (placanje.getStatus() == StatusPlacanja.PLACENO) {
            throw new IllegalArgumentException("Payment is already processed");
        }
        
        if (placanje.getStatus() == StatusPlacanja.OTKAZANO) {
            throw new IllegalArgumentException("Cannot process cancelled payment");
        }
        
        placanje.setStatus(StatusPlacanja.PLACENO);
        placanje.setDatum(LocalDateTime.now()); // Update to payment processing time
        
        Placanje processedPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment processed successfully: {}", id);
        return convertToDTO(processedPlacanje);
    }

    /**
     * Cancels a payment
     */
    public PlacanjeDTO cancelPayment(Long id) {
        logger.info("Cancelling payment with ID: {}", id);
        
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        
        if (placanje.getStatus() == StatusPlacanja.PLACENO) {
            throw new IllegalArgumentException("Cannot cancel processed payment");
        }
        
        placanje.setStatus(StatusPlacanja.OTKAZANO);
        
        Placanje cancelledPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment cancelled successfully: {}", id);
        return convertToDTO(cancelledPlacanje);
    }

    /**
     * Updates payment method
     */
    public PlacanjeDTO updatePaymentMethod(Long id, NacinPlacanja newPaymentMethod) {
        logger.info("Updating payment method for ID: {} to {}", id, newPaymentMethod);
        
        Placanje placanje = placanjeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placanje with ID " + id + " not found"));
        
        if (placanje.getStatus() == StatusPlacanja.PLACENO) {
            throw new IllegalArgumentException("Cannot update payment method of processed payment");
        }
        
        if (placanje.getStatus() == StatusPlacanja.OTKAZANO) {
            throw new IllegalArgumentException("Cannot update payment method of cancelled payment");
        }
        
        placanje.setNacinPlacanja(newPaymentMethod);
        
        Placanje updatedPlacanje = placanjeRepository.save(placanje);
        
        logger.info("Payment method updated successfully: {}", id);
        return convertToDTO(updatedPlacanje);
    }

    /**
     * Gets payments for a specific customer
     */
    @Transactional(readOnly = true)
    public List<PlacanjeDTO> getPlacanjaByKupacId(Long kupacId) {
        logger.debug("Fetching payments for customer ID: {}", kupacId);
        return placanjeRepository.findByKupacId(kupacId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets successful payments in date range
     */
    @Transactional(readOnly = true)
    public List<PlacanjeDTO> getSuccessfulPayments(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Fetching successful payments between {} and {}", startDate, endDate);
        return placanjeRepository.findSuccessfulPayments(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets total revenue for date range
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
     * Deletes a payment (only if not processed)
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

    // Helper method for conversion
    private PlacanjeDTO convertToDTO(Placanje placanje) {
        PlacanjeDTO dto = new PlacanjeDTO();
        dto.setId(placanje.getId());
        dto.setNarudzbaId(placanje.getNarudzba().getId());
        dto.setNacinPlacanja(placanje.getNacinPlacanja());
        dto.setStatus(placanje.getStatus());
        dto.setDatum(placanje.getDatum());
        return dto;
    }
}