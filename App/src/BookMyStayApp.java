import java.util.LinkedList;
import java.util.Queue;

class BookingReservation {
    private String guestName;
    private String roomType;

    public BookingReservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}

class BookingRequestQueue {
    private Queue<BookingReservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(BookingReservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    public void displayQueue() {
        System.out.println("\nCurrent Booking Request Queue:");
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        int position = 1;

        for (BookingReservation reservation : requestQueue) {
            System.out.println("\nRequest Position: " + position);
            reservation.displayReservation();
            position++;
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("        Book My Stay App");
        System.out.println("   Hotel Booking Management System");
        System.out.println("           Version 5.1");
        System.out.println("=====================================\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        BookingReservation r1 = new BookingReservation("Alice", "Single Room");
        BookingReservation r2 = new BookingReservation("Bob", "Double Room");
        BookingReservation r3 = new BookingReservation("Charlie", "Suite Room");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        bookingQueue.displayQueue();

        System.out.println("\nRequests stored successfully. No rooms allocated yet.");
    }
}
