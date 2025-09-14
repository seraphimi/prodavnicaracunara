package com.example.prodavnicaracunara.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "proizvodi")
public class Proizvod {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Naziv proizvoda je obavezan")
    @Size(max = 100, message = "Naziv ne može biti duži od 100 karaktera")
    @Column(nullable = false, length = 100)
    private String naziv;
    
    @Column(columnDefinition = "TEXT")
    private String cpu;
    
    @Column(columnDefinition = "TEXT")
    private String ram;
    
    @Column(columnDefinition = "TEXT")
    private String gpu;
    
    @NotNull(message = "Cena je obavezna")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cena mora biti veća od 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cena;
    
    @NotNull(message = "Količina na stanju je obavezna")
    @Min(value = 0, message = "Količina ne može biti negativna")
    @Column(nullable = false)
    private Integer kolicinaUStanju;

    // Constructors
    public Proizvod() {}

    public Proizvod(String naziv, String cpu, String ram, String gpu, BigDecimal cena, Integer kolicinaUStanju) {
        this.naziv = naziv;
        this.cpu = cpu;
        this.ram = ram;
        this.gpu = gpu;
        this.cena = cena;
        this.kolicinaUStanju = kolicinaUStanju;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Integer getKolicinaUStanju() {
        return kolicinaUStanju;
    }

    public void setKolicinaUStanju(Integer kolicinaUStanju) {
        this.kolicinaUStanju = kolicinaUStanju;
    }

    @Override
    public String toString() {
        return "Proizvod{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", gpu='" + gpu + '\'' +
                ", cena=" + cena +
                ", kolicinaUStanju=" + kolicinaUStanju +
                '}';
    }
}