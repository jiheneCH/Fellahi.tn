package tn.esprit.fallehiuser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;


import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;

@Setter
@Getter
@Entity


@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    public Event() {
        // Initialisez les valeurs par défaut si nécessaire
        this.recurrence = Recurrence.NONE;  // Par exemple
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long  idEvent;

    @Pattern(regexp = "^[^\\s].*[^\\s]$", message = "Le titre ne doit pas commencer ni finir par un espace")

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
    String title;

   // @NotNull(message = "La date est obligatoire")
    //@FutureOrPresent(message = "La date doit être dans le futur ou aujourd'hui")
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
   // Date dateEvent;

    @NotNull(message = "La date est obligatoire")
    @FutureOrPresent(message = "La date doit être dans le futur ou aujourd'hui")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateEvent;

    @NotNull(message = "L'heure est obligatoire")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime heureEvent;

    @Pattern(regexp = "^[a-zA-Z0-9,\\s]+$", message = "L'emplacement doit être alphanumérique")
    @NotBlank(message = "L'emplacement est obligatoire")
    @Size(min = 3, max = 255, message = "L'emplacement doit contenir entre 3 et 255 caractères")
    private String location;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 3, max = 100, message = "La description doit contenir entre 3 et 100 caractères")
   private  String description;

    @NotNull(message = "Le nombre de places est obligatoire")
    @Min(value = 0)
    @Max(value = 10000, message = "Le nombre de places ne peut pas dépasser 10 000")
    Long nombrePlaces;

    @NotNull(message = "Le prix est obligatoire.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0.")
    @Digits(integer = 6, fraction = 2, message = "Le prix doit être un nombre valide avec 2 décimales maximum.")
    private BigDecimal prix;

    @Column(name = "image")
    @NotNull(message = "L'image est obligatoire")
    private String image;

    @Column(name = "pdf")

    private String pdf;

    private boolean archived = false;

    @NotNull(message = "Le type d'événement est obligatoire")
    @Enumerated(EnumType.STRING)
    TypeEvent typeEvent;

    @NotNull(message = "Le statut de l'événement est obligatoire")
    @Enumerated(EnumType.STRING)
    StatusEvent statusEvent;



    @Column(name = "reference_e")
    String reference;

    @Lob
    private byte[] qrCodeImage;


    @Enumerated(EnumType.STRING)
    private Recurrence recurrence = Recurrence.NONE;

    private LocalDateTime recurrenceEndDate;


    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "event_recurrence_days", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "day_of_week")
    private List<DayOfWeek> recurrenceDaysOfWeek;

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        if (recurrence == Recurrence.NONE) {
            this.recurrenceEndDate = null;
            this.recurrenceDaysOfWeek = new ArrayList<>();
        }
        this.recurrence = recurrence;
    }


    public LocalDateTime getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(LocalDateTime recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    public List<DayOfWeek> getRecurrenceDaysOfWeek() {
        return recurrenceDaysOfWeek;
    }

    // Getters and Setters
    public void setRecurrenceDaysOfWeek(List<DayOfWeek> days) {
        this.recurrenceDaysOfWeek = days;
    }


    public byte[] getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();


    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private List<WaitingListEntry> waitingListEntries;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();


    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<WaitingListEntry> getWaitingListEntries() {
        return waitingListEntries;
    }

    public void setWaitingListEntries(List<WaitingListEntry> waitingListEntries) {
        this.waitingListEntries = waitingListEntries;

    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(long idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(LocalDate dateEvent) {
        this.dateEvent = dateEvent;
    }

    public LocalTime getHeureEvent() {
        return heureEvent;
    }

    public void setHeureEvent(LocalTime heureEvent) {
        this.heureEvent = heureEvent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(Long nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
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

    public StatusEvent getStatusEvent() {
        return statusEvent;
    }

    public void setStatusEvent(StatusEvent statusEvent) {
        this.statusEvent = statusEvent;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


}





