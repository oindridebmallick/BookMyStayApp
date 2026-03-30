import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class UC8Reservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;

    public UC8Reservation(String guestName, String roomType) {
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

class UC8BookingHistory {
    private final List<UC8Reservation> confirmedBookings = new ArrayList<>();

    public void recordBooking(UC8Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<UC8Reservation> getConfirmedBookings() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}

class UC8BookingReportService {
    private final UC8BookingHistory bookingHistory;

    public UC8BookingReportService(UC8BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void generateSummaryReport() {
        List<UC8Reservation> bookings = bookingHistory.getConfirmedBookings();

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Confirmed Bookings: " + bookings.size());
        for (int i = 0; i < bookings.size(); i++) {
            UC8Reservation reservation = bookings.get(i);
            System.out.println((i + 1) + ". " + reservation.getGuestName()
                    + " | Room: " + reservation.getRoomType()
                    + " | Reservation ID: " + reservation.getReservationId());
        }
        System.out.println("------------------------------");
    }
}

public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 8.0");
        System.out.println("=====================================\n");

        UC8BookingHistory bookingHistory = new UC8BookingHistory();

        bookingHistory.recordBooking(new UC8Reservation("Alice", "Single Room"));
        bookingHistory.recordBooking(new UC8Reservation("Bob", "Double Room"));
        bookingHistory.recordBooking(new UC8Reservation("Charlie", "Suite Room"));

        UC8BookingReportService reportService = new UC8BookingReportService(bookingHistory);
        reportService.generateSummaryReport();
    }
}
