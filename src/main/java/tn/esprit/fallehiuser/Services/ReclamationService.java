package tn.esprit.fallehiuser.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.Repository.ReclamationRepository;
import tn.esprit.fallehiuser.Repository.UserRepository;
import tn.esprit.fallehiuser.model.Reclamation;
import tn.esprit.fallehiuser.model.ReclamationStatus;
import tn.esprit.fallehiuser.model.ReclamationSubject;
import tn.esprit.fallehiuser.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;

    // Add a new reclamation (based on role, user can choose predefined subject)
    public void addReclamation(String username, String description, ReclamationSubject subject) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Validate the complaint subject based on the user's role
        validateComplaintSubject(user, subject);

        Reclamation reclamation = new Reclamation();
        reclamation.setDescription(description);
        reclamation.setCreatedAt(LocalDateTime.now());
        reclamation.setUser(user);
        reclamation.setSubject(subject.name());
        reclamation.setStatus(ReclamationStatus.PENDING);  // Set initial status to "PENDING"

        reclamationRepository.save(reclamation);
    }

    private void validateComplaintSubject(User user, ReclamationSubject subject) {
        if ("FARMER".equals(user.getRoleName()) && !isFarmerComplaint(subject)) {
            throw new IllegalArgumentException("Invalid complaint subject for farmer");
        }
        if ("TRANSPORTER".equals(user.getRoleName()) && !isTransporterComplaint(subject)) {
            throw new IllegalArgumentException("Invalid complaint subject for transporter");
        }
        if ("CLIENT".equals(user.getRoleName()) && !isClientComplaint(subject)) {
            throw new IllegalArgumentException("Invalid complaint subject for client");
        }
    }

    private boolean isFarmerComplaint(ReclamationSubject subject) {
        return subject == ReclamationSubject.DELIVERY_DELAY_BY_TRANSPORTER ||
                subject == ReclamationSubject.ORDER_CANCELLATION_BY_CLIENT ||
                subject == ReclamationSubject.INCORRECT_PRICE_OR_QUANTITY ||
                subject == ReclamationSubject.UNAVAILABILITY_OF_GOODS;
    }

    private boolean isTransporterComplaint(ReclamationSubject subject) {
        return subject == ReclamationSubject.DELIVERY_DELAY_BY_CLIENT ||
                subject == ReclamationSubject.CLIENT_REFUSES_TO_ACCEPT_GOODS ||
                subject == ReclamationSubject.WRONG_PRODUCT_INFORMATION ||
                subject == ReclamationSubject.UNAVAILABILITY_OF_CLIENT;
    }

    private boolean isClientComplaint(ReclamationSubject subject) {
        return subject == ReclamationSubject.POOR_QUALITY_OF_GOODS ||
                subject == ReclamationSubject.DELAYED_DELIVERY ||
                subject == ReclamationSubject.WRONG_PRODUCT_DELIVERED ||
                subject == ReclamationSubject.GOODS_NOT_AS_DESCRIBED ||
                subject == ReclamationSubject.PRICE_DISCREPANCY;
    }

    // Get all pending reclamations (for admin)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getAllPendingReclamations() {
        return reclamationRepository.findByStatus(ReclamationStatus.PENDING);
    }

    // Admin can update the status of a reclamation
    @PreAuthorize("hasRole('ADMIN')")
    public void treatReclamation(Long reclamationId, ReclamationStatus status) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new IllegalStateException("Reclamation not found"));

        // If the complaint is PENDING, it can be changed to IN_PROGRESS
        if (reclamation.getStatus() == ReclamationStatus.PENDING && status == ReclamationStatus.IN_PROGRESS) {
            reclamation.setStatus(ReclamationStatus.IN_PROGRESS);
        }

        // If the complaint is IN_PROGRESS, it can be resolved (status becomes RESOLVED)
        else if (reclamation.getStatus() == ReclamationStatus.IN_PROGRESS && status == ReclamationStatus.RESOLVED) {
            reclamation.setStatus(ReclamationStatus.RESOLVED);
        } else {
            // Handle invalid status transitions
            throw new IllegalArgumentException("Invalid status transition.");
        }

        reclamationRepository.save(reclamation);
    }

    // Get reclamations for a specific user
    public List<Reclamation> getReclamationsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return reclamationRepository.findByUser(user);
    }

    // Find reclamation by ID
    public Reclamation findReclamationById(Long id) {
        return reclamationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Reclamation not found"));
    }

    // Update the reclamation status
    public void updateReclamationStatus(Long reclamationId, ReclamationStatus status) {
        Reclamation reclamation = findReclamationById(reclamationId);
        reclamation.setStatus(status);
        reclamationRepository.save(reclamation);
    }

    // Get reclamations by subject (for filtering)
    public List<Reclamation> getReclamationsBySubject(String subject) {
        return reclamationRepository.findBySubject(subject);
    }

    // New method to filter reclamations by date
    public List<Reclamation> getReclamationsByDate(boolean latest) {
        if (latest) {
            // Get the latest complaints (order by date descending)
            return reclamationRepository.findAllByOrderByCreatedAtDesc();
        } else {
            // Get the oldest complaints (order by date ascending)
            return reclamationRepository.findAllByOrderByCreatedAtAsc();
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getReclamationsByStatus(ReclamationStatus status) {
        return reclamationRepository.findByStatus(status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }


}
