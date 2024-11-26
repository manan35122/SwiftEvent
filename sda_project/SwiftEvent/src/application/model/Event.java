package application.model;

public class Event {
    private int eventID;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private int maxCapacity;
    private int registeredAttendees;
    private EventOrganizer eventOrg; 
    private String venue;
    private int TicketPrice;
    
    
    
	public int getEventID() {
		return eventID;
	}
	public int getTicketPrice() {
		return TicketPrice;
	}
	public void setTicketPrice(int ticketPrice) {
		TicketPrice = ticketPrice;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getStatus() {
		return status;
	}
	public int getMaxCapacity() {
		return maxCapacity;
	}
	public int getRegisteredAttendees() {
		return registeredAttendees;
	}
	public EventOrganizer getEventOrg() {
		return eventOrg;
	}
	public String getVenue() {
		return venue;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	public void setRegisteredAttendees(int registeredAttendees) {
		this.registeredAttendees = registeredAttendees;
	}
	public void setEventOrg(EventOrganizer eventOrg) {
		this.eventOrg = eventOrg;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	@Override
	public String toString() {
	    return name + ", Description: " + description +", (Start: " + startDate + ", End: " + endDate + ", Ticket Price: RS" + TicketPrice + ")";
	}


        
    
}
