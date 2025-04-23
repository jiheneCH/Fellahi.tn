package tn.esprit.fallehiuser.Execption;

/**
 * Custom exception thrown when the username is already taken during registration.
 */
public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException() {
        super("Username is already taken.");
    }

    public UsernameAlreadyTakenException(String message) {
        super(message);
    }

    public UsernameAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyTakenException(Throwable cause) {
        super(cause);
    }
}
