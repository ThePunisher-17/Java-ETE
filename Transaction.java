import java.util.*;

// Order class represents a customer order with a unique ID
class Order {
    private int id;
    public Order(int id) { this.id = id; }
    public int getId() { return id; }
}

// SharedQueue implements a thread-safe queue using the producer-consumer pattern
class SharedQueue {
    private Queue<Order> orders = new LinkedList<>();
    private static final int MAX_SIZE = 5; // Maximum orders that can be in queue at once

    // Producer method: Adds orders to queue
    public synchronized void add(Order order) throws InterruptedException {
        while (orders.size() >= MAX_SIZE) wait(); // Wait if queue is full
        orders.add(order);
        System.out.println("Order added: " + order.getId());
        notifyAll(); // Notify waiting consumers (chefs)
    }

    // Consumer method: Removes orders from queue
    public synchronized Order remove() throws InterruptedException {
        while (orders.isEmpty()) wait(); // Wait if queue is empty
        Order order = orders.remove();
        notifyAll(); // Notify waiting producers (waiters)
        return order;
    }
}

// Chef class represents the consumer thread
class Chef implements Runnable {
    private SharedQueue queue;
    private static final int TOTAL_ORDERS = 15; // Total orders to process

    public Chef(SharedQueue queue) { this.queue = queue; }

    @Override
    public void run() {
        for (int i = 0; i < TOTAL_ORDERS; i++) {
            try {
                Order order = queue.remove(); // Get order from queue
                System.out.println("Preparing order: " + order.getId());
                Thread.sleep(1500); // Simulate cooking time
                System.out.println("Completed order: " + order.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

// Waiter class represents the producer thread
class Waiter implements Runnable {
    private SharedQueue queue;
    private static final int TOTAL_ORDERS = 15; // Total orders to create

    public Waiter(SharedQueue queue) { this.queue = queue; }

    @Override
    public void run() {
        for (int i = 1; i <= TOTAL_ORDERS; i++) {
            try {
                queue.add(new Order(i)); // Create and add new order to queue
                Thread.sleep(1000); // Simulate time between taking orders
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

// Main class that simulates a restaurant ordering system
public class Transaction {
    public static void main(String[] args) {
        // Initialize shared queue and create chef and waiter threads
        SharedQueue queue = new SharedQueue();
        Thread chef = new Thread(new Chef(queue));
        Thread waiter = new Thread(new Waiter(queue));
        
        // Start both threads
        chef.start();
        waiter.start();
        
        try {
            // Wait for both threads to complete
            chef.join();
            waiter.join();
            System.out.println("Restaurant closed!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}