package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.entity.Proizvod;
import com.example.prodavnicaracunara.service.ProizvodService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/proizvodi")
public class ProizvodController {

    private static final Logger logger = LoggerFactory.getLogger(ProizvodController.class);

    @Autowired
    private ProizvodService proizvodService;

    @PostMapping
    public ResponseEntity<Proizvod> createProizvod(@Valid @RequestBody Proizvod proizvod) {
        logger.info("REST request to create Proizvod: {}", proizvod.getNaziv());
        Proizvod createdProizvod = proizvodService.createProizvod(proizvod);
        return new ResponseEntity<>(createdProizvod, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Proizvod>> getAllProizvodi() {
        logger.debug("REST request to get all Proizvodi");
        List<Proizvod> proizvodi = proizvodService.getAllProizvodi();
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proizvod> getProizvodById(@PathVariable Long id) {
        logger.debug("REST request to get Proizvod by id: {}", id);
        Proizvod proizvod = proizvodService.getProizvodById(id);
        return ResponseEntity.ok(proizvod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proizvod> updateProizvod(@PathVariable Long id, @Valid @RequestBody Proizvod proizvod) {
        logger.info("REST request to update Proizvod with id: {}", id);
        Proizvod updatedProizvod = proizvodService.updateProizvod(id, proizvod);
        return ResponseEntity.ok(updatedProizvod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProizvod(@PathVariable Long id) {
        logger.info("REST request to delete Proizvod with id: {}", id);
        proizvodService.deleteProizvod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Proizvod>> searchProizvodiByNaziv(@RequestParam String naziv) {
        logger.debug("REST request to search Proizvodi by naziv: {}", naziv);
        List<Proizvod> proizvodi = proizvodService.searchByNaziv(naziv);
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Proizvod>> getProizvodiInStock() {
        logger.debug("REST request to get Proizvodi in stock");
        List<Proizvod> proizvodi = proizvodService.getProizvodiInStock();
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Proizvod>> getProizvodiByPriceRange(
            @RequestParam BigDecimal minCena, @RequestParam BigDecimal maxCena) {
        logger.debug("REST request to get Proizvodi by price range: {} - {}", minCena, maxCena);
        List<Proizvod> proizvodi = proizvodService.getProizvodiByPriceRange(minCena, maxCena);
        return ResponseEntity.ok(proizvodi);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Proizvod> updateStock(@PathVariable Long id, @RequestParam Integer novaKolicina) {
        logger.info("REST request to update stock for Proizvod id: {} to {}", id, novaKolicina);
        Proizvod updatedProizvod = proizvodService.updateStock(id, novaKolicina);
        return ResponseEntity.ok(updatedProizvod);
    }
}