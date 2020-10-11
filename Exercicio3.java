import java.util.*;
import java.util.concurrent.locks.*;

public class Exercicio3 {
    private static final int N = 10;
    private static final int I = 1000;
    private static final int V = 100;

    public static void main (String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        Bank bank = new Bank();

        for (int i = 0; i < N; i++) {
            Thread t = new Thread(new Depositer(bank, I, V));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads)
            t.join();

        System.out.println("O valor na conta e " + bank.balance());
    }
}

class Depositer implements Runnable {
    private Bank bank;
    private int nrDepositos;
    private int valorDeposito;

    public Depositer(Bank b, int nrDepositos, int valorDeposito) {
        this.bank = b;
        this.nrDepositos = nrDepositos;
        this.valorDeposito = valorDeposito;
    }

    public void run() {
        for (int i = 0; i < nrDepositos; i++) {
            this.bank.deposit(this.valorDeposito);
        }
    }
}

class Bank {
    private static class Account {
        private int balance;
        Account(int balance) { this.balance = balance; }
        int balance() { return balance; }
        boolean deposit(int value) {
            balance += value;
            return true;
        }
    }

    // Our single account, for now
    private Account savings = new Account(0);
    private Lock lock = new ReentrantLock();

    // Account balance
    public int balance() {
        return savings.balance();
    }

    // Deposit
    boolean deposit(int value) {
        lock.lock();
        try {
            return savings.deposit(value);
        } finally {
            lock.unlock();
        }
    }
}
