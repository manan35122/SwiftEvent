package application.model;

public class SessionManager {
    private static SessionManager instance;

    private static int loggedInUserID;
    private String loggedInUserType;
    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setLoggedInUser(int userID, String userType) {
        SessionManager.loggedInUserID = userID;
        this.loggedInUserType = userType;
    }

    public static int getLoggedInUserID() {
        return loggedInUserID;
    }

    public String getLoggedInUserType() {
        return loggedInUserType;
    }

    public boolean isEventOrganizer() {
        return "Event Organizer".equalsIgnoreCase(loggedInUserType);
    }
}
