package org.beta.db;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    private static final String CONN_STRING = "jdbc:mysql://localhost/testdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Brussels";
    private static final String DELETE = "DELETE FROM persoon WHERE id= ?";
    //De DELETE functie moet niet uitgechreven worden, dit is een SQL functie die de databank zelf moet uitvoeren
    // Binnen Java is dit dus enkel een commando (een String!) en moet ook zo behandeld worden.

    public static void main(String[] args) throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Volgende personen staan in de tabel: ");
        try (
                Connection conn = DriverManager.getConnection(CONN_STRING, "root", "VDAB");
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM persoon")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String voornaam = rs.getString("voornaam");
                String achternaam = rs.getString("achternaam");
                System.out.printf("%d. %s %s%n", id, voornaam, achternaam);
            }
        }
        System.out.println("Welke persoon wil je verwijderen? Geef de ID: ");
        int keuze = Integer.parseInt(scanner.nextLine());
        try (
            Connection conn = DriverManager.getConnection(CONN_STRING, "root", "VDAB");
            PreparedStatement stm = conn.prepareStatement(DELETE)){
            stm.setInt(1, keuze);
            // Na deze lijn code is de persoon al VERWIJDERD! MAAR hij geeft nog geen bericht dat het verwijderd is.
            int resultaat = stm.executeUpdate();
            // Resultaat is hier de hoeveel records hij aangepast heeft. Je vraagt om een 1 persoon met 1 uniek ID te
            // verwijderen, dus in dit geval zat het aantal veranderde records 1 zijn!
                if(resultaat > 0) {
                        System.out.println("Persoon is verwijderd");
                    } else{
                        System.out.println("ID bestaat niet, verwijdering is niet uitgevoerd.");
                }
            }
        }
    }

