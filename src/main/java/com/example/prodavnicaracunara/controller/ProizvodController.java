package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.dto.ProizvodDTO;
import com.example.prodavnicaracunara.service.ProizvodService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/proizvodi")
@Tag(name = "Proizvodi", description = "API za upravljanje proizvodima")
public class ProizvodController {

    private static final Logger logger = LoggerFactory.getLogger(ProizvodController.class);

    @Autowired
    private ProizvodService proizvodService;

    @PostMapping
    @Operation(summary = "Kreiranje novog proizvoda", description = "Kreira novi proizvod u sistemu")
    public ResponseEntity<ProizvodDTO> createProizvod(@Valid @RequestBody ProizvodDTO proizvodDTO) {
        logger.info("REST request to create Proizvod: {}", proizvodDTO.getNaziv());
        ProizvodDTO createdProizvod = proizvodService.createProizvod(proizvodDTO);
        return new ResponseEntity<>(createdProizvod, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Dohvatanje svih proizvoda", description = "Vraća listu svih proizvoda")
    public ResponseEntity<List<ProizvodDTO>> getAllProizvodi() {
        logger.debug("REST request to get all Proizvodi");
        List<ProizvodDTO> proizvodi = proizvodService.getAllProizvodi();
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dohvatanje proizvoda po ID-u", description = "Vraća proizvod sa specifičnim ID-om")
    public ResponseEntity<ProizvodDTO> getProizvodById(
            @Parameter(description = "ID proizvoda") @PathVariable Long id) {
        logger.debug("REST request to get Proizvod by id: {}", id);
        ProizvodDTO proizvod = proizvodService.getProizvodById(id);
        return ResponseEntity.ok(proizvod);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Ažuriranje proizvoda", description = "Ažurira postojeći proizvod")
    public ResponseEntity<ProizvodDTO> updateProizvod(
            @Parameter(description = "ID proizvoda") @PathVariable Long id,
            @Valid @RequestBody ProizvodDTO proizvodDTO) {
        logger.info("REST request to update Proizvod with id: {}", id);
        ProizvodDTO updatedProizvod = proizvodService.updateProizvod(id, proizvodDTO);
        return ResponseEntity.ok(updatedProizvod);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje proizvoda", description = "Briše proizvod sa specifičnim ID-om")
    public ResponseEntity<Void> deleteProizvod(
            @Parameter(description = "ID proizvoda") @PathVariable Long id) {
        logger.info("REST request to delete Proizvod with id: {}", id);
        proizvodService.deleteProizvod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Pretraga proizvoda po nazivu", description = "Vraća proizvode koji sadrže određeni tekst u nazivu")
    public ResponseEntity<List<ProizvodDTO>> searchProizvodiByNaziv(
            @Parameter(description = "Naziv za pretragu") @RequestParam String naziv) {
        logger.debug("REST request to search Proizvodi by naziv: {}", naziv);
        List<ProizvodDTO> proizvodi = proizvodService.searchByNaziv(naziv);
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/in-stock")
    @Operation(summary = "Proizvodi na stanju", description = "Vraća sve proizvode koji su dostupni na stanju")
    public ResponseEntity<List<ProizvodDTO>> getProizvodiInStock() {
        logger.debug("REST request to get Proizvodi in stock");
        List<ProizvodDTO> proizvodi = proizvodService.getProizvodiInStock();
        return ResponseEntity.ok(proizvodi);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Proizvodi u cenovnom opsegu", description = "Vraća proizvode u određenom cenovnom opsegu")
    public ResponseEntity<List<ProizvodDTO>> getProizvodiByPriceRange(
            @Parameter(description = "Minimalna cena") @RequestParam BigDecimal minCena,
            @Parameter(description = "Maksimalna cena") @RequestParam BigDecimal maxCena) {
        logger.debug("REST request to get Proizvodi by price range: {} - {}", minCena, maxCena);
        List<ProizvodDTO> proizvodi = proizvodService.getProizvodiByPriceRange(minCena, maxCena);
        return ResponseEntity.ok(proizvodi);
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Ažuriranje stanja", description = "Ažurira količinu proizvoda na stanju")
    public ResponseEntity<ProizvodDTO> updateStock(
            @Parameter(description = "ID proizvoda") @PathVariable Long id,
            @Parameter(description = "Nova količina") @RequestParam Integer novaKolicina) {
        logger.info("REST request to update stock for Proizvod id: {} to {}", id, novaKolicina);
        ProizvodDTO updatedProizvod = proizvodService.updateStock(id, novaKolicina);
        return ResponseEntity.ok(updatedProizvod);
    }
}