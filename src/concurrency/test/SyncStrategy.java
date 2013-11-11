package concurrency.test;

public class SyncStrategy implements TransferStrategy{
    @Override
    public synchronized void transfer(Account a, Account b, int amount) {
        a.add(-amount);
        b.add(amount);
    }
}