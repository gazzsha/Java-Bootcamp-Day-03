import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lock {

    Runnable object;
    synchronized void push(Runnable o) throws InterruptedException {
        while (object != null) {
            wait();
        }
        object = o;
        object.run();
        notify();
    }

    synchronized void poll(Runnable o) throws InterruptedException {
        while (object == null)
            wait();
        o.run();
        object = null;
        notify();
    }
}

public class Main {
    public static void main(String[] args) {
        String argc = HandlerInputParametrs.Handler(args[0]);
        Lock lock = new Lock();
        new Thread(() -> {
            for(int i = 0; i != Integer.parseInt(argc); i++) {
                try {
                    lock.push(()->System.out.println("Egg"));
                } catch (InterruptedException ignore) { /*NOP*/ }
            }
        }).start();
        new Thread(() -> {
            for(int i = 0; i != Integer.parseInt(argc); i++) {
                try {
                    lock.poll(()->System.out.println("Hen"));
                } catch (InterruptedException ignore) { /*NOP*/ }
            }
        }).start();
    }
}

final class HandlerInputParametrs {

    final static String KEY_WORD = "^--count=\\d+$";

    static String Handler(String argc) {
        Pattern pattern = Pattern.compile(KEY_WORD);
        Matcher matcher = pattern.matcher(argc);
        if (matcher.matches()) {
            return matcher.group(0).substring("--count=".length());
        } else throw new RuntimeException("Wrong input!");
    }
}
