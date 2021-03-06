package concurrency.stm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class Transaction<T> extends Context{
    
    private HashMap<Ref, Object> inTxMap = new HashMap<>();
    private HashSet<Ref> toUpdate = new HashSet<>();
    private HashMap<Ref, Long> version = new HashMap<>();
    private ArrayList<T> values = new ArrayList<>();

    private long revision;
    private static AtomicLong transactionNum = new AtomicLong(0);

    Transaction() {
        revision = transactionNum.incrementAndGet();
    }
    
    T getValue() {
        if (values.size() > 0) {
            return values.get(values.size() - 1);
        }
        return null;
    }
    
    void setValue(T val) {
        values.add(val);
    }
    
    @Override
    <T> T get(Ref<T> ref) {
        if (!inTxMap.containsKey(ref)) {
            RefTuple<T, Long> tuple = ref.content;
            inTxMap.put(ref, tuple.value);
            if (!version.containsKey(ref)) {
                version.put(ref, tuple.revision);
            }
        }
        return (T)inTxMap.get(ref);
    }

    <T> void set(Ref<T> ref, T value) {
        inTxMap.put(ref, value);
        toUpdate.add(ref);
        if (!version.containsKey(ref)) {
            version.put(ref, ref.content.revision);
        }
    }

    boolean commit() {
        synchronized (STM.commitLock) {
            // validation
            boolean isValid = true;
            for (Ref ref : inTxMap.keySet()) {
                if (ref.content.revision != version.get(ref)) {
                    isValid = false;
                    break;
                }
            }

            // writes
            if (isValid) {
                for (Ref ref : toUpdate) {
                    ref.content = RefTuple.get(inTxMap.get(ref), revision);
                }
            }
            return isValid;
        }
    }
}