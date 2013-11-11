package concurrency.test;

import concurrency.stm.STM;
import concurrency.stm.Transaction;
import concurrency.stm.TransactionBlock;

public class STMStrategy implements TransferStrategy {
    
    static int count = 0;
    
    @Override
    public void transfer(final Account a, final Account b, final int amount) {
        
        Long l = STM.<Long>transaction(new TransactionBlock() {
            @Override
            public void run() {
                Transaction tx = this.getTx();
                long old1 = a.getRef().getValue(tx);
                a.getRef().setValue(old1 - amount, tx);
                long old2 = b.getRef().getValue(tx);
                b.getRef().setValue(old2 + amount, tx);
            }
        }, (long)count);
        count ++;
        
        System.out.println("iiiiii=" + l);
    }
}
