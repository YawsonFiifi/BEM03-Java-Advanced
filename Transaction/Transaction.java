package Transaction;

import java.util.Date;

public class Transaction{
    public int transactionCounter = 0;

    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private String timestamp;

    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this.transactionId = "TXN"+ ++transactionCounter;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = String.format("%s", new Date().toString());
    }

    public void displayTransactionDetails(){
        System.out.printf("transaction ID: %s\nAccount: %s\nType: %s\nAmount: $%f\nPrevious Balance: $%f\nNew Balance: $%f\nDate/Time: %s\n",
                transactionId,
                accountNumber,
                type,
                amount,
                type.equals("WITHDRAWAL") ? balanceAfter + amount : balanceAfter - amount,
                balanceAfter,
                timestamp
        );
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }
}