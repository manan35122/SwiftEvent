package application.model;

public class EventDetails {

    private String eventName;
    private int registeredAttendees;
    private double eventRating;

    public EventDetails(String eventName, int registeredAttendees, double eventRating) {
        this.eventName = eventName;
        this.registeredAttendees = registeredAttendees;
        this.eventRating = eventRating;
    }

    public String getEventName() {
        return eventName;
    }

    public int getRegisteredAttendees() {
        return registeredAttendees;
    }

    public double getEventRating() {
        return eventRating;
    }
}
