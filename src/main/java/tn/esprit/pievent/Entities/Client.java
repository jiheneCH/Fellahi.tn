package tn.esprit.pievent.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity


@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idClient;
    String nomClient;


    private String badge;


    private double reduction;

    public Client() {
        this.badge = "STANDARD";
        this.reduction = 0.0;
    }




    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();


    // Liste des entrées dans la liste d'attente liées au client
    @OneToMany(mappedBy = "client")
    private List<WaitingListEntry> waitingListEntries;

    public List<WaitingListEntry> getWaitingListEntries() {
        return waitingListEntries;
    }

    public void setWaitingListEntries(List<WaitingListEntry> waitingListEntries) {
        this.waitingListEntries = waitingListEntries;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;


    }

}
