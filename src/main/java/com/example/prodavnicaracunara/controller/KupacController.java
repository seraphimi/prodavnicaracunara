package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.entity.Kupac;
import com.example.prodavnicaracunara.service.KupacService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kupci")
public class KupacController {

    @Autowired
    private KupacService kupacService;

    @PostMapping
    public ResponseEntity<Kupac> createKupac(@Valid @RequestBody Kupac kupac) {
        Kupac createdKupac = kupacService.createKupac(kupac);
        return new ResponseEntity<>(createdKupac, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Kupac>> getAllKupci() {
        List<Kupac> kupci = kupacService.getAllKupci();
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kupac> getKupacById(@PathVariable Long id) {
        Kupac kupac = kupacService.getKupacById(id);
        return ResponseEntity.ok(kupac);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Kupac> getKupacByEmail(@PathVariable String email) {
        Kupac kupac = kupacService.getKupacByEmail(email);
        return ResponseEntity.ok(kupac);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kupac> updateKupac(@PathVariable Long id, @Valid @RequestBody Kupac kupac) {
        Kupac updatedKupac = kupacService.updateKupac(id, kupac);
        return ResponseEntity.ok(updatedKupac);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKupac(@PathVariable Long id) {
        kupacService.deleteKupac(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/ime")
    public ResponseEntity<List<Kupac>> searchKupciByIme(@RequestParam String ime) {
        List<Kupac> kupci = kupacService.searchByIme(ime);
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/search/prezime")
    public ResponseEntity<List<Kupac>> searchKupciByPrezime(@RequestParam String prezime) {
        List<Kupac> kupci = kupacService.searchByPrezime(prezime);
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/search/puno-ime")
    public ResponseEntity<List<Kupac>> searchKupciByPunoIme(@RequestParam String punoIme) {
        List<Kupac> kupci = kupacService.searchByPunoIme(punoIme);
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/search/adresa")
    public ResponseEntity<List<Kupac>> searchKupciByAdresa(@RequestParam String adresa) {
        List<Kupac> kupci = kupacService.searchByAdresa(adresa);
        return ResponseEntity.ok(kupci);
    }
}