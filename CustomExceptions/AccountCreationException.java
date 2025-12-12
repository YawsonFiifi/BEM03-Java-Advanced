package CustomExceptions;

public class AccountCreationException extends Exception {
    public AccountCreationException() {
        super("Account creation failed");
    }

    public AccountCreationException(String message) {
        super("Account creation failed: "+message);
    }

    public AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
