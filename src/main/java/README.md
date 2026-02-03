# Bank Account Management System

## Overview

A console-based Java banking application demonstrating object-oriented programming, error handling, testing, and version control practices.

## System Features

### Feature 1: Refactored Account and Transaction Classes

- Simplified methods with clear names and comments
- Modular design separating responsibilities (balance calculation vs. display)
- Helper methods for common operations like validateAmount()

### Feature 2: Error Handling and Validation

- Handle invalid inputs with try-catch blocks
- Throw custom exceptions (InsufficientFundsException, InvalidAccountException)
- Ensure withdrawals don't exceed overdraft limits or go below minimum balance

### Feature 3: Transaction Testing and Verification

- Write JUnit tests for deposit(), withdraw(), and transfer()
- Validate balance updates, exception conditions, and transaction records
- Log test results to console for clarity

### Feature 4: Git Version Control Integration

- Initialize a Git repository and track code changes
- Use branches for features (feature/error-handling, feature/testing)
- Merge and cherry-pick commits across branches for controlled integration

### Feature 5: Documentation and Code Comments

- Document all methods with clear Javadoc comments
- Explain complex logic inline
- Maintain README with setup and usage instructions

## Requirements

- Java JDK 8 or higher
- JUnit for testing
- Git for version control

## Installation

Clone and compile:
```bash
git clone <repository-url>
cd bank-account-system
javac Main.java models/*.java models/exceptions/*.java services/*.java Screens/*.java
java Main
```

## Project Structure

```
bank-account-system/
├── Main.java
├── models/
│   ├── Account.java
│   ├── CheckingAccount.java
│   ├── SavingsAccount.java
│   ├── Customer.java
│   ├── Transaction.java
│   └── exceptions/
├── services/
│   ├── AccountManager.java
│   └── TransactionManager.java
└── Screens/
```

## Main Menu Options

1. Create Account
2. Manage Accounts
3. Perform Transactions
4. Generate Account Statements
5. Run Tests
6. Exit

## Account Types

Savings Account:
- Minimum balance: $500
- Interest rate: 3.5%

Checking Account:
- Overdraft limit: $1,000
- Monthly fee: $10 (waived for Premium customers)

## Customer Types

Regular Customer - Standard services
Premium Customer - Minimum $10,000 deposit, waived fees

## Error Handling

Custom exceptions handle:
- InsufficientFundsException - Not enough balance
- InvalidDepositException - Invalid amount
- AccountCreationException - Invalid account setup
- AccountNotFound - Account doesn't exist

All errors use try-catch blocks with user-friendly messages.

## Testing

Run tests from main menu option 5.

Tests cover:
- deposit() method
- withdraw() method
- transfer() method
- Balance validation
- Exception handling

## Common Operations

Create account:
```java
Customer customer = new RegularCustomer("John Doe", 30, "555-0100", "123 Main St");
Account account = new SavingsAccount(customer, 1000.00);
```

Deposit:
```java
account.deposit(500.00);
```

Withdraw:
```java
account.withdraw(200.00);
```

## Version Control

Project uses Git with feature branches. See git-workflow.md for details.

## Limitations

- Maximum 50 accounts
- Maximum 200 transactions
- No data persistence
- Console interface only

## Documentation

All code includes Javadoc comments explaining methods, parameters, and exceptions.
