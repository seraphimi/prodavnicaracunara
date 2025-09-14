package com.example.prodavnicaracunara.service;

import com.example.prodavnicaracunara.entity.*;
import com.example.prodavnicaracunara.exception.ResourceNotFoundException;
import com.example.prodavnicaracunara.repository.KupacRepository;
import com.example.prodavnicaracunara.repository.NarudzbaRepository;
import com.example.prodavnicaracunara.repository.ProizvodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NarudzbaService {

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
        // Generate order number
        narudzba.setBrojNarudzbe("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        narudzba.setDatumKreiranja(LocalDateTime.now());
        narudzba.setStatus(StatusNarudzbe.U_OBRADI);
        
        // Calculate total price and reduce stock
        if (narudzba.getProizvodi() != null && !narudzba.getProizvodi().isEmpty()) {
            BigDecimal total = BigDecimal.ZERO;
            for (Proizvod proizvod : narudzba.getProizvodi()) {
                proizvodService.reduceStock(proizvod.getId(), 1); // Assuming quantity is 1 for simplicity
                total = total.add(proizvod.getCena());
            }
            narudzba.setUkupnaCena(total);
        }
        
        return narudzbaRepository.save(narudzba);
    }

    /**
     * Gets all orders
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getAllNarudzbe() {
        return narudzbaRepository.findAll();
    }

    /**
     * Gets order by ID
     */
    @Transactional(readOnly = true)
    public Narudzba getNarudzbaById(Long id) {
        return narudzbaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with ID " + id + " not found"));
    }

    /**
     * Gets order by order number
     */
    @Transactional(readOnly = true)
    public Narudzba getNarudzbaByBrojNarudzbe(String brojNarudzbe) {
        return narudzbaRepository.findByBrojNarudzbe(brojNarudzbe)
                .orElseThrow(() -> new ResourceNotFoundException("Narudzba with number " + brojNarudzbe + " not found"));
    }

    /**
     * Gets orders by customer ID
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getNarudzbeByKupacId(Long kupacId) {
        return narudzbaRepository.findByKupacId(kupacId);
    }

    /**
     * Gets orders by status
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getNarudzbeByStatus(StatusNarudzbe status) {
        return narudzbaRepository.findByStatus(status);
    }

    /**
     * Gets active orders
     */
    @Transactional(readOnly = true)
    public List<Narudzba> getActiveOrders() {
        return narudzbaRepository.findActiveOrders();
    }

    /**
     * Updates order status
     */
    public Narudzba updateOrderStatus(Long id, StatusNarudzbe noviStatus) {
        Narudzba narudzba = getNarudzbaById(id);
        narudzba.setStatus(noviStatus);
        return narudzbaRepository.save(narudzba);
    }

    /**
     * Cancels an order
     */
    public Narudzba cancelOrder(Long id) {
        Narudzba narudzba = getNarudzbaById(id);
        
        if (narudzba.getStatus() == StatusNarudzbe.ISPORUCENA) {
            throw new IllegalStateException("Cannot cancel delivered order");
        }
        
        narudzba.setStatus(StatusNarudzbe.U_OBRADI); // Set to processing as there's no CANCELLED status
        return narudzbaRepository.save(narudzba);
    }

    /**
     * Processes order status updates (for background task)
     */
    public void processOrderStatusUpdates() {
        List<Narudzba> activeOrders = getActiveOrders();
        
        for (Narudzba narudzba : activeOrders) {
            // Simple logic to progress orders through statuses
            if (narudzba.getStatus() == StatusNarudzbe.U_OBRADI) {
                narudzba.setStatus(StatusNarudzbe.POSLATA);
                narudzbaRepository.save(narudzba);
            } else if (narudzba.getStatus() == StatusNarudzbe.POSLATA) {
                narudzba.setStatus(StatusNarudzbe.ISPORUCENA);
                narudzbaRepository.save(narudzba);
            }
        }
    }
}