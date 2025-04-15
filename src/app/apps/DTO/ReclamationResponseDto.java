public class ReclamationResponseDto {
    private Long reclamationId;
    private Long adminId;
    private String responseMessage;

    // Constructors
    public ReclamationResponseDto() {}

    public ReclamationResponseDto(Long reclamationId, Long adminId, String responseMessage) {
        this.reclamationId = reclamationId;
        this.adminId = adminId;
        this.responseMessage = responseMessage;
    }

    // Getters and setters
    public Long getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(Long reclamationId) {
        this.reclamationId = reclamationId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
