import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class UC12Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String guestName;
    private final String roomType;

    public UC12Reservation(String guestName, String roomType) {
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

class UC12InventorySnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private int singleRooms;
    private int doubleRooms;

    public UC12InventorySnapshot(int singleRooms, int doubleRooms) {
        this.singleRooms = singleRooms;
        this.doubleRooms = doubleRooms;
    }

    public int getSingleRooms() {
        return singleRooms;
    }

    public int getDoubleRooms() {
        return doubleRooms;
    }
}

class UC12HotelState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UC12InventorySnapshot inventorySnapshot;
    private final List<UC12Reservation> bookingHistory;

    public UC12HotelState(UC12InventorySnapshot inventorySnapshot, List<UC12Reservation> bookingHistory) {
        this.inventorySnapshot = inventorySnapshot;
        this.bookingHistory = bookingHistory;
    }

    public UC12InventorySnapshot getInventorySnapshot() {
        return inventorySnapshot;
    }

    public List<UC12Reservation> getBookingHistory() {
        return bookingHistory;
    }
}

class UC12PersistenceService {
    private static final String FILE_NAME = "uc12_hotel_data.ser";

    public void saveState(UC12HotelState state) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            outputStream.writeObject(state);
            System.out.println("State saved successfully to " + FILE_NAME);
        } catch (IOException exception) {
            System.out.println("Failed to save state: " + exception.getMessage());
        }
    }

    public UC12HotelState loadState() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("State restored successfully from " + FILE_NAME);
            return (UC12HotelState) inputStream.readObject();
        } catch (Exception exception) {
            System.out.println("No valid saved state found. Starting with a fresh system.");
            return null;
        }
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 12.0");
        System.out.println("=====================================\n");

        UC12PersistenceService persistenceService = new UC12PersistenceService();
        UC12HotelState recoveredState = persistenceService.loadState();

        if (recoveredState == null) {
            List<UC12Reservation> freshHistory = new ArrayList<>();
            freshHistory.add(new UC12Reservation("Alice", "Single Room"));
            freshHistory.add(new UC12Reservation("Bob", "Double Room"));

            UC12HotelState newState = new UC12HotelState(
                    new UC12InventorySnapshot(4, 2),
                    freshHistory);

            System.out.println("Saving current inventory and booking history...");
            persistenceService.saveState(newState);
        } else {
            System.out.println("Recovered Inventory:");
            System.out.println("Single Room : " + recoveredState.getInventorySnapshot().getSingleRooms());
            System.out.println("Double Room : " + recoveredState.getInventorySnapshot().getDoubleRooms());

            System.out.println("\nRecovered Booking History:");
            for (UC12Reservation reservation : recoveredState.getBookingHistory()) {
                System.out.println(reservation.getGuestName() + " | " + reservation.getRoomType());
            }
        }
    }
}
