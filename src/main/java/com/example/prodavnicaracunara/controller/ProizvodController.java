package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.entity.Proizvod;
import com.example.prodavnicaracunara.service.ProizvodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/proizvodi")
public class ProizvodController {

    @Autowired
    private ProizvodService proizvodService;

    @PostMapping
    public ResponseEntity<Proizvod> createProizvod(@Valid @RequestBody Proizvod proizvod) {
        Proizvod createdProizvod = proizvodService.createProizvod(proizvod);
        return new ResponseEntity<>(createdProizvod, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Proizvod>> getAllProizvodi() {
        List<Proizvod> proizvodi = proizvodService.getAllProizvodi();
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proizvod> getProizvodById(@PathVariable Long id) {
        Proizvod proizvod = proizvodService.getProizvodById(id);
        return ResponseEntity.ok(proizvod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proizvod> updateProizvod(@PathVariable Long id, @Valid @RequestBody Proizvod proizvod) {
        Proizvod updatedProizvod = proizvodService.updateProizvod(id, proizvod);
        return ResponseEntity.ok(updatedProizvod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProizvod(@PathVariable Long id) {
        proizvodService.deleteProizvod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Proizvod>> searchProizvodiByNaziv(@RequestParam String naziv) {
        List<Proizvod> proizvodi = proizvodService.searchByNaziv(naziv);
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Proizvod>> getProizvodiInStock() {
        List<Proizvod> proizvodi = proizvodService.getProizvodiInStock();
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Proizvod>> getProizvodiByPriceRange(
            @RequestParam BigDecimal minCena,
            @RequestParam BigDecimal maxCena) {
        List<Proizvod> proizvodi = proizvodService.getProizvodiByPriceRange(minCena, maxCena);
        return ResponseEntity.ok(proizvodi);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Proizvod> updateStock(@PathVariable Long id, @RequestParam Integer novaKolicina) {
        Proizvod updatedProizvod = proizvodService.updateStock(id, novaKolicina);
        return ResponseEntity.ok(updatedProizvod);
    }
}