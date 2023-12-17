
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException {
        int countOfThreads = Integer.parseInt(HandlerInputParametrs.Handler(args[0]));
        Map<Integer, String> map = FileReader.getUrlsFromFile();
        ThreadFixedPool threadFixedPool = new ThreadFixedPool(countOfThreads);
        for (final var pair : map.entrySet()) {
            threadFixedPool.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " start download file number " + pair.getKey());
                    Files.copy(new URI(pair.getValue()).toURL().openConnection().getInputStream(), Paths.get(pair.getValue().split("/")[pair.getValue().split("/").length - 1]).toAbsolutePath());
                    System.out.println(Thread.currentThread().getName() + " finish download file number " + pair.getKey());
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        //   threadFixedPool.shutDownNow();
    }
}

class ThreadFixedPool {
    final int countThread;
    final PoolWorker[] threadPool;

    final Queue<Runnable> blockedQueue;

    volatile boolean start;


    ThreadFixedPool(int countThread) {
        this.countThread = countThread;
        threadPool = new PoolWorker[countThread];
        blockedQueue = new ArrayDeque<>();
        start = true;
        for (int i = 0; i != countThread; i++) {
            threadPool[i] = new PoolWorker();
            threadPool[i].start();
        }
    }

    void shutDownNow() {
        start = false;
    }

    void execute(Runnable runnable) {
        synchronized (blockedQueue) {
            blockedQueue.add(runnable);
            blockedQueue.notifyAll();
        }
    }

    private class PoolWorker extends Thread {
        Runnable task;

        @Override
        public void run() {
            while (start) {
                synchronized (blockedQueue) {
                    while (blockedQueue.isEmpty()) {
                        try {
                            blockedQueue.wait();
                        } catch (InterruptedException e) { /*NOP*/ }
                    }
                    task = blockedQueue.poll();
                }
                task.run();
            }
        }
    }
}


