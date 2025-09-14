package com.example.prodavnicaracunara.config;

import com.example.prodavnicaracunara.entity.*;
import com.example.prodavnicaracunara.repository.KupacRepository;
import com.example.prodavnicaracunara.repository.ProizvodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private ProizvodRepository proizvodRepository;

    @Autowired
    private KupacRepository kupacRepository;

    @Override
    public void run(String... args) {
        logger.info("Starting data initialization...");

        // Initialize sample products if none exist
        if (proizvodRepository.count() == 0) {
            initializeProducts();
        }

        // Initialize sample customers if none exist
        if (kupacRepository.count() == 0) {
            initializeCustomers();
        }

        logger.info("Data initialization completed");
    }

    private void initializeProducts() {
        logger.info("Initializing sample products...");

        // Laptops
        Proizvod laptop1 = new Proizvod("Dell XPS 13", "Intel i7-1185G7", "16GB DDR4", "Intel Iris Xe", 
                new BigDecimal("149990.00"), 10);
        Proizvod laptop2 = new Proizvod("MacBook Pro 14\"", "Apple M2 Pro", "16GB Unified Memory", "Apple M2 Pro GPU", 
                new BigDecimal("289990.00"), 5);
        Proizvod laptop3 = new Proizvod("ASUS ROG Strix G15", "AMD Ryzen 7 5800H", "16GB DDR4", "NVIDIA RTX 3070", 
                new BigDecimal("199990.00"), 8);

        // Desktop computers
        Proizvod desktop1 = new Proizvod("Gaming PC Ultimate", "Intel i9-12900K", "32GB DDR4", "NVIDIA RTX 4080", 
                new BigDecimal("449990.00"), 3);
        Proizvod desktop2 = new Proizvod("Office PC Standard", "Intel i5-12400", "16GB DDR4", "Intel UHD Graphics", 
                new BigDecimal("89990.00"), 15);

        // Components
        Proizvod gpu1 = new Proizvod("NVIDIA RTX 4090", "", "", "NVIDIA GeForce RTX 4090", 
                new BigDecimal("189990.00"), 6);
        Proizvod cpu1 = new Proizvod("AMD Ryzen 9 7950X", "AMD Ryzen 9 7950X", "", "", 
                new BigDecimal("89990.00"), 12);
        Proizvod ram1 = new Proizvod("Corsair Vengeance DDR5", "", "32GB DDR5-5600", "", 
                new BigDecimal("35990.00"), 20);

        // Accessories
        Proizvod monitor1 = new Proizvod("Dell UltraSharp 27\" 4K", "", "", "", 
                new BigDecimal("45990.00"), 7);
        Proizvod keyboard1 = new Proizvod("Logitech MX Keys", "", "", "", 
                new BigDecimal("12990.00"), 25);

        proizvodRepository.save(laptop1);
        proizvodRepository.save(laptop2);
        proizvodRepository.save(laptop3);
        proizvodRepository.save(desktop1);
        proizvodRepository.save(desktop2);
        proizvodRepository.save(gpu1);
        proizvodRepository.save(cpu1);
        proizvodRepository.save(ram1);
        proizvodRepository.save(monitor1);
        proizvodRepository.save(keyboard1);

        logger.info("Sample products initialized: {} products created", 10);
    }

    private void initializeCustomers() {
        logger.info("Initializing sample customers...");

        Kupac kupac1 = new Kupac("Marko", "Petrović", "marko.petrovic@email.com", "+381611234567", 
                "Knez Mihailova 1, Beograd");
        Kupac kupac2 = new Kupac("Ana", "Jovanović", "ana.jovanovic@email.com", "+381627890123", 
                "Bulevar Nemanjića 2, Novi Sad");
        Kupac kupac3 = new Kupac("Stefan", "Nikolić", "stefan.nikolic@email.com", "+381634567890", 
                "Kralja Petra 15, Niš");
        Kupac kupac4 = new Kupac("Milica", "Stojanović", "milica.stojanovic@email.com", "+381645678901", 
                "Svetosavska 10, Kragujevac");
        Kupac kupac5 = new Kupac("Nikola", "Milosević", "nikola.milosevic@email.com", "+381656789012", 
                "Cara Dušana 5, Subotica");

        kupacRepository.save(kupac1);
        kupacRepository.save(kupac2);
        kupacRepository.save(kupac3);
        kupacRepository.save(kupac4);
        kupacRepository.save(kupac5);

        logger.info("Sample customers initialized: {} customers created", 5);
    }
}