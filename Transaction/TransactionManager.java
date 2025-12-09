package Transaction;

import java.util.Arrays;

public class TransactionManager {
    private Transaction[] transactions = new Transaction[200];
    static private int transactionCount = 0;

    public void addTransaction(Transaction transaction){
        transactions[transactionCount++] = transaction;
    }

    public void viewTransactionsByAccount(String accountNumber){
        StringBuilder output = new StringBuilder("""
                TRANSACTION HISTORY
                --------------------------------------------------------------------------
                TXN ID | DATE/TIME            | TYPE       | AMOUNT       | BALANCE
                ---------------------------------------------------------------------------
                """);

        int counter = 0;
        double netChange = 0;

        for(Transaction transaction : transactions){
            if(transaction == null) break;

            boolean isWithdrawal = transaction.getType().equals("WITHDRAWAL");

            netChange = netChange + (isWithdrawal ? -transaction.getAmount() : transaction.getAmount());

            if(transaction.getAccountNumber().equals(accountNumber)){
                output.append(String.format("%s | %s | %s | %c$%f | $%f\n",
                    transaction.getTransactionId(),
                    transaction.getTimestamp(),
                    transaction.getType(),
                    isWithdrawal ? '-' : '+',
                    transaction.getAmount(),
                    transaction.getBalanceAfter()
                ));
                counter++;
            }
        }

        output.append(String.format("""
            Total Transactions: %d
            Total Depositions: +$%f
            Total Withdrawals: -$%f
            Net Change: %c$%f
            """,
            getTransactionCount(),
            calculateTotalDeposits(accountNumber),
            calculateTotalWithdrawals(accountNumber),
            netChange < 0 ? '-' : '+',
            netChange
        ));

        System.out.println(counter == 0 ? """
        --------------------------------------------------------------------------
        no transactions recorded for this account.
        ---------------------------------------------------------------------------
        """ : output.toString());
    }

    public double calculateTotalDeposits(String accountNumber){
        double totalDeposits = 0;

        for(Transaction transaction : transactions){
            if(transaction == null) break;

            if(transaction.getAccountNumber().equals(accountNumber) && transaction.getType().equals("DEPOSIT")){
                totalDeposits += transaction.getAmount();
            }
        }

        return totalDeposits;
    }

    public double calculateTotalWithdrawals(String accountNumber){
        double totalWithdrawals = 0;

        for(Transaction transaction : transactions){
            if(transaction == null) break;

            if(transaction.getAccountNumber().equals(accountNumber) && transaction.getType().equals("WITHDRAWAL")){
                totalWithdrawals += transaction.getAmount();
            }
        }
        return totalWithdrawals;
    }

    public int getTransactionCount(){
        return transactionCount;
    }
}
