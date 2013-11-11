package concurrency.stm;

public abstract class TransactionBlock implements Runnable {
    private Transaction tx;

    void setTx(Transaction tx) {
        this.tx = tx;
    }

    public Transaction getTx() {
        return tx;
    }
}
