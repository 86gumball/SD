import java.util.*;

public class Exercicio1 {
    private static final int N = 10;

    // Using array
    public static void main (String[] args) throws InterruptedException {
        Thread[] arr = new Thread[N];

        for (int i = 0; i < N; i++) {
            Thread t = new Thread(new Incrementer());
            arr[i] = t;
            t.start();
        }

        for (int i = 0; i < N; i++)
            arr[i].join();

        System.out.println("fim");
    }


    // Using List
    public static void main2 (String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            Thread t = new Thread(new Incrementer());
            threads.add(t);
            t.start();
        }

        for (Thread t : threads)
            t.join();

        System.out.println("fim");
    }
}

class Incrementer implements Runnable {
    public void run() {
        final long I = 100;
        for (long i = 0; i < I; i++)
            System.out.println(i);
    }
}
