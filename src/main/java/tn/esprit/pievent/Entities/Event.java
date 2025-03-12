package tn.esprit.pievent.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idEvent;

    @Pattern(regexp = "^[^\\s].*[^\\s]$", message = "Le titre ne doit pas commencer ni finir par un espace")

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
    String title;

    @NotNull(message = "La date est obligatoire")
    @FutureOrPresent(message = "La date doit être dans le futur ou aujourd'hui")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date dateEvent;

    @Pattern(regexp = "^[a-zA-Z0-9,\\s]+$", message = "L'emplacement doit être alphanumérique")
    @NotBlank(message = "L'emplacement est obligatoire")
    @Size(min = 3, max = 255, message = "L'emplacement doit contenir entre 3 et 255 caractères")
    String location;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 1, message = "Le nombre de places doit être au moins 1")
    @Max(value = 10000, message = "Le nombre de places ne peut pas dépasser 10 000")
    Long nomPlaces; // Changer en Integer pour éviter les erreurs

    private boolean archived = false;

    @NotNull(message = "Le type d'événement est obligatoire")
    @Enumerated(EnumType.STRING)
    TypeEvent typeEvent;

    @NotNull(message = "Le statut de l'événement est obligatoire")
    @Enumerated(EnumType.STRING)
    Status status;

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getNomPlaces() {
        return nomPlaces;
    }

    public void setNomPlaces(Long nomPlaces) {
        this.nomPlaces = nomPlaces;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(TypeEvent typeEvent) {
        this.typeEvent = typeEvent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}





