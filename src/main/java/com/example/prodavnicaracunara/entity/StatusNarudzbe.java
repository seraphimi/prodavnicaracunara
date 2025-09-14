package com.example.prodavnicaracunara.entity;

public enum StatusNarudzbe {
    U_OBRADI("U obradi"),
    POSLATA("Poslata"),
    ISPORUCENA("Isporučena"),
    OTKAZANA("Otkazana");

    private final String opis;

    StatusNarudzbe(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}