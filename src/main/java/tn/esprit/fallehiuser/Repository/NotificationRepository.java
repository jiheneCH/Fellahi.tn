package tn.esprit.fallehiuser.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.fallehiuser.model.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
