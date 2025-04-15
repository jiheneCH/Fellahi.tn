package tn.esprit.fallehiuser.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.DTO.ReclamationResponseDTO;
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

    // === Client / User Operations ===

    /**
     * User adds a new reclamation based on their allowed subjects by role.
     */
    public void addReclamation(String username, String description, ReclamationSubject subject) {
        User user = getUserByUsername(username);
        validateComplaintSubject(user, subject);

        Reclamation reclamation = new Reclamation();
        reclamation.setDescription(description);
        reclamation.setCreatedAt(LocalDateTime.now());
        reclamation.setUser(user);
        reclamation.setSubject(subject.name());
        reclamation.setStatus(ReclamationStatus.PENDING);

        reclamationRepository.save(reclamation);
    }

    /**
     * Get all reclamations submitted by a specific user.
     */
    public List<Reclamation> getReclamationsByUser(String username) {
        User user = getUserByUsername(username);
        return reclamationRepository.findByUser(user);
    }

    // === Admin Operations ===

    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getAllPendingReclamations() {
        return reclamationRepository.findByStatus(ReclamationStatus.PENDING);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getReclamationsByStatus(ReclamationStatus status) {
        return reclamationRepository.findByStatus(status);
    }

    /**
     * Admin treats a reclamation by updating its status from PENDING to IN_PROGRESS or
     * from IN_PROGRESS to RESOLVED.
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void treatReclamation(Long reclamationId, ReclamationStatus status) {
        Reclamation reclamation = findReclamationById(reclamationId);
        validateStatusTransition(reclamation.getStatus(), status);

        reclamation.setStatus(status);
        reclamationRepository.save(reclamation);
    }

    /**
     * Admin responds to a reclamation and marks it as resolved.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void respondToReclamation(Long reclamationId, String adminUsername, String responseMessage, ReclamationStatus updatedStatus) {
        Reclamation reclamation = findReclamationById(reclamationId);
        if (reclamation == null) {
            throw new IllegalArgumentException("Reclamation not found with ID: " + reclamationId);
        }

        ReclamationStatus currentStatus = reclamation.getStatus();
        boolean validTransition = (currentStatus == ReclamationStatus.PENDING && updatedStatus == ReclamationStatus.IN_PROGRESS)
                || (currentStatus == ReclamationStatus.IN_PROGRESS && updatedStatus == ReclamationStatus.RESOLVED);

        if (!validTransition) {
            throw new IllegalArgumentException("Invalid status transition.");
        }

        // Fetch admin User entity by username
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        // Set treatment data
        reclamation.setStatus(updatedStatus);
        reclamation.setResponseMessage(responseMessage);
        reclamation.setTreatedByAdminId(adminUser.getId());
        reclamation.setTreatedByAdminName(adminUser.getUsername());

        reclamationRepository.save(reclamation);
    }

    // === General Query Utilities ===
    @PreAuthorize("hasRole('ADMIN')")
    public Reclamation findReclamationById(Long id) {
        return reclamationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Reclamation not found with ID: " + id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getReclamationsBySubject(String subject) {
        return reclamationRepository.findBySubject(subject);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reclamation> getReclamationsByDate(boolean latest) {
        return latest
                ? reclamationRepository.findAllByOrderByCreatedAtDesc()
                : reclamationRepository.findAllByOrderByCreatedAtAsc();
    }

    public void updateReclamationStatus(Long reclamationId, ReclamationStatus status) {
        Reclamation reclamation = findReclamationById(reclamationId);
        reclamation.setStatus(status);
        reclamationRepository.save(reclamation);
    }

    // === Private Utility Methods ===

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private void validateComplaintSubject(User user, ReclamationSubject subject) {
        switch (user.getRoleName()) {
            case "FARMER" -> {
                if (!isFarmerComplaint(subject)) {
                    throw new IllegalArgumentException("Invalid subject for FARMER");
                }
            }
            case "TRANSPORTER" -> {
                if (!isTransporterComplaint(subject)) {
                    throw new IllegalArgumentException("Invalid subject for TRANSPORTER");
                }
            }
            case "CLIENT" -> {
                if (!isClientComplaint(subject)) {
                    throw new IllegalArgumentException("Invalid subject for CLIENT");
                }
            }
            default -> throw new IllegalArgumentException("Unsupported role: " + user.getRoleName());
        }
    }

    private boolean isFarmerComplaint(ReclamationSubject subject) {
        return switch (subject) {
            case DELIVERY_DELAY_BY_TRANSPORTER, ORDER_CANCELLATION_BY_CLIENT,
                 INCORRECT_PRICE_OR_QUANTITY, UNAVAILABILITY_OF_GOODS -> true;
            default -> false;
        };
    }

    private boolean isTransporterComplaint(ReclamationSubject subject) {
        return switch (subject) {
            case DELIVERY_DELAY_BY_CLIENT, CLIENT_REFUSES_TO_ACCEPT_GOODS,
                 WRONG_PRODUCT_INFORMATION, UNAVAILABILITY_OF_CLIENT -> true;
            default -> false;
        };
    }

    private boolean isClientComplaint(ReclamationSubject subject) {
        return switch (subject) {
            case POOR_QUALITY_OF_GOODS, DELAYED_DELIVERY, WRONG_PRODUCT_DELIVERED,
                 GOODS_NOT_AS_DESCRIBED, PRICE_DISCREPANCY -> true;
            default -> false;
        };
    }

    private void validateStatusTransition(ReclamationStatus current, ReclamationStatus newStatus) {
        if (current == ReclamationStatus.PENDING && newStatus == ReclamationStatus.IN_PROGRESS) return;
        if (current == ReclamationStatus.IN_PROGRESS && newStatus == ReclamationStatus.RESOLVED) return;

        throw new IllegalArgumentException(
                String.format("Invalid transition from %s to %s", current, newStatus)
        );
    }
}
