package tn.esprit.fallehiuser.Services;


import org.springframework.web.multipart.MultipartFile;
import tn.esprit.fallehiuser.DTO.AuthenticationResponse;
import tn.esprit.fallehiuser.model.User;


public interface IAuthService {
    void register(User user);
    boolean verifyTwoFactorCode(String email, String code);
    String generateOtp();
    void sendOtpByEmail(String email, String otp);
    void completeUserProfile(Long userId, String phone, String address, String governorate, MultipartFile profilePicture);

    AuthenticationResponse googleRegister(String idToken);

}

