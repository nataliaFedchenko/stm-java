package concurrency.stm;

public final class STM {
    private STM() {}

    public static final Object commitLock = new Object();

    public static <T> T transaction(TransactionBlock block, T param) {
        boolean committed = false;
        T returnVal = null;
        
        while (!committed) {
            Transaction tx = new Transaction();
            block.setTx(tx);
            block.run();
            committed = tx.commit();
            tx.setValue(param);
            returnVal = (T)tx.getValue();
        }
        return returnVal;
    }

}