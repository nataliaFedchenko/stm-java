package concurrency.test;

public class NonSyncStrategy implements TransferStrategy {
    @Override
    public void transfer(Account a, Account b, int amount) {
        a.add(-amount);
        b.add(amount);
    }
}
