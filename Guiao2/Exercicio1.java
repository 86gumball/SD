import java.util.*;
import java.util.concurrent.locks.*;

public class Exercicio1 {
    public static void main (String[] args) {
        
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
}
