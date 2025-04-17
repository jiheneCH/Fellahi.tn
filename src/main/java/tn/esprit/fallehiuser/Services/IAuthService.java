package tn.esprit.fallehiuser.Services;


import tn.esprit.fallehiuser.DTO.AuthenticationResponse;
import tn.esprit.fallehiuser.model.User;


public interface IAuthService {
    void register(User user);
    boolean verifyTwoFactorCode(String email, String code);
    String generateOtp();
    void sendOtpByEmail(String email, String otp);
    AuthenticationResponse googleRegister(String idToken);

}

