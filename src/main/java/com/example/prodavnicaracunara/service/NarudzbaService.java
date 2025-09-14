package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.*;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.KupacRepository;
import com.example.prodavnicaracunara.repository.NarudzbaRepository;
import com.example.prodavnicaracunara.repository.ProizvodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class NarudzbaService {

    private static final Logger logger = LoggerFactory.getLogger(NarudzbaService.class);

    @Autowired
    private NarudzbaRepository narudzbaRepository;
    
    @Autowired
    private KupacRepository kupacRepository;
    
    @Autowired
    private ProizvodRepository proizvodRepository;
    
    @Autowired
    private ProizvodService proizvodService;

    /**
     * Creates a new order
     */
    public Narudzba createNarudzba(Narudzba narudzba) {
        logger.info("Creating new order for customer ID: {}", narudzba.getKupac() != null ? narudzba.getKupac().getId() : "null");
        
        // Validate customer exists
        if (narudzba.getKupac() == null || narudzba.getKupac().getId() == null) {
            throw new IllegalArgumentException("Customer is required for order");
        }
        
        Kupac kupac = kupacRepository.findById(narudzba.getKupac().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Kupac with ID " + narudzba.getKupac().getId() + " not found"));
        
        // Validate products exist and calculate total price
        List<Proizvod> proizvodi = narudzba.getProizvodi();
        if (proizvodi == null || proizvodi.isEmpty()) {
            throw new IllegalArgumentException("At least one product is required for order");
        }
        
        // Fetch full product details and validate
        List<Long> proizvodIds = proizvodi.stream().map(Proizvod::getId).collect(Collectors.toList());
        List<Proizvod> fullProizvodi = validateAndGetProizvodi(proizvodIds);
        BigDecimal calculatedTotal = calculateTotalPrice(fullProizvodi);
        
        // Check stock availability
        validateStockAvailability(fullProizvodi);
        
        // Generate unique order number
        String brojNarudzbe;
        do {
            brojNarudzbe = generateOrderNumber();
        } while (narudzbaRepository.existsByBrojNarudzbe(brojNarudzbe));
        
        // Create order
        narudzba.setBrojNarudzbe(brojNarudzbe);
        narudzba.setKupac(kupac);
        narudzba.setProizvodi(fullProizvodi);
        narudzba.setUkupnaCena(calculatedTotal);
        narudzba.setStatus(StatusNarudzbe.U_OBRADI);
        narudzba.setDatumKreiranja(LocalDateTime.now());
        
        Narudzba savedNarudzba = narudzbaRepository.save(narudzba);
        
        // Reduce stock for ordered products
        for (Proizvod proizvod : fullProizvodi) {
            proizvodService.reduceStock(proizvod.getId(), 1); // Assuming quantity 1 for each product
        }
        
        logger.info("Order created successfully with number: {}", brojNarudzbe);
        return savedNarudzba;
    }

    /**
     * Gets all orders
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getAllNarudzbe() {
        logger.debug("Fetching all orders");
        return narudzbaRepository.findAll();
    }

    /**
     * Gets an order by ID
     */
    @Transactional(readOnly = true)
    public Narudzba getNarudzbaById(Long id) {
        logger.debug("Fetching order with ID: {}", id);
        return narudzbaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with ID " + id + " not found"));
    }

    /**
     * Gets an order by order number
     */
    @Transactional(readOnly = true)
    public Narudzba getNarudzbaByBrojNarudzbe(String brojNarudzbe) {
        logger.debug("Fetching order with number: {}", brojNarudzbe);
        return narudzbaRepository.findByBrojNarudzbe(brojNarudzbe)
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with number " + brojNarudzbe + " not found"));
    }

    /**
     * Gets orders by customer ID
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getNarudzbeByKupacId(Long kupacId) {
        logger.debug("Fetching orders for customer ID: {}", kupacId);
        return narudzbaRepository.findByKupacId(kupacId);
    }

    /**
     * Gets orders by status
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getNarudzbeByStatus(StatusNarudzbe status) {
        logger.debug("Fetching orders with status: {}", status);
        return narudzbaRepository.findByStatus(status);
    }

    /**
     * Updates order status
     */
    public Narudzba updateOrderStatus(Long id, StatusNarudzbe newStatus) {
        logger.info("Updating order status for ID: {} to {}", id, newStatus);
        
        Narudzba narudzba = narudzbaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with ID " + id + " not found"));
        
        // Validate status transition
        validateStatusTransition(narudzba.getStatus(), newStatus);
        
        narudzba.setStatus(newStatus);
        Narudzba updatedNarudzba = narudzbaRepository.save(narudzba);
        
        logger.info("Order status updated successfully: {}", id);
        return updatedNarudzba;
    }

    /**
     * Cancels an order
     */
    public Narudzba cancelOrder(Long id) {
        logger.info("Cancelling order with ID: {}", id);
        
        Narudzba narudzba = narudzbaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with ID " + id + " not found"));
        
        if (narudzba.getStatus() == StatusNarudzbe.ISPORUCENA) {
            throw new IllegalArgumentException("Cannot cancel delivered order");
        }
        
        // Restore stock if order was not delivered
        if (narudzba.getStatus() != StatusNarudzbe.OTKAZANA) {
            for (Proizvod proizvod : narudzba.getProizvodi()) {
                proizvodService.updateStock(proizvod.getId(), proizvod.getKolicinaUStanju() + 1);
            }
        }
        
        narudzba.setStatus(StatusNarudzbe.OTKAZANA);
        Narudzba cancelledNarudzba = narudzbaRepository.save(narudzba);
        
        logger.info("Order cancelled successfully: {}", id);
        return cancelledNarudzba;
    }

    /**
     * Gets active orders for monitoring
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getActiveOrders() {
        logger.debug("Fetching active orders for monitoring");
        return narudzbaRepository.findActiveOrders();
    }

    /**
     * Updates orders that need status progression
     */
    public void processOrderStatusUpdates() {
        logger.debug("Processing order status updates");
        
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);
        List<Narudzba> ordersToUpdate = narudzbaRepository.findOrdersForStatusUpdate(cutoffTime);
        
        for (Narudzba narudzba : ordersToUpdate) {
            // Simple logic: U_OBRADI -> POSLATA -> ISPORUCENA
            if (narudzba.getStatus() == StatusNarudzbe.U_OBRADI) {
                narudzba.setStatus(StatusNarudzbe.POSLATA);
                narudzbaRepository.save(narudzba);
                logger.info("Order {} status updated to POSLATA", narudzba.getBrojNarudzbe());
            }
        }
        
        // Update POSLATA orders older than 10 minutes to ISPORUCENA
        LocalDateTime deliveryCutoff = LocalDateTime.now().minusMinutes(10);
        List<Narudzba> deliveryOrders = narudzbaRepository.findByStatus(StatusNarudzbe.POSLATA)
                .stream()
                .filter(n -> n.getDatumKreiranja().isBefore(deliveryCutoff))
                .collect(Collectors.toList());
        
        for (Narudzba narudzba : deliveryOrders) {
            narudzba.setStatus(StatusNarudzbe.ISPORUCENA);
            narudzbaRepository.save(narudzba);
            logger.info("Order {} status updated to ISPORUCENA", narudzba.getBrojNarudzbe());
        }
    }

    // Helper methods
    private List<Proizvod> validateAndGetProizvodi(List<Long> proizvodIds) {
        List<Proizvod> proizvodi = proizvodRepository.findAllById(proizvodIds);
        if (proizvodi.size() != proizvodIds.size()) {
            throw new ResourceNotFoundException("Some products not found");
        }
        return proizvodi;
    }
    
    private BigDecimal calculateTotalPrice(List<Proizvod> proizvodi) {
        return proizvodi.stream()
                .map(Proizvod::getCena)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private void validateStockAvailability(List<Proizvod> proizvodi) {
        for (Proizvod proizvod : proizvodi) {
            if (proizvod.getKolicinaUStanju() <= 0) {
                throw new IllegalArgumentException("Product out of stock: " + proizvod.getNaziv());
            }
        }
    }
    
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private void validateStatusTransition(StatusNarudzbe currentStatus, StatusNarudzbe newStatus) {
        // Define valid transitions
        if (currentStatus == StatusNarudzbe.ISPORUCENA || currentStatus == StatusNarudzbe.OTKAZANA) {
            throw new IllegalArgumentException("Cannot change status of delivered or cancelled order");
        }
    }
}