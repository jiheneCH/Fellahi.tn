package tn.esprit.fallehiuser.DTO;

public class ReclamationDTO {
    private String description;
    private String subject;

    // Constructor, getters, and setters
    public ReclamationDTO(String description, String subject) {
        this.description = description;
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
