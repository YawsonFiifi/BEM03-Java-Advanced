package models.exceptions;

public class InsufficientFundsException extends TransactionException {
    public InsufficientFundsException() {
        super("Transaction Failed");
    }

    public InsufficientFundsException(String message) {
        super("Transaction Failed: " + message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientFundsException(Throwable cause) {
        super(cause);
    }
}
