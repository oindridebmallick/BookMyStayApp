import java.util.HashMap;
import java.util.Map;

class UC9InvalidBookingException extends Exception {
    public UC9InvalidBookingException(String message) {
        super(message);
    }
}

class UC9InventoryService {
    private final Map<String, Integer> inventory = new HashMap<>();

    public UC9InventoryService() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class UC9BookingValidator {
    public static void validateBooking(String guestName, String roomType, UC9InventoryService inventoryService)
            throws UC9InvalidBookingException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new UC9InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new UC9InvalidBookingException("Room type cannot be empty.");
        }

        if (inventoryService.getAvailability(roomType) <= 0) {
            throw new UC9InvalidBookingException("No rooms available for room type: " + roomType);
        }
    }
}

public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 9.0");
        System.out.println("=====================================\n");

        UC9InventoryService inventoryService = new UC9InventoryService();
        inventoryService.displayInventory();
        System.out.println();

        String[][] testBookings = {
                {"Alice", "Single Room"},
                {"", "Double Room"},
                {"Charlie", "Suite Room"},
                {"David", "Penthouse"}
        };

        for (String[] booking : testBookings) {
            String guestName = booking[0];
            String roomType = booking[1];

            try {
                UC9BookingValidator.validateBooking(guestName, roomType, inventoryService);
                System.out.println("Booking validation passed for " + guestName + " requesting " + roomType);
            } catch (UC9InvalidBookingException exception) {
                System.out.println("Booking validation failed: " + exception.getMessage());
            }
        }
    }
}
