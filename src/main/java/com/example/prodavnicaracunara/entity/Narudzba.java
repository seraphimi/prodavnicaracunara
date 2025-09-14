package com.example.prodavnicaracunara.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "narudzbe")
public class Narudzba {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Broj narudžbe je obavezan")
    @Column(nullable = false, unique = true, length = 50)
    private String brojNarudzbe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kupac_id", nullable = false)
    @NotNull(message = "Kupac je obavezan")
    @JsonBackReference("kupac-narudzbe")
    private Kupac kupac;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "narudzba_proizvod",
        joinColumns = @JoinColumn(name = "narudzba_id"),
        inverseJoinColumns = @JoinColumn(name = "proizvod_id")
    )
    private List<Proizvod> proizvodi;
    
    @NotNull(message = "Ukupna cena je obavezna")
    @DecimalMin(value = "0.0", inclusive = false, message = "Ukupna cena mora biti veća od 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ukupnaCena;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusNarudzbe status = StatusNarudzbe.U_OBRADI;
    
    @Column(nullable = false)
    private LocalDateTime datumKreiranja;
    
    @OneToOne(mappedBy = "narudzba", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("narudzba-placanje")
    private Placanje placanje;

    // Constructors
    public Narudzba() {
        this.datumKreiranja = LocalDateTime.now();
    }

    public Narudzba(String brojNarudzbe, Kupac kupac, List<Proizvod> proizvodi, BigDecimal ukupnaCena) {
        this();
        this.brojNarudzbe = brojNarudzbe;
        this.kupac = kupac;
        this.proizvodi = proizvodi;
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

    public Kupac getKupac() {
        return kupac;
    }

    public void setKupac(Kupac kupac) {
        this.kupac = kupac;
    }

    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
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

    public Placanje getPlacanje() {
        return placanje;
    }

    public void setPlacanje(Placanje placanje) {
        this.placanje = placanje;
    }

    @Override
    public String toString() {
        return "Narudzba{" +
                "id=" + id +
                ", brojNarudzbe='" + brojNarudzbe + '\'' +
                ", ukupnaCena=" + ukupnaCena +
                ", status=" + status +
                ", datumKreiranja=" + datumKreiranja +
                '}';
    }
}