package com.example.prodavnicaracunara.dto;

import com.example.prodavnicaracunara.entity.StatusNarudzbe;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class NarudzbaDTO {
    
    private Long id;
    
    @NotBlank(message = "Broj narudžbe je obavezan")
    private String brojNarudzbe;
    
    @NotNull(message = "Kupac je obavezan")
    private Long kupacId;
    
    private KupacDTO kupac;
    
    @NotNull(message = "Lista proizvoda je obavezna")
    @Size(min = 1, message = "Narudžba mora imati najmanje jedan proizvod")
    private List<Long> proizvodIds;
    
    private List<ProizvodDTO> proizvodi;
    
    @NotNull(message = "Ukupna cena je obavezna")
    @DecimalMin(value = "0.0", inclusive = false, message = "Ukupna cena mora biti veća od 0")
    private BigDecimal ukupnaCena;
    
    private StatusNarudzbe status;
    private LocalDateTime datumKreiranja;
    private PlacanjeDTO placanje;

    // Constructors
    public NarudzbaDTO() {}

    public NarudzbaDTO(String brojNarudzbe, Long kupacId, List<Long> proizvodIds, BigDecimal ukupnaCena) {
        this.brojNarudzbe = brojNarudzbe;
        this.kupacId = kupacId;
        this.proizvodIds = proizvodIds;
        this.ukupnaCena = ukupnaCena;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrojNarudzbe() {
        return brojNarudzbe;
    }

    public void setBrojNarudzbe(String brojNarudzbe) {
        this.brojNarudzbe = brojNarudzbe;
    }

    public Long getKupacId() {
        return kupacId;
    }

    public void setKupacId(Long kupacId) {
        this.kupacId = kupacId;
    }

    public KupacDTO getKupac() {
        return kupac;
    }

    public void setKupac(KupacDTO kupac) {
        this.kupac = kupac;
    }

    public List<Long> getProizvodIds() {
        return proizvodIds;
    }

    public void setProizvodIds(List<Long> proizvodIds) {
        this.proizvodIds = proizvodIds;
    }

    public List<ProizvodDTO> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<ProizvodDTO> proizvodi) {
        this.proizvodi = proizvodi;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public StatusNarudzbe getStatus() {
        return status;
    }

    public void setStatus(StatusNarudzbe status) {
        this.status = status;
    }

    public LocalDateTime getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(LocalDateTime datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public PlacanjeDTO getPlacanje() {
        return placanje;
    }

    public void setPlacanje(PlacanjeDTO placanje) {
        this.placanje = placanje;
    }

    @Override
    public String toString() {
        return "NarudzbaDTO{" +
                "id=" + id +
                ", brojNarudzbe='" + brojNarudzbe + '\'' +
                ", kupacId=" + kupacId +
                ", ukupnaCena=" + ukupnaCena +
                ", status=" + status +
                ", datumKreiranja=" + datumKreiranja +
                '}';
    }
}