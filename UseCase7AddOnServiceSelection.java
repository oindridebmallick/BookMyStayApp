import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class UC7Reservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;

    public UC7Reservation(String guestName, String roomType) {
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
}

class UC7AddOnService {
    private final String serviceName;
    private final double price;

    public UC7AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}

class UC7AddOnServiceManager {
    private final Map<String, List<UC7AddOnService>> reservationServices = new HashMap<>();

    public void addService(String reservationId, UC7AddOnService service) {
        reservationServices.computeIfAbsent(reservationId, key -> new ArrayList<>()).add(service);
    }

    public double calculateAdditionalCost(String reservationId) {
        double total = 0.0;
        for (UC7AddOnService service : reservationServices.getOrDefault(reservationId, new ArrayList<>())) {
            total += service.getPrice();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        List<UC7AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());

        if (services.isEmpty()) {
            System.out.println("No add-on services selected for reservation " + reservationId);
            return;
        }

        System.out.println("Selected Add-On Services for Reservation " + reservationId + ":");
        for (UC7AddOnService service : services) {
            System.out.println("- " + service.getServiceName() + " : $" + service.getPrice());
        }
        System.out.println("Total Additional Cost: $" + calculateAdditionalCost(reservationId));
    }
}

public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 7.0");
        System.out.println("=====================================\n");

        UC7Reservation reservation = new UC7Reservation("Alice", "Single Room");
        UC7AddOnServiceManager addOnManager = new UC7AddOnServiceManager();

        addOnManager.addService(reservation.getReservationId(), new UC7AddOnService("Breakfast Buffet", 25.0));
        addOnManager.addService(reservation.getReservationId(), new UC7AddOnService("Airport Pickup", 40.0));
        addOnManager.addService(reservation.getReservationId(), new UC7AddOnService("Spa Access", 50.0));

        System.out.println("Guest Name: " + reservation.getGuestName());
        System.out.println("Room Type: " + reservation.getRoomType());
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println();

        addOnManager.displayServices(reservation.getReservationId());
    }
}
