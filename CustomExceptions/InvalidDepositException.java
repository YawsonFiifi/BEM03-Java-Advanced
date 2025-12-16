package CustomExceptions;

public class InvalidDepositException extends TransactionException {
    public InvalidDepositException() {
        super("Error: Invalid amount");
    }

    public InvalidDepositException(String message) {
        super("Error: " + message);
    }

    public InvalidDepositException(String message, Throwable cause) {
        super("Error : " + message, cause);
    }

    public InvalidDepositException(Throwable cause) {
        super(cause);
    }
}
