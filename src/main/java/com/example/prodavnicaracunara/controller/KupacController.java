package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.dto.KupacDTO;
import com.example.prodavnicaracunara.service.KupacService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kupci")
@Tag(name = "Kupci", description = "API za upravljanje kupcima")
public class KupacController {

    private static final Logger logger = LoggerFactory.getLogger(KupacController.class);

    @Autowired
    private KupacService kupacService;

    @PostMapping
    @Operation(summary = "Registracija novog kupca", description = "Registruje novog kupca u sistemu")
    public ResponseEntity<KupacDTO> createKupac(@Valid @RequestBody KupacDTO kupacDTO) {
        logger.info("REST request to create Kupac: {} {}", kupacDTO.getIme(), kupacDTO.getPrezime());
        KupacDTO createdKupac = kupacService.createKupac(kupacDTO);
        return new ResponseEntity<>(createdKupac, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Dohvatanje svih kupaca", description = "Vraća listu svih registrovanih kupaca")
    public ResponseEntity<List<KupacDTO>> getAllKupci() {
        logger.debug("REST request to get all Kupci");
        List<KupacDTO> kupci = kupacService.getAllKupci();
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dohvatanje kupca po ID-u", description = "Vraća kupca sa specifičnim ID-om")
    public ResponseEntity<KupacDTO> getKupacById(
            @Parameter(description = "ID kupca") @PathVariable Long id) {
        logger.debug("REST request to get Kupac by id: {}", id);
        KupacDTO kupac = kupacService.getKupacById(id);
        return ResponseEntity.ok(kupac);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Dohvatanje kupca po email-u", description = "Vraća kupca sa specifičnim email-om")
    public ResponseEntity<KupacDTO> getKupacByEmail(
            @Parameter(description = "Email kupca") @PathVariable String email) {
        logger.debug("REST request to get Kupac by email: {}", email);
        KupacDTO kupac = kupacService.getKupacByEmail(email);
        return ResponseEntity.ok(kupac);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Ažuriranje kupca", description = "Ažurira podatke postojećeg kupca")
    public ResponseEntity<KupacDTO> updateKupac(
            @Parameter(description = "ID kupca") @PathVariable Long id,
            @Valid @RequestBody KupacDTO kupacDTO) {
        logger.info("REST request to update Kupac with id: {}", id);
        KupacDTO updatedKupac = kupacService.updateKupac(id, kupacDTO);
        return ResponseEntity.ok(updatedKupac);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje kupca", description = "Briše kupca sa specifičnim ID-om")
    public ResponseEntity<Void> deleteKupac(
            @Parameter(description = "ID kupca") @PathVariable Long id) {
        logger.info("REST request to delete Kupac with id: {}", id);
        kupacService.deleteKupac(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/ime")
    @Operation(summary = "Pretraga kupaca po imenu", description = "Vraća kupce koji sadrže određeni tekst u imenu")
    public ResponseEntity<List<KupacDTO>> searchKupciByIme(
            @Parameter(description = "Ime za pretragu") @RequestParam String ime) {
        logger.debug("REST request to search Kupci by ime: {}", ime);
        List<KupacDTO> kupci = kupacService.searchByIme(ime);
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/search/prezime")
    @Operation(summary = "Pretraga kupaca po prezimenu", description = "Vraća kupce koji sadrže određeni tekst u prezimenu")
    public ResponseEntity<List<KupacDTO>> searchKupciByPrezime(
            @Parameter(description = "Prezime za pretragu") @RequestParam String prezime) {
        logger.debug("REST request to search Kupci by prezime: {}", prezime);
        List<KupacDTO> kupci = kupacService.searchByPrezime(prezime);
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/search/puno-ime")
    @Operation(summary = "Pretraga kupaca po punom imenu", description = "Vraća kupce koji sadrže određeni tekst u punom imenu")
    public ResponseEntity<List<KupacDTO>> searchKupciByPunoIme(
            @Parameter(description = "Puno ime za pretragu") @RequestParam String punoIme) {
        logger.debug("REST request to search Kupci by puno ime: {}", punoIme);
        List<KupacDTO> kupci = kupacService.searchByPunoIme(punoIme);
        return ResponseEntity.ok(kupci);
    }

    @GetMapping("/search/adresa")
    @Operation(summary = "Pretraga kupaca po adresi", description = "Vraća kupce koji sadrže određeni tekst u adresi")
    public ResponseEntity<List<KupacDTO>> searchKupciByAdresa(
            @Parameter(description = "Adresa za pretragu") @RequestParam String adresa) {
        logger.debug("REST request to search Kupci by adresa: {}", adresa);
        List<KupacDTO> kupci = kupacService.searchByAdresa(adresa);
        return ResponseEntity.ok(kupci);
    }
}