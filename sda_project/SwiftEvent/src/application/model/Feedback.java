package application.model;

public class Feedback {
    private int eventID; 
    private int rating;  
    private String comment; 

    public Feedback(int eventID, int rating, String comment) {
        this.eventID = eventID;
        this.rating = rating;
        this.comment = comment;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}