package com.example.prodavnicaracunara.dto;

import jakarta.validation.constraints.*;

public class KupacDTO {
    
    private Long id;
    
    @NotBlank(message = "Ime je obavezno")
    @Size(max = 50, message = "Ime ne može biti duže od 50 karaktera")
    private String ime;
    
    @NotBlank(message = "Prezime je obavezno")
    @Size(max = 50, message = "Prezime ne može biti duže od 50 karaktera")
    private String prezime;
    
    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email mora biti valjan")
    private String email;
    
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Telefon mora biti valjan")
    private String telefon;
    
    @Size(max = 200, message = "Adresa ne može biti duža od 200 karaktera")
    private String adresa;

    // Constructors
    public KupacDTO() {}

    public KupacDTO(String ime, String prezime, String email, String telefon, String adresa) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.telefon = telefon;
        this.adresa = adresa;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "KupacDTO{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", adresa='" + adresa + '\'' +
                '}';
    }
}