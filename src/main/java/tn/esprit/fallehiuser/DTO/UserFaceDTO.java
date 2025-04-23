package tn.esprit.fallehiuser.DTO;

public class UserFaceDTO {
    private Long userId;
    private String imageBase64;

    public UserFaceDTO(Long userId, String imageBase64) {
        this.userId = userId;
        this.imageBase64 = imageBase64;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
