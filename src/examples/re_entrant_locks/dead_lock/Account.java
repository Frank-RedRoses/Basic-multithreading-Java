package examples.re_entrant_locks.dead_lock;

/**
 * Represent a bank account and its basic operations.
 */
public class Account {
    private int balance = 10000;   // Initial balance of the account

    // getter
    public int getBalance() {
        return balance;
    }

    /**
     * Make a deposit into the account.
     * Add the given {@code amount} to the {@code balance} of the account.
     *
     * @param amount money amount to deposit
     */
    public void deposit(int amount) {
        balance += amount;
    }

    /**
     * Make a withdrawal from the account.
     * Take the given {@code amount} from the {@code balance} of the account.
     *
     * @param amount money amount to withdraw
     */
    public void withdraw(int amount) {
        balance -= amount;
    }

    /**
     * Make a transfer from one {@code Account} to another {@code Account}.
     *
     * @param fromAccount the {@code Account} from which is taking out the money.
     * @param toAccount the {@code Account} in which the money is deposit.
     * @param amount amount of money to transfer.
     */
    public static void transfer(Account fromAccount, Account toAccount, int amount) {
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
    }
}
