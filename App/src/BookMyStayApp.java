import java.util.*;

class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
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

class InventoryService {
    private HashMap<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms left");
        }
    }
}

class BookingService {
    private Queue<ReservationRequest> bookingQueue;
    private InventoryService inventoryService;
    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> roomAllocationMap;
    private int roomCounter = 1;

    public BookingService(Queue<ReservationRequest> bookingQueue, InventoryService inventoryService) {
        this.bookingQueue = bookingQueue;
        this.inventoryService = inventoryService;
        allocatedRoomIds = new HashSet<>();
        roomAllocationMap = new HashMap<>();
    }

    public void processBookings() {
        System.out.println("\nProcessing Booking Requests...\n");
        while (!bookingQueue.isEmpty()) {
            ReservationRequest request = bookingQueue.poll();
            String roomType = request.getRoomType();
            if (inventoryService.getAvailability(roomType) > 0) {
                String roomId = generateRoomId(roomType);
                allocatedRoomIds.add(roomId);
                roomAllocationMap.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);
                inventoryService.decrementRoom(roomType);
                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println("-----------------------------");
            } else {
                System.out.println("Reservation Failed for " + request.getGuestName());
                System.out.println("Reason: No rooms available for " + roomType);
                System.out.println("-----------------------------");
            }
        }
    }

    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.replace(" ", "").substring(0, 3).toUpperCase() + roomCounter++;
        }
        while (allocatedRoomIds.contains(roomId));
        return roomId;
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 6.1");
        System.out.println("=====================================\n");

        Queue<ReservationRequest> bookingQueue = new LinkedList<>();
        bookingQueue.add(new ReservationRequest("Alice", "Single Room"));
        bookingQueue.add(new ReservationRequest("Bob", "Single Room"));
        bookingQueue.add(new ReservationRequest("Charlie", "Single Room"));
        bookingQueue.add(new ReservationRequest("David", "Suite Room"));

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(bookingQueue, inventoryService);

        bookingService.processBookings();
        inventoryService.displayInventory();
    }
}
