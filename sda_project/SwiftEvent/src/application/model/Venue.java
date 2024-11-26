package application.model;

public class Venue {
    private int venueID;
    private String name;
    private String address;
    private int capacity;
    private int pricePerHour;
    private boolean availability;

    public Venue(int venueID, String name, String address, int capacity, int pricePerHour, boolean availability) {
        this.venueID = venueID;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.availability = availability;
    }

	// Getters and Setters
    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueID=" + venueID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", capacity=" + capacity +
                ", pricePerHour=" + pricePerHour +
                ", availability=" + availability +
                '}';
    }
}
