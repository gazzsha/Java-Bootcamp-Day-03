
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

class Sum {
    Integer value = 0;

    void addition(Integer value) {
        this.value += value;
    }
}


public class Main {
    static final int MAX_VALUE = 1000;

    public static void main(String[] args) throws InterruptedException {
        Pair<Integer> pair = HandlerInputParametrs.Handler(args[0], args[1]);
        List<Integer> arrayList = ThreadLocalRandom.current().ints(-MAX_VALUE, MAX_VALUE).limit(pair.getFirst()).boxed().toList();
        System.out.println("Sum: " + arrayList.stream().reduce(Integer::sum).get());
        System.out.println(arrayList);
        Object lock = new Object();
        Sum sum = new Sum();
        final int delimiter = (int) Math.ceil((double) arrayList.size() / pair.getSecond());
        Thread[] threads = new Thread[pair.getSecond()];
        for (int i = 0; i < pair.getSecond(); i++) {
            synchronized (lock) {
                int finalI = i;
                threads[i] = new Thread(() -> {
                    int from = delimiter * finalI;
                    int to = ((finalI + 1) * delimiter) >= pair.getFirst() ? pair.getFirst() : (finalI + 1) * delimiter;
                    Integer value = summator(arrayList.subList(from, to));
                    synchronized (sum) {
                        System.out.println(Thread.currentThread().getName() +
                                ": from " + from + " to " + (to - 1) + " sum is " + value);
                        sum.addition(value);
                    }
                });
                threads[i].start();
            }
        }
        for (int i = 0; i != threads.length; i++) {
            threads[i].join();
        }
        System.out.println("Sum by threads: " + sum.value);
    }


    static <T extends Integer> Integer summator(Collection<? extends T> collection) {
        return collection.stream().map(Integer::intValue).reduce(Integer::sum).get();
    }
}


final class HandlerInputParametrs {

    final static String KEY_WORD_FIRST = "^--arraySize=\\d+$";
    final static String KEY_WORD_SECOND = "^--threadsCount=\\d+$";

    static Pair<Integer> Handler(String argc1, String argc2) {
        if (Pattern.compile(KEY_WORD_FIRST).matcher(argc1).matches() &&
                Pattern.compile(KEY_WORD_SECOND).matcher(argc2).matches()) {
            return new Pair<>(Integer.parseInt(argc1.substring("--arraySize=".length())),
                    Integer.parseInt(argc2.substring("--threadsCount=".length())));
        } else throw new RuntimeException("Wrong input!");
    }
}

class Pair<T extends Number> {
    private final T first;
    private final T second;

    Pair(T s1, T s2) {
        first = s1;
        second = s2;
    }

    T getFirst() {
        return first;
    }

    T getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "First: " + first + "\nSecond:" + second;
    }

}

