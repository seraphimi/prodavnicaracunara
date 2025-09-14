package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.entity.Placanje;
import com.example.prodavnicaracunara.entity.NacinPlacanja;
import com.example.prodavnicaracunara.entity.StatusPlacanja;
import com.example.prodavnicaracunara.service.PlacanjeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/placanja")
public class PlacanjeController {

    @Autowired
    private PlacanjeService placanjeService;

    @PostMapping
    public ResponseEntity<Placanje> createPlacanje(@Valid @RequestBody Placanje placanje) {
        Placanje createdPlacanje = placanjeService.createPlacanje(placanje);
        return new ResponseEntity<>(createdPlacanje, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Placanje>> getAllPlacanja() {
        List<Placanje> placanja = placanjeService.getAllPlacanja();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Placanje> getPlacanjeById(@PathVariable Long id) {
        Placanje placanje = placanjeService.getPlacanjeById(id);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/narudzba/{narudzbaId}")
    public ResponseEntity<Placanje> getPlacanjeByNarudzbaId(@PathVariable Long narudzbaId) {
        Placanje placanje = placanjeService.getPlacanjeByNarudzbaId(narudzbaId);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/nacin-placanja/{nacinPlacanja}")
    public ResponseEntity<List<Placanje>> getPlacanjaByNacinPlacanja(@PathVariable NacinPlacanja nacinPlacanja) {
        List<Placanje> placanja = placanjeService.getPlacanjaByNacinPlacanja(nacinPlacanja);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Placanje>> getPlacanjaByStatus(@PathVariable StatusPlacanja status) {
        List<Placanje> placanja = placanjeService.getPlacanjaByStatus(status);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Placanje>> getPendingPlacanja() {
        List<Placanje> placanja = placanjeService.getPendingPlacanja();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Placanje>> getCompletedPlacanja() {
        List<Placanje> placanja = placanjeService.getCompletedPlacanja();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        BigDecimal revenue = placanjeService.getTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/period")
    public ResponseEntity<BigDecimal> getRevenueByPeriod(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        BigDecimal revenue = placanjeService.getRevenueByPeriod(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Placanje> updatePlacanjeStatus(@PathVariable Long id, @RequestParam StatusPlacanja noviStatus) {
        Placanje updatedPlacanje = placanjeService.updatePlacanjeStatus(id, noviStatus);
        return ResponseEntity.ok(updatedPlacanje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Placanje> updatePlacanje(@PathVariable Long id, @Valid @RequestBody Placanje placanje) {
        Placanje updatedPlacanje = placanjeService.updatePlacanje(id, placanje);
        return ResponseEntity.ok(updatedPlacanje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlacanje(@PathVariable Long id) {
        placanjeService.deletePlacanje(id);
        return ResponseEntity.noContent().build();
    }
}