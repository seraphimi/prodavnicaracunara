package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.entity.NacinPlacanja;
import com.example.prodavnicaracunara.entity.Placanje;
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
    public ResponseEntity<Placanje> createPlacanje(@Valid @RequestBody Placanje placanje) {
        logger.info("REST request to create Placanje for order ID: {}", placanje.getNarudzba() != null ? placanje.getNarudzba().getId() : "null");
        Placanje createdPlacanje = placanjeService.createPlacanje(placanje);
        return new ResponseEntity<>(createdPlacanje, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Placanje>> getAllPlacanja() {
        logger.debug("REST request to get all Placanja");
        List<Placanje> placanja = placanjeService.getAllPlacanja();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Placanje> getPlacanjeById(@PathVariable Long id) {
        logger.debug("REST request to get Placanje by id: {}", id);
        Placanje placanje = placanjeService.getPlacanjeById(id);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/narudzba/{narudzbaId}")
    public ResponseEntity<Placanje> getPlacanjeByNarudzbaId(@PathVariable Long narudzbaId) {
        logger.debug("REST request to get Placanje by narudzba id: {}", narudzbaId);
        Placanje placanje = placanjeService.getPlacanjeByNarudzbaId(narudzbaId);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/nacin-placanja/{nacinPlacanja}")
    public ResponseEntity<List<Placanje>> getPlacanjaByNacinPlacanja(@PathVariable NacinPlacanja nacinPlacanja) {
        logger.debug("REST request to get Placanja by nacin placanja: {}", nacinPlacanja);
        List<Placanje> placanja = placanjeService.getPlacanjaByNacinPlacanja(nacinPlacanja);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Placanje>> getPlacanjaByStatus(@PathVariable StatusPlacanja status) {
        logger.debug("REST request to get Placanja by status: {}", status);
        List<Placanje> placanja = placanjeService.getPlacanjaByStatus(status);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Placanje>> getUnpaidPayments() {
        logger.debug("REST request to get unpaid payments");
        List<Placanje> placanja = placanjeService.getUnpaidPayments();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/kupac/{kupacId}")
    public ResponseEntity<List<Placanje>> getPlacanjaByKupacId(@PathVariable Long kupacId) {
        logger.debug("REST request to get Placanja by kupac id: {}", kupacId);
        List<Placanje> placanja = placanjeService.getPlacanjaByKupacId(kupacId);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/successful")
    public ResponseEntity<List<Placanje>> getSuccessfulPayments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        logger.debug("REST request to get successful payments between {} and {}", startDate, endDate);
        List<Placanje> placanja = placanjeService.getSuccessfulPayments(startDate, endDate);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
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
    public ResponseEntity<Placanje> processPayment(@PathVariable Long id) {
        logger.info("REST request to process Placanje with id: {}", id);
        Placanje processedPlacanje = placanjeService.processPayment(id);
        return ResponseEntity.ok(processedPlacanje);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Placanje> cancelPayment(@PathVariable Long id) {
        logger.info("REST request to cancel Placanje with id: {}", id);
        Placanje cancelledPlacanje = placanjeService.cancelPayment(id);
        return ResponseEntity.ok(cancelledPlacanje);
    }

    @PatchMapping("/{id}/payment-method")
    public ResponseEntity<Placanje> updatePaymentMethod(@PathVariable Long id, @RequestParam NacinPlacanja noviNacinPlacanja) {
        logger.info("REST request to update payment method for Placanje id: {} to {}", id, noviNacinPlacanja);
        Placanje updatedPlacanje = placanjeService.updatePaymentMethod(id, noviNacinPlacanja);
        return ResponseEntity.ok(updatedPlacanje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlacanje(@PathVariable Long id) {
        logger.info("REST request to delete Placanje with id: {}", id);
        placanjeService.deletePlacanje(id);
        return ResponseEntity.noContent().build();
    }
}