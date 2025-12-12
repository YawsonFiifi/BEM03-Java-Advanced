package CustomExceptions;

public class DepositException extends TransactionException {
    public DepositException() {
        super("Deposit failed");
    }

    public DepositException(String message) {
        super(message);
    }

    public DepositException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepositException(Throwable cause) {
        super(cause);
    }
}
