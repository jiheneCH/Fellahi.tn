package tn.esprit.pievent.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime createdAt;

    //@ManyToOne
    //@JoinColumn(name = "client_id")
   // private Client client;

    public Notification() {}

    //public Notification(String message, Client client) {
       // this.message = message;
        //this.client = client;
       // this.createdAt = LocalDateTime.now();
    //}

    //public Long getId() {
     //   return id;
    //}

   // public String getMessage() {
       // return message;
   // }

    //public void setMessage(String message) {
     //   this.message = message;
    //}

   // public LocalDateTime getCreatedAt() {
      //  return createdAt;
    //}

   // public Client getClient() {
       // return client;
   // }

   // public void setClient(Client client) {
      //  this.client = client;
   // }
}
