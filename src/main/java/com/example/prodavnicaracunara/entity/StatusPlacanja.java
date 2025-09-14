package com.example.prodavnicaracunara.entity;

public enum StatusPlacanja {
    NEPLACENO("Neplaćeno"),
    PLACENO("Plaćeno"),
    OTKAZANO("Otkazano");

    private final String opis;

    StatusPlacanja(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}