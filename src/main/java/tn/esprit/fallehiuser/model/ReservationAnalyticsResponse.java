package tn.esprit.fallehiuser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class ReservationAnalyticsResponse {
    @Id
    private Long id;
    private Long currentMonthReservations;
    private Long previousMonthReservations;
    private BigDecimal currentMonthEarnings;
    private BigDecimal previousMonthEarnings;


    // Constructeur par défaut
    public ReservationAnalyticsResponse() {
    }

    // Constructeur avec tous les attributs
    public ReservationAnalyticsResponse(Long currentMonthReservations, Long previousMonthReservations,
                                        BigDecimal currentMonthEarnings, BigDecimal previousMonthEarnings) {
        this.currentMonthReservations = currentMonthReservations;
        this.previousMonthReservations = previousMonthReservations;
        this.currentMonthEarnings = currentMonthEarnings;
        this.previousMonthEarnings = previousMonthEarnings;
    }

    // Getters et Setters
    public Long getCurrentMonthReservations() {
        return currentMonthReservations;
    }

    public void setCurrentMonthReservations(Long currentMonthReservations) {
        this.currentMonthReservations = currentMonthReservations;
    }

    public Long getPreviousMonthReservations() {
        return previousMonthReservations;
    }

    public void setPreviousMonthReservations(Long previousMonthReservations) {
        this.previousMonthReservations = previousMonthReservations;
    }

    public BigDecimal getCurrentMonthEarnings() {
        return currentMonthEarnings;
    }

    public void setCurrentMonthEarnings(BigDecimal currentMonthEarnings) {
        this.currentMonthEarnings = currentMonthEarnings;
    }

    public BigDecimal getPreviousMonthEarnings() {
        return previousMonthEarnings;
    }

    public void setPreviousMonthEarnings(BigDecimal previousMonthEarnings) {
        this.previousMonthEarnings = previousMonthEarnings;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

