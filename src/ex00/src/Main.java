import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String argc = HandlerInputParametrs.Handler(args[0]);
        Thread runner = new Thread(() -> {
            try {
                runPrint(argc);
            } catch (InterruptedException ignore) { /*NOP*/}
        });
        runner.start();
        runner.join();
        runEndProgramm(argc);
    }

    static void runPrint(String argc) throws InterruptedException {
        final int count = Integer.parseInt(argc);
        Thread eggThread = new Thread(() -> {
            for (int i = 0; i != count; i++) {
                System.out.println("Egg");
            }
        }
        );

        Thread henThread = new Thread(() -> {
            for (int i = 0; i != count; i++) {
                System.out.println("Hen");
            }
        });
        eggThread.start();
        henThread.start();
        eggThread.join();
        henThread.join();
    }

    static void runEndProgramm(String argc) {
        for (int i = 0; i != Integer.parseInt(argc); i++) {
            System.out.println("Human");
        }
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


