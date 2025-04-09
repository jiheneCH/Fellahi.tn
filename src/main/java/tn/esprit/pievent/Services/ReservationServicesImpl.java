package tn.esprit.pievent.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.pievent.Entities.*;
import tn.esprit.pievent.Repositories.*;

import java.math.BigDecimal;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationServicesImpl implements IReservationServices {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private IEventServices eventServices;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private WaitingListEntryRepository waitingListEntryRepository;



    @Override
    public Reservation createReservation(long idEvent, long idClient, int numberOfPass) {
        Optional<Event> eventOptional = eventRepository.findById(idEvent);
        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if (eventOptional.isPresent() && clientOptional.isPresent()) {
            Event event = eventOptional.get();
            Client client = clientOptional.get();

            if (event.getNombrePlaces() >= numberOfPass) {
                long remainingPlaces = event.getNombrePlaces() - numberOfPass;

                if (remainingPlaces == 0) {
                    try {
                        eventServices.archiverEvent(event.getIdEvent());
                    } catch (Exception e) {
                        throw new RuntimeException("Erreur lors de l'archivage de l'événement : " + e.getMessage());
                    }
                }

                event.setNombrePlaces(remainingPlaces);
                eventRepository.save(event);
                eventRepository.flush();

                BigDecimal prixUnitaire = event.getPrix();
                BigDecimal reduction = BigDecimal.valueOf(client.getReduction());
                BigDecimal multiplicateur = BigDecimal.ONE.subtract(reduction);
                BigDecimal prixTotal = prixUnitaire
                        .multiply(BigDecimal.valueOf(numberOfPass))
                        .multiply(multiplicateur);

                Reservation reservation = new Reservation();
                reservation.setEvent(event);
                reservation.setClient(client);
                reservation.setNumberOfPass(numberOfPass);
                reservation.setPrixTotal(prixTotal);
                reservation.setReference(UUID.randomUUID().toString());
                reservationRepository.save(reservation);

                //  Notifier la réservation réussie
                //String notificationMessage = " Réservation réussie pour l'événement : " + event.getNomEvent();
                //Notification notifReservation = new Notification(notificationMessage, client);
                //notificationRepository.save(notifReservation);
               // messagingTemplate.convertAndSend("/topic/reservation", notifReservation);

                int totalReservations = reservationRepository.countByClient_IdClient(client.getIdClient());

                if (client.getReduction() > 0.0) {
                    // Réduction utilisée : remise à zéro
                    client.setReduction(0.0);
                    client.setBadge("STANDARD");
                } else if (totalReservations >= 5 && !"VIP".equalsIgnoreCase(client.getBadge())) {
                    //  Le client devient VIP
                    client.setReduction(0.10);
                    client.setBadge("VIP");

                   // String vipMessage = " Félicitations " + client.getNomClient() + ", vous êtes maintenant un client VIP avec 10% de réduction !";
                    //Notification vipNotification = new Notification(vipMessage, client);
                    //notificationRepository.save(vipNotification);
                    //messagingTemplate.convertAndSend("/topic/reservations", vipNotification);
                }

                clientRepository.save(client);
                return reservation;

            } else {
                // Liste d'attente
                WaitingListEntry entry = new WaitingListEntry();
                entry.setClient(client);
                entry.setEvent(event);
                entry.setRequestedPasses(numberOfPass);
                entry.setRequestTime(LocalDateTime.now());

                WaitingListEntry savedEntry = waitingListEntryRepository.save(entry);
                waitingListEntryRepository.flush();

                System.out.println("Entrée dans la liste d'attente ajoutée avec ID : " + savedEntry.getId());

                throw new IllegalArgumentException("Pas assez de places disponibles. Vous avez été ajouté à la liste d'attente.");
            }
        } else {
            throw new IllegalArgumentException("Event ou Client non trouvé");
        }
    }




    @Override
    public void cancelReservation(long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        Event event = reservation.getEvent();
        int placesToRestore = reservation.getNumberOfPass();  // Nombre de places à restaurer

        // Supprimer la réservation
        reservationRepository.delete(reservation);

        // Restaurer les places
        event.setNombrePlaces(event.getNombrePlaces() + placesToRestore); // Incrémenter les places
        event.setArchived(false); // Si l'événement était archivé
        eventRepository.save(event);  // Sauvegarder l'événement après modification

        // Gérer la liste d’attente
        handleWaitingList(event);
    }

    //gérer la transition entre la liste d'attente et la confirmation de réservation
    @Override
    public void handleWaitingList(Event event) {
        // Récupérer les entrées de la liste d'attente triées par la demande la plus ancienne
        List<WaitingListEntry> waitingList = waitingListEntryRepository.findByEventOrderByRequestTimeAsc(event);

        // Traiter chaque entrée dans la liste d'attente
        for (WaitingListEntry entry : waitingList) {
            int requestedPass = entry.getRequestedPasses();

            // Vérifier si l'événement a suffisamment de places pour cette entrée
            if (event.getNombrePlaces() >= requestedPass) {
                // Créer la réservation automatiquement
                Reservation reservation = new Reservation();
                reservation.setClient(entry.getClient());
                reservation.setEvent(event);
                reservation.setNumberOfPass(requestedPass);
                reservationRepository.save(reservation);

                // Décrémenter les places disponibles après création de la réservation
                event.setNombrePlaces(event.getNombrePlaces() - requestedPass);
                eventRepository.save(event);  // Sauvegarder l'événement après modification

                // Supprimer l’entrée de la liste d’attente
                waitingListEntryRepository.delete(entry);

                // Si l'événement n'a plus de places, on l'archive
                if (event.getNombrePlaces() == 0) {
                    event.setArchived(true);  // Archiver l'événement
                    event.setStatus(Status.archive);  // Mettre le statut en "archivé"
                    eventRepository.save(event);  // Sauvegarder l'événement après modification
                    break; // Arrêter le traitement si l'événement est archivé
                }
            }
        }
    }

    private String generateReference() {
        return UUID.randomUUID().toString().substring(0, 19).replaceAll("-", "").replaceAll("(.{4})(?=.)", "$1-");
    }

//Analyse des Earnings
@Override
public ReservationAnalyticsResponse calculateAnalytics() {
    // Récupérer la date actuelle
    LocalDate now = LocalDate.now();
    int currentMonth = now.getMonthValue();
    int currentYear = now.getYear();

    // Calculer le mois précédent
    LocalDate previousMonthDate = now.minusMonths(1);
    int previousMonth = previousMonthDate.getMonthValue();
    int previousYear = previousMonthDate.getYear();

    // Définir la plage de dates pour le mois courant (LocalDateTime)
    LocalDate currentMonthStart = LocalDate.of(currentYear, currentMonth, 1);
    LocalDate currentMonthEnd = currentMonthStart.withDayOfMonth(currentMonthStart.lengthOfMonth());
    LocalDateTime currentMonthStartDateTime = currentMonthStart.atStartOfDay();
    LocalDateTime currentMonthEndDateTime = currentMonthEnd.atTime(23, 59, 59);

    // Définir la plage de dates pour le mois précédent
    LocalDate previousMonthStart = LocalDate.of(previousYear, previousMonth, 1);
    LocalDate previousMonthEnd = previousMonthStart.withDayOfMonth(previousMonthStart.lengthOfMonth());
    LocalDateTime previousMonthStartDateTime = previousMonthStart.atStartOfDay();
    LocalDateTime previousMonthEndDateTime = previousMonthEnd.atTime(23, 59, 59);

    // Récupérer les réservations pour chaque période via le repository
    List<Reservation> currentReservations = reservationRepository
            .findByReservationDateBetween(currentMonthStartDateTime, currentMonthEndDateTime);
    List<Reservation> previousReservations = reservationRepository
            .findByReservationDateBetween(previousMonthStartDateTime, previousMonthEndDateTime);

    // Calculer le nombre de réservations pour chaque période
    Long currentCount = (long) currentReservations.size();
    Long previousCount = (long) previousReservations.size();

    // Calculer le total des revenus pour chaque période
    // Option 1 : Par calcul direct avec un stream converti en Double
    Double currentEarnings = currentReservations.stream()
            .mapToDouble(r -> r.getPrixTotal() != null ? r.getPrixTotal().doubleValue() : 0)
            .sum();
    Double previousEarnings = previousReservations.stream()
            .mapToDouble(r -> r.getPrixTotal() != null ? r.getPrixTotal().doubleValue() : 0)
            .sum();

    // Option 2 : Utiliser la méthode séparée pour récupérer le total en BigDecimal
    // BigDecimal currentEarningsBD = getTotalEarningsForMonth(currentMonth, currentYear);
    // BigDecimal previousEarningsBD = getTotalEarningsForMonth(previousMonth, previousYear);
    // Vous pouvez convertir en double si nécessaire : currentEarningsBD.doubleValue()

    // Créer et retourner la réponse via le DTO ReservationAnalyticsResponse
    ReservationAnalyticsResponse response = new ReservationAnalyticsResponse();
    response.setCurrentMonthReservations(currentCount);
    response.setCurrentMonthEarnings(BigDecimal.valueOf(currentEarnings));
    response.setPreviousMonthReservations(previousCount);
    response.setPreviousMonthEarnings(BigDecimal.valueOf(previousEarnings));

    return response;
}





}



