package com.example.prodavnicaracunara.dto;

import com.example.prodavnicaracunara.entity.NacinPlacanja;
import com.example.prodavnicaracunara.entity.StatusPlacanja;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PlacanjeDTO {
    
    private Long id;
    
    @NotNull(message = "Narudžba je obavezna")
    private Long narudzbaId;
    
    @NotNull(message = "Način plaćanja je obavezan")
    private NacinPlacanja nacinPlacanja;
    
    private StatusPlacanja status;
    private LocalDateTime datum;

    // Constructors
    public PlacanjeDTO() {}

    public PlacanjeDTO(Long narudzbaId, NacinPlacanja nacinPlacanja) {
        this.narudzbaId = narudzbaId;
        this.nacinPlacanja = nacinPlacanja;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNarudzbaId() {
        return narudzbaId;
    }

    public void setNarudzbaId(Long narudzbaId) {
        this.narudzbaId = narudzbaId;
    }

    public NacinPlacanja getNacinPlacanja() {
        return nacinPlacanja;
    }

    public void setNacinPlacanja(NacinPlacanja nacinPlacanja) {
        this.nacinPlacanja = nacinPlacanja;
    }

    public StatusPlacanja getStatus() {
        return status;
    }

    public void setStatus(StatusPlacanja status) {
        this.status = status;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "PlacanjeDTO{" +
                "id=" + id +
                ", narudzbaId=" + narudzbaId +
                ", nacinPlacanja=" + nacinPlacanja +
                ", status=" + status +
                ", datum=" + datum +
                '}';
    }
}