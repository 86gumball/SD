import java.util.*;
import java.util.concurrent.locks.*;

class Bank {
    private static class Account {
        private int balance;
        Account(int balance) { this.balance = balance; }

        int balance() { return balance; }

        boolean deposit(int value) {
            balance += value;
            return true;
        }

        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    // Bank slots and vector of accounts
    private int slots;
    private Account[] av; 
    private Lock lock = new ReentrantLock();

    public Bank(int n) {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    // Account balance
    public int balance(int id) {
        if (id < 0 || id >= slots)
            return 0;

        this.lock.lock();
        try {
            return av[id].balance();
        } finally {
            this.lock.unlock();
        }
    }

    // Deposit
    boolean deposit(int id, int value) {
        if (id < 0 || id >= slots)
            return false;

        this.lock.lock();
        try {
            return av[id].deposit(value);
        } finally {
            this.lock.unlock();
        }
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        
        this.lock.lock();
        try {
            return av[id].withdraw(value);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean transfer(int from, int to, int value) {
        this.lock.lock();
        try {
            if (this.withdraw(from, value)) {
                if (this.deposit(to, value)) {
                    return true;
                } else {
                    this.deposit(from, value);
                }
            }
        } finally {
            this.lock.unlock();
        }
        return false;
    }

    public int totalBalance() {
        this.lock.lock();
        try {
            int total = 0;
            for (Account acc : this.av) {
                total += acc.balance();
            }
            return total;
        } finally {
            this.lock.unlock();
        }
    }
}

// From Bank-test.java
class Mover implements Runnable {
    Bank b;
    int s; // Number of accounts

    public Mover(Bank b, int s) { this.b=b; this.s=s; }

    public void run() {
        final int moves=100000;
        int from, to;
        Random rand = new Random();

        for (int m=0; m<moves; m++) {
            from=rand.nextInt(s); // Get one
            while ((to=rand.nextInt(s))==from); // Slow way to get distinct
            b.transfer(from,to,1);
        }
    }
}

// This is the BankTest class
public class Exercicio2 {
    public static void main(String[] args) throws InterruptedException {
        final int N=10;

        Bank b = new Bank(N);

        for (int i=0; i<N; i++) 
            b.deposit(i,1000);

        System.out.println(b.totalBalance());

        Thread t1 = new Thread(new Mover(b,10)); 
        Thread t2 = new Thread(new Mover(b,10));

        t1.start(); t2.start(); t1.join(); t2.join();

        System.out.println(b.totalBalance());
    }
}
