package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.dto.PlacanjeDTO;
import com.example.prodavnicaracunara.entity.NacinPlacanja;
import com.example.prodavnicaracunara.entity.StatusPlacanja;
import com.example.prodavnicaracunara.service.PlacanjeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/placanja")
public class PlacanjeController {

    private static final Logger logger = LoggerFactory.getLogger(PlacanjeController.class);

    @Autowired
    private PlacanjeService placanjeService;

    @PostMapping
    public ResponseEntity<PlacanjeDTO> createPlacanje(@Valid @RequestBody PlacanjeDTO placanjeDTO) {
        logger.info("REST request to create Placanje for order ID: {}", placanjeDTO.getNarudzbaId());
        PlacanjeDTO createdPlacanje = placanjeService.createPlacanje(placanjeDTO);
        return new ResponseEntity<>(createdPlacanje, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlacanjeDTO>> getAllPlacanja() {
        logger.debug("REST request to get all Placanja");
        List<PlacanjeDTO> placanja = placanjeService.getAllPlacanja();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacanjeDTO> getPlacanjeById(
        logger.debug("REST request to get Placanje by id: {}", id);
        PlacanjeDTO placanje = placanjeService.getPlacanjeById(id);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/narudzba/{narudzbaId}")
    public ResponseEntity<PlacanjeDTO> getPlacanjeByNarudzbaId(
        logger.debug("REST request to get Placanje by narudzba id: {}", narudzbaId);
        PlacanjeDTO placanje = placanjeService.getPlacanjeByNarudzbaId(narudzbaId);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/nacin-placanja/{nacinPlacanja}")
    public ResponseEntity<List<PlacanjeDTO>> getPlacanjaByNacinPlacanja(
        logger.debug("REST request to get Placanja by nacin placanja: {}", nacinPlacanja);
        List<PlacanjeDTO> placanja = placanjeService.getPlacanjaByNacinPlacanja(nacinPlacanja);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PlacanjeDTO>> getPlacanjaByStatus(
        logger.debug("REST request to get Placanja by status: {}", status);
        List<PlacanjeDTO> placanja = placanjeService.getPlacanjaByStatus(status);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<PlacanjeDTO>> getUnpaidPayments() {
        logger.debug("REST request to get unpaid payments");
        List<PlacanjeDTO> placanja = placanjeService.getUnpaidPayments();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/kupac/{kupacId}")
    public ResponseEntity<List<PlacanjeDTO>> getPlacanjaByKupacId(
        logger.debug("REST request to get Placanja by kupac id: {}", kupacId);
        List<PlacanjeDTO> placanja = placanjeService.getPlacanjaByKupacId(kupacId);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/successful")
    public ResponseEntity<List<PlacanjeDTO>> getSuccessfulPayments(
        logger.debug("REST request to get successful payments between {} and {}", startDate, endDate);
        List<PlacanjeDTO> placanja = placanjeService.getSuccessfulPayments(startDate, endDate);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue(
        logger.debug("REST request to get total revenue between {} and {}", startDate, endDate);
        Double totalRevenue = placanjeService.getTotalRevenue(startDate, endDate);
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/statistics/method")
    public ResponseEntity<List<Object[]>> getPaymentStatisticsByMethod() {
        logger.debug("REST request to get payment statistics by method");
        List<Object[]> statistics = placanjeService.getPaymentStatisticsByMethod();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/status")
    public ResponseEntity<List<Object[]>> getPaymentStatisticsByStatus() {
        logger.debug("REST request to get payment statistics by status");
        List<Object[]> statistics = placanjeService.getPaymentStatisticsByStatus();
        return ResponseEntity.ok(statistics);
    }

    @PatchMapping("/{id}/process")
    public ResponseEntity<PlacanjeDTO> processPayment(
        logger.info("REST request to process Placanje with id: {}", id);
        PlacanjeDTO processedPlacanje = placanjeService.processPayment(id);
        return ResponseEntity.ok(processedPlacanje);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PlacanjeDTO> cancelPayment(
        logger.info("REST request to cancel Placanje with id: {}", id);
        PlacanjeDTO cancelledPlacanje = placanjeService.cancelPayment(id);
        return ResponseEntity.ok(cancelledPlacanje);
    }

    @PatchMapping("/{id}/payment-method")
    public ResponseEntity<PlacanjeDTO> updatePaymentMethod(
        logger.info("REST request to update payment method for Placanje id: {} to {}", id, noviNacinPlacanja);
        PlacanjeDTO updatedPlacanje = placanjeService.updatePaymentMethod(id, noviNacinPlacanja);
        return ResponseEntity.ok(updatedPlacanje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlacanje(
        logger.info("REST request to delete Placanje with id: {}", id);
        placanjeService.deletePlacanje(id);
        return ResponseEntity.noContent().build();
    }
}