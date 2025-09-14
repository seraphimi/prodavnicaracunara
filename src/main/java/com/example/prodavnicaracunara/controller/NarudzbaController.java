package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.entity.Narudzba;
import com.example.prodavnicaracunara.entity.StatusNarudzbe;
import com.example.prodavnicaracunara.service.NarudzbaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/narudzbe")
public class NarudzbaController {

    @Autowired
    private NarudzbaService narudzbaService;

    @PostMapping
    public ResponseEntity<Narudzba> createNarudzba(@Valid @RequestBody Narudzba narudzba) {
        Narudzba createdNarudzba = narudzbaService.createNarudzba(narudzba);
        return new ResponseEntity<>(createdNarudzba, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Narudzba>> getAllNarudzbe() {
        List<Narudzba> narudzbe = narudzbaService.getAllNarudzbe();
        return ResponseEntity.ok(narudzbe);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Narudzba> getNarudzbaById(@PathVariable Long id) {
        Narudzba narudzba = narudzbaService.getNarudzbaById(id);
        return ResponseEntity.ok(narudzba);
    }

    @GetMapping("/broj/{brojNarudzbe}")
    public ResponseEntity<Narudzba> getNarudzbaByBrojNarudzbe(@PathVariable String brojNarudzbe) {
        Narudzba narudzba = narudzbaService.getNarudzbaByBrojNarudzbe(brojNarudzbe);
        return ResponseEntity.ok(narudzba);
    }

    @GetMapping("/kupac/{kupacId}")
    public ResponseEntity<List<Narudzba>> getNarudzbeByKupacId(@PathVariable Long kupacId) {
        List<Narudzba> narudzbe = narudzbaService.getNarudzbeByKupacId(kupacId);
        return ResponseEntity.ok(narudzbe);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Narudzba>> getNarudzbeByStatus(@PathVariable StatusNarudzbe status) {
        List<Narudzba> narudzbe = narudzbaService.getNarudzbeByStatus(status);
        return ResponseEntity.ok(narudzbe);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Narudzba>> getActiveOrders() {
        List<Narudzba> narudzbe = narudzbaService.getActiveOrders();
        return ResponseEntity.ok(narudzbe);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Narudzba> updateOrderStatus(@PathVariable Long id, @RequestParam StatusNarudzbe noviStatus) {
        Narudzba updatedNarudzba = narudzbaService.updateOrderStatus(id, noviStatus);
        return ResponseEntity.ok(updatedNarudzba);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Narudzba> cancelOrder(@PathVariable Long id) {
        Narudzba cancelledNarudzba = narudzbaService.cancelOrder(id);
        return ResponseEntity.ok(cancelledNarudzba);
    }
}