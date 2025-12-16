package CustomExceptions;

public class AccountNotFound extends Exception {
    public AccountNotFound() {
        super("Error: Account Not Found. Please check account number and try again.");
    }

    public AccountNotFound(String message) {
        super("Account Not Found: " + message);
    }

    public AccountNotFound(String message, Throwable cause) {
        super("Account Not Found: " + message, cause);
    }

    public AccountNotFound(Throwable cause) {
        super(cause);
    }
}
