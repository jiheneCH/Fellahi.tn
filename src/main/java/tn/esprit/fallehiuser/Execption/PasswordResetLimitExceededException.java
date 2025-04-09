package tn.esprit.fallehiuser.Execption;

public class PasswordResetLimitExceededException extends RuntimeException {
    public PasswordResetLimitExceededException(String message) {
        super(message);
    }
}
