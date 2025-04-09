package tn.esprit.pievent.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pievent.Entities.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
