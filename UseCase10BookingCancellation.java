import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class UC10Reservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private String allocatedRoomId;

    public UC10Reservation(String guestName, String roomType) {
        this.reservationId = UUID.randomUUID().toString().substring(0, 8);
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getAllocatedRoomId() {
        return allocatedRoomId;
    }

    public void setAllocatedRoomId(String allocatedRoomId) {
        this.allocatedRoomId = allocatedRoomId;
    }
}

class UC10InventoryService {
    private int singleRooms = 2;

    public void decrementRoom() {
        singleRooms--;
    }

    public void incrementRoom() {
        singleRooms++;
    }

    public void displayInventory() {
        System.out.println("Single Room : " + singleRooms + " rooms left");
    }
}

class UC10BookingHistory {
    private final List<UC10Reservation> confirmedBookings = new ArrayList<>();

    public void recordBooking(UC10Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public void removeBooking(UC10Reservation reservation) {
        confirmedBookings.remove(reservation);
    }

    public boolean contains(UC10Reservation reservation) {
        return confirmedBookings.contains(reservation);
    }

    public void displayHistory() {
        System.out.println("Confirmed Bookings:");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No active bookings found.");
            return;
        }

        for (UC10Reservation reservation : confirmedBookings) {
            System.out.println(reservation.getGuestName() + " | "
                    + reservation.getRoomType() + " | "
                    + reservation.getAllocatedRoomId());
        }
    }
}

class UC10CancellationService {
    private final UC10InventoryService inventoryService;
    private final UC10BookingHistory bookingHistory;
    private final Stack<String> rollbackStack = new Stack<>();

    public UC10CancellationService(UC10InventoryService inventoryService, UC10BookingHistory bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
    }

    public void cancelBooking(UC10Reservation reservation) {
        if (reservation == null || reservation.getAllocatedRoomId() == null) {
            System.out.println("Cancellation failed: reservation is invalid or not confirmed.");
            return;
        }

        if (!bookingHistory.contains(reservation)) {
            System.out.println("Cancellation failed: reservation not found in booking history.");
            return;
        }

        rollbackStack.push(reservation.getAllocatedRoomId());
        inventoryService.incrementRoom();
        bookingHistory.removeBooking(reservation);

        System.out.println("Cancelled reservation for " + reservation.getGuestName());
        System.out.println("Released Room ID: " + reservation.getAllocatedRoomId());
    }

    public void displayRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 10.0");
        System.out.println("=====================================\n");

        UC10InventoryService inventoryService = new UC10InventoryService();
        UC10BookingHistory bookingHistory = new UC10BookingHistory();

        UC10Reservation reservation = new UC10Reservation("Alice", "Single Room");
        reservation.setAllocatedRoomId("SIN-101");

        inventoryService.decrementRoom();
        bookingHistory.recordBooking(reservation);

        System.out.println("Before Cancellation:");
        inventoryService.displayInventory();
        bookingHistory.displayHistory();
        System.out.println();

        UC10CancellationService cancellationService = new UC10CancellationService(inventoryService, bookingHistory);
        cancellationService.cancelBooking(reservation);

        System.out.println("\nAfter Cancellation:");
        inventoryService.displayInventory();
        bookingHistory.displayHistory();
        cancellationService.displayRollbackStack();
    }
}
