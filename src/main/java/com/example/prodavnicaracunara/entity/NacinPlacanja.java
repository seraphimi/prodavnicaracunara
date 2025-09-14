package com.example.prodavnicaracunara.entity;

public enum NacinPlacanja {
    KARTICA("Kartica"),
    PAYPAL("PayPal"),
    GOTOVINA("Gotovina");

    private final String opis;

    NacinPlacanja(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}