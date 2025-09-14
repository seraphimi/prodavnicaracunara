package com.example.prodavnicaracunara.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "placanja")
public class Placanje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "narudzba_id", nullable = false)
    @NotNull(message = "Narudžba je obavezna")
    private Narudzba narudzba;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Način plaćanja je obavezan")
    private NacinPlacanja nacinPlacanja;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPlacanja status = StatusPlacanja.NEPLACENO;
    
    @Column(nullable = false)
    private LocalDateTime datum;

    // Constructors
    public Placanje() {
        this.datum = LocalDateTime.now();
    }

    public Placanje(Narudzba narudzba, NacinPlacanja nacinPlacanja) {
        this();
        this.narudzba = narudzba;
        this.nacinPlacanja = nacinPlacanja;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Narudzba getNarudzba() {
        return narudzba;
    }

    public void setNarudzba(Narudzba narudzba) {
        this.narudzba = narudzba;
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
        return "Placanje{" +
                "id=" + id +
                ", nacinPlacanja=" + nacinPlacanja +
                ", status=" + status +
                ", datum=" + datum +
                '}';
    }
}