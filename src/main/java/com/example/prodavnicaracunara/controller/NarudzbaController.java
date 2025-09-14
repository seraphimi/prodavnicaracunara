package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.dto.NarudzbaDTO;
import com.example.prodavnicaracunara.entity.StatusNarudzbe;
import com.example.prodavnicaracunara.service.NarudzbaService;
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
@RequestMapping("/narudzbe")
@Tag(name = "Narudžbe", description = "API za upravljanje narudžbama")
public class NarudzbaController {

    private static final Logger logger = LoggerFactory.getLogger(NarudzbaController.class);

    @Autowired
    private NarudzbaService narudzbaService;

    @PostMapping
    @Operation(summary = "Kreiranje nove narudžbe", description = "Kreira novu narudžbu u sistemu")
    public ResponseEntity<NarudzbaDTO> createNarudzba(@Valid @RequestBody NarudzbaDTO narudzbaDTO) {
        logger.info("REST request to create Narudzba for customer ID: {}", narudzbaDTO.getKupacId());
        NarudzbaDTO createdNarudzba = narudzbaService.createNarudzba(narudzbaDTO);
        return new ResponseEntity<>(createdNarudzba, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Dohvatanje svih narudžbi", description = "Vraća listu svih narudžbi")
    public ResponseEntity<List<NarudzbaDTO>> getAllNarudzbe() {
        logger.debug("REST request to get all Narudzbe");
        List<NarudzbaDTO> narudzbe = narudzbaService.getAllNarudzbe();
        return ResponseEntity.ok(narudzbe);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dohvatanje narudžbe po ID-u", description = "Vraća narudžbu sa specifičnim ID-om")
    public ResponseEntity<NarudzbaDTO> getNarudzbaById(
            @Parameter(description = "ID narudžbe") @PathVariable Long id) {
        logger.debug("REST request to get Narudzba by id: {}", id);
        NarudzbaDTO narudzba = narudzbaService.getNarudzbaById(id);
        return ResponseEntity.ok(narudzba);
    }

    @GetMapping("/broj/{brojNarudzbe}")
    @Operation(summary = "Dohvatanje narudžbe po broju", description = "Vraća narudžbu sa specifičnim brojem narudžbe")
    public ResponseEntity<NarudzbaDTO> getNarudzbaByBrojNarudzbe(
            @Parameter(description = "Broj narudžbe") @PathVariable String brojNarudzbe) {
        logger.debug("REST request to get Narudzba by broj: {}", brojNarudzbe);
        NarudzbaDTO narudzba = narudzbaService.getNarudzbaByBrojNarudzbe(brojNarudzbe);
        return ResponseEntity.ok(narudzba);
    }

    @GetMapping("/kupac/{kupacId}")
    @Operation(summary = "Dohvatanje narudžbi po kupcu", description = "Vraća sve narudžbe određenog kupca")
    public ResponseEntity<List<NarudzbaDTO>> getNarudzbeByKupacId(
            @Parameter(description = "ID kupca") @PathVariable Long kupacId) {
        logger.debug("REST request to get Narudzbe by kupac id: {}", kupacId);
        List<NarudzbaDTO> narudzbe = narudzbaService.getNarudzbeByKupacId(kupacId);
        return ResponseEntity.ok(narudzbe);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Dohvatanje narudžbi po statusu", description = "Vraća sve narudžbe sa određenim statusom")
    public ResponseEntity<List<NarudzbaDTO>> getNarudzbeByStatus(
            @Parameter(description = "Status narudžbe") @PathVariable StatusNarudzbe status) {
        logger.debug("REST request to get Narudzbe by status: {}", status);
        List<NarudzbaDTO> narudzbe = narudzbaService.getNarudzbeByStatus(status);
        return ResponseEntity.ok(narudzbe);
    }

    @GetMapping("/active")
    @Operation(summary = "Aktivne narudžbe", description = "Vraća sve aktivne narudžbe (u obradi ili poslate)")
    public ResponseEntity<List<NarudzbaDTO>> getActiveOrders() {
        logger.debug("REST request to get active orders");
        List<NarudzbaDTO> narudzbe = narudzbaService.getActiveOrders();
        return ResponseEntity.ok(narudzbe);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ažuriranje statusa narudžbe", description = "Ažurira status postojeće narudžbe")
    public ResponseEntity<NarudzbaDTO> updateOrderStatus(
            @Parameter(description = "ID narudžbe") @PathVariable Long id,
            @Parameter(description = "Novi status") @RequestParam StatusNarudzbe noviStatus) {
        logger.info("REST request to update status for Narudzba id: {} to {}", id, noviStatus);
        NarudzbaDTO updatedNarudzba = narudzbaService.updateOrderStatus(id, noviStatus);
        return ResponseEntity.ok(updatedNarudzba);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Otkazivanje narudžbe", description = "Otkazuje postojeću narudžbu")
    public ResponseEntity<NarudzbaDTO> cancelOrder(
            @Parameter(description = "ID narudžbe") @PathVariable Long id) {
        logger.info("REST request to cancel Narudzba with id: {}", id);
        NarudzbaDTO cancelledNarudzba = narudzbaService.cancelOrder(id);
        return ResponseEntity.ok(cancelledNarudzba);
    }
}