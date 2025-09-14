package com.example.prodavnicaracunara.controller;

import com.example.prodavnicaracunara.dto.PlacanjeDTO;
import com.example.prodavnicaracunara.entity.NacinPlacanja;
import com.example.prodavnicaracunara.entity.StatusPlacanja;
import com.example.prodavnicaracunara.service.PlacanjeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Plaćanja", description = "API za upravljanje plaćanjima")
public class PlacanjeController {

    private static final Logger logger = LoggerFactory.getLogger(PlacanjeController.class);

    @Autowired
    private PlacanjeService placanjeService;

    @PostMapping
    @Operation(summary = "Kreiranje novog plaćanja", description = "Kreira novo plaćanje za narudžbu")
    public ResponseEntity<PlacanjeDTO> createPlacanje(@Valid @RequestBody PlacanjeDTO placanjeDTO) {
        logger.info("REST request to create Placanje for order ID: {}", placanjeDTO.getNarudzbaId());
        PlacanjeDTO createdPlacanje = placanjeService.createPlacanje(placanjeDTO);
        return new ResponseEntity<>(createdPlacanje, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Dohvatanje svih plaćanja", description = "Vraća listu svih plaćanja")
    public ResponseEntity<List<PlacanjeDTO>> getAllPlacanja() {
        logger.debug("REST request to get all Placanja");
        List<PlacanjeDTO> placanja = placanjeService.getAllPlacanja();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dohvatanje plaćanja po ID-u", description = "Vraća plaćanje sa specifičnim ID-om")
    public ResponseEntity<PlacanjeDTO> getPlacanjeById(
            @Parameter(description = "ID plaćanja") @PathVariable Long id) {
        logger.debug("REST request to get Placanje by id: {}", id);
        PlacanjeDTO placanje = placanjeService.getPlacanjeById(id);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/narudzba/{narudzbaId}")
    @Operation(summary = "Dohvatanje plaćanja po narudžbi", description = "Vraća plaćanje za specifičnu narudžbu")
    public ResponseEntity<PlacanjeDTO> getPlacanjeByNarudzbaId(
            @Parameter(description = "ID narudžbe") @PathVariable Long narudzbaId) {
        logger.debug("REST request to get Placanje by narudzba id: {}", narudzbaId);
        PlacanjeDTO placanje = placanjeService.getPlacanjeByNarudzbaId(narudzbaId);
        return ResponseEntity.ok(placanje);
    }

    @GetMapping("/nacin-placanja/{nacinPlacanja}")
    @Operation(summary = "Dohvatanje plaćanja po načinu plaćanja", description = "Vraća sva plaćanja određenog načina plaćanja")
    public ResponseEntity<List<PlacanjeDTO>> getPlacanjaByNacinPlacanja(
            @Parameter(description = "Način plaćanja") @PathVariable NacinPlacanja nacinPlacanja) {
        logger.debug("REST request to get Placanja by nacin placanja: {}", nacinPlacanja);
        List<PlacanjeDTO> placanja = placanjeService.getPlacanjaByNacinPlacanja(nacinPlacanja);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Dohvatanje plaćanja po statusu", description = "Vraća sva plaćanja sa određenim statusom")
    public ResponseEntity<List<PlacanjeDTO>> getPlacanjaByStatus(
            @Parameter(description = "Status plaćanja") @PathVariable StatusPlacanja status) {
        logger.debug("REST request to get Placanja by status: {}", status);
        List<PlacanjeDTO> placanja = placanjeService.getPlacanjaByStatus(status);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/unpaid")
    @Operation(summary = "Neplaćena plaćanja", description = "Vraća sva neplaćena plaćanja")
    public ResponseEntity<List<PlacanjeDTO>> getUnpaidPayments() {
        logger.debug("REST request to get unpaid payments");
        List<PlacanjeDTO> placanja = placanjeService.getUnpaidPayments();
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/kupac/{kupacId}")
    @Operation(summary = "Plaćanja po kupcu", description = "Vraća sva plaćanja određenog kupca")
    public ResponseEntity<List<PlacanjeDTO>> getPlacanjaByKupacId(
            @Parameter(description = "ID kupca") @PathVariable Long kupacId) {
        logger.debug("REST request to get Placanja by kupac id: {}", kupacId);
        List<PlacanjeDTO> placanja = placanjeService.getPlacanjaByKupacId(kupacId);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/successful")
    @Operation(summary = "Uspešna plaćanja u periodu", description = "Vraća sva uspešna plaćanja u određenom vremenskom periodu")
    public ResponseEntity<List<PlacanjeDTO>> getSuccessfulPayments(
            @Parameter(description = "Početni datum") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Krajnji datum") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        logger.debug("REST request to get successful payments between {} and {}", startDate, endDate);
        List<PlacanjeDTO> placanja = placanjeService.getSuccessfulPayments(startDate, endDate);
        return ResponseEntity.ok(placanja);
    }

    @GetMapping("/revenue")
    @Operation(summary = "Ukupan prihod", description = "Vraća ukupan prihod u određenom vremenskom periodu")
    public ResponseEntity<Double> getTotalRevenue(
            @Parameter(description = "Početni datum") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Krajnji datum") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        logger.debug("REST request to get total revenue between {} and {}", startDate, endDate);
        Double totalRevenue = placanjeService.getTotalRevenue(startDate, endDate);
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/statistics/method")
    @Operation(summary = "Statistike po načinu plaćanja", description = "Vraća statistike plaćanja grupisane po načinu plaćanja")
    public ResponseEntity<List<Object[]>> getPaymentStatisticsByMethod() {
        logger.debug("REST request to get payment statistics by method");
        List<Object[]> statistics = placanjeService.getPaymentStatisticsByMethod();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/status")
    @Operation(summary = "Statistike po statusu", description = "Vraća statistike plaćanja grupisane po statusu")
    public ResponseEntity<List<Object[]>> getPaymentStatisticsByStatus() {
        logger.debug("REST request to get payment statistics by status");
        List<Object[]> statistics = placanjeService.getPaymentStatisticsByStatus();
        return ResponseEntity.ok(statistics);
    }

    @PatchMapping("/{id}/process")
    @Operation(summary = "Obradi plaćanje", description = "Označava plaćanje kao obrađeno/plaćeno")
    public ResponseEntity<PlacanjeDTO> processPayment(
            @Parameter(description = "ID plaćanja") @PathVariable Long id) {
        logger.info("REST request to process Placanje with id: {}", id);
        PlacanjeDTO processedPlacanje = placanjeService.processPayment(id);
        return ResponseEntity.ok(processedPlacanje);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Otkaži plaćanje", description = "Otkazuje plaćanje")
    public ResponseEntity<PlacanjeDTO> cancelPayment(
            @Parameter(description = "ID plaćanja") @PathVariable Long id) {
        logger.info("REST request to cancel Placanje with id: {}", id);
        PlacanjeDTO cancelledPlacanje = placanjeService.cancelPayment(id);
        return ResponseEntity.ok(cancelledPlacanje);
    }

    @PatchMapping("/{id}/payment-method")
    @Operation(summary = "Ažuriraj način plaćanja", description = "Ažurira način plaćanja za postojeće plaćanje")
    public ResponseEntity<PlacanjeDTO> updatePaymentMethod(
            @Parameter(description = "ID plaćanja") @PathVariable Long id,
            @Parameter(description = "Novi način plaćanja") @RequestParam NacinPlacanja noviNacinPlacanja) {
        logger.info("REST request to update payment method for Placanje id: {} to {}", id, noviNacinPlacanja);
        PlacanjeDTO updatedPlacanje = placanjeService.updatePaymentMethod(id, noviNacinPlacanja);
        return ResponseEntity.ok(updatedPlacanje);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje plaćanja", description = "Briše plaćanje (samo ako nije obrađeno)")
    public ResponseEntity<Void> deletePlacanje(
            @Parameter(description = "ID plaćanja") @PathVariable Long id) {
        logger.info("REST request to delete Placanje with id: {}", id);
        placanjeService.deletePlacanje(id);
        return ResponseEntity.noContent().build();
    }
}