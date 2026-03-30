import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

class UC11ReservationRequest {
    private final String guestName;
    private final String roomType;

    public UC11ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class UC11InventoryService {
    private final Map<String, Integer> inventory = new HashMap<>();

    public UC11InventoryService() {
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available <= 0) {
            return false;
        }

        inventory.put(roomType, available - 1);
        return true;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms left");
        }
    }
}

class UC11BookingWorker implements Runnable {
    private final Queue<UC11ReservationRequest> bookingQueue;
    private final UC11InventoryService inventoryService;

    public UC11BookingWorker(Queue<UC11ReservationRequest> bookingQueue, UC11InventoryService inventoryService) {
        this.bookingQueue = bookingQueue;
        this.inventoryService = inventoryService;
    }

    @Override
    public void run() {
        while (true) {
            UC11ReservationRequest request;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    return;
                }
                request = bookingQueue.poll();
            }

            if (inventoryService.allocateRoom(request.getRoomType())) {
                String roomId = request.getRoomType().replace(" ", "").substring(0, 3).toUpperCase()
                        + "-" + UUID.randomUUID().toString().substring(0, 4);
                System.out.println(Thread.currentThread().getName()
                        + " confirmed " + request.getGuestName()
                        + " for " + request.getRoomType()
                        + " with Room ID " + roomId);
            } else {
                System.out.println(Thread.currentThread().getName()
                        + " failed booking for " + request.getGuestName()
                        + " because " + request.getRoomType() + " is unavailable");
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 11.0");
        System.out.println("=====================================\n");

        Queue<UC11ReservationRequest> bookingQueue = new LinkedList<>();
        bookingQueue.offer(new UC11ReservationRequest("Alice", "Single Room"));
        bookingQueue.offer(new UC11ReservationRequest("Bob", "Single Room"));
        bookingQueue.offer(new UC11ReservationRequest("Charlie", "Single Room"));
        bookingQueue.offer(new UC11ReservationRequest("David", "Single Room"));
        bookingQueue.offer(new UC11ReservationRequest("Eve", "Double Room"));
        bookingQueue.offer(new UC11ReservationRequest("Frank", "Double Room"));
        bookingQueue.offer(new UC11ReservationRequest("Grace", "Double Room"));

        UC11InventoryService inventoryService = new UC11InventoryService();

        Thread workerOne = new Thread(new UC11BookingWorker(bookingQueue, inventoryService), "Thread-1");
        Thread workerTwo = new Thread(new UC11BookingWorker(bookingQueue, inventoryService), "Thread-2");
        Thread workerThree = new Thread(new UC11BookingWorker(bookingQueue, inventoryService), "Thread-3");

        workerOne.start();
        workerTwo.start();
        workerThree.start();

        try {
            workerOne.join();
            workerTwo.join();
            workerThree.join();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println("Simulation interrupted: " + exception.getMessage());
        }

        inventoryService.displayInventory();
    }
}
