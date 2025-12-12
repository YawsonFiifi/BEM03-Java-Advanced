package CustomExceptions;

public class WithdrawalException extends TransactionException {
    public WithdrawalException() {
        super("Withdrawal failed");
    }

    public WithdrawalException(String message) {
        super("Withdrawal Error: " + message);
    }

    public WithdrawalException(String message, Throwable cause) {
        super(message, cause);
    }

    public WithdrawalException(Throwable cause) {
        super(cause);
    }
}
