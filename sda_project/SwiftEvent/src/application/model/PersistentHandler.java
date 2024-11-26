package application.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersistentHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/swift_event_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "manan35122";
    private static Connection connection;


    public static Connection getConnection() {

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                System.out.println("Database connection established successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to connect to the database!");
            }
        }
        return connection;
    }

    public String validateLogin(String username, String password) {
        String userQuery = "SELECT UserID, UserType FROM User WHERE Name = ? AND Password = ?";
        String adminQuery = "SELECT AdminID FROM Admin WHERE Name = ? AND Password = ?";

        try (PreparedStatement userStatement = getConnection().prepareStatement(userQuery);
             PreparedStatement adminStatement = getConnection().prepareStatement(adminQuery)) {
            userStatement.setString(1, username);
            userStatement.setString(2, password);
            ResultSet userResultSet = userStatement.executeQuery();

            if (userResultSet.next()) {
                int userID = userResultSet.getInt("UserID");
                String userType = userResultSet.getString("UserType");
                SessionManager.getInstance().setLoggedInUser(userID, userType);
                return userType; 
            }
            adminStatement.setString(1, username);
            adminStatement.setString(2, password);
            ResultSet adminResultSet = adminStatement.executeQuery();

            if (adminResultSet.next()) {
                int adminID = adminResultSet.getInt("AdminID");
                SessionManager.getInstance().setLoggedInUser(adminID, "Admin");
                return "Admin";
            }

            return null; 
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }
    public boolean saveNewUser(String name, String email, String password, String address, String phoneNumber, String userType) {
        String userQuery = "INSERT INTO User (Name, Email, Password, Address, PhoneNumber, UserType) VALUES (?, ?, ?, ?, ?, ?)";
        String organizerQuery = "INSERT INTO EventOrganizer (OrganizerID, Name) VALUES (?, ?)";
        String memberQuery = "INSERT INTO Attendee (AttendeeID, Name) VALUES (?, ?)";
        try (PreparedStatement userStatement = getConnection().prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            userStatement.setString(1, name);
            userStatement.setString(2, email);
            userStatement.setString(3, password);
            userStatement.setString(4, address);
            userStatement.setString(5, phoneNumber);
            userStatement.setString(6, userType);

            int rowsInserted = userStatement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);  
                    if ("Event Organizer".equalsIgnoreCase(userType)) {
                        try (PreparedStatement organizerStatement = getConnection().prepareStatement(organizerQuery)) {
                            organizerStatement.setInt(1, userId);  
                            organizerStatement.setString(2, name);  

                            int organizerRowsInserted = organizerStatement.executeUpdate();
                            return organizerRowsInserted > 0;  
                        }
                    }
                    if ("member".equalsIgnoreCase(userType)) {
                        try (PreparedStatement memberStatement = getConnection().prepareStatement(memberQuery)) {
                            memberStatement.setInt(1, userId);  
                            memberStatement.setString(2, name); 

                            int organizerRowsInserted = memberStatement.executeUpdate();
                            return organizerRowsInserted > 0;  
                        }
                    }

                    return true;
                }
            }

            return false; 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveEvent(Event event, int organizerID) {
        String query = "INSERT INTO Events (Name, Description, StartDate, EndDate, Status, MaxCapacity, OrganizerID, Venue, TicketPrice) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, event.getName());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getStartDate());
            statement.setString(4, event.getEndDate());
            statement.setString(5, event.getStatus());
            statement.setInt(6, event.getMaxCapacity());
            statement.setInt(7, organizerID);
            statement.setString(8, event.getVenue());
            statement.setInt(9, event.getTicketPrice());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        event.setEventID(generatedKeys.getInt(1)); 
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to close the database connection.");
            }
        }
    }
    
    public ObservableList<Vendor> getAllVendors() {
        String query = "SELECT * FROM Vendor WHERE availability = true";
        ObservableList<Vendor> vendors = FXCollections.observableArrayList();

        try (PreparedStatement statement = getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Vendor vendor = new Vendor();
                vendor.setVendorID(resultSet.getInt("vendorID"));
                vendor.setName(resultSet.getString("name"));
                vendor.setServiceType(resultSet.getString("serviceType"));
                vendor.setContactInfo(resultSet.getString("contactInfo"));
                vendor.setRating(resultSet.getFloat("rating"));
                vendor.setPricing(resultSet.getDouble("pricing"));
                vendor.setAvailability(resultSet.getBoolean("availability"));

                vendors.add(vendor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vendors;
    }
    
    public boolean assignVendorsToEvent(int eventID, int organizerID, int vendorID, String vendorName) {
        String query = "INSERT INTO Event_Vendor_Assignment (eventID, organizerID, vendorID, vendorName) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, eventID);
            stmt.setInt(2, organizerID);
            stmt.setInt(3, vendorID);
            stmt.setString(4, vendorName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                System.out.println("No rows inserted for vendor " + vendorName); 
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error in assignVendorsToEvent: " + e.getMessage()); 
            return false;
        }
    }
    
    public List<Event> getEventsByOrganizer(int organizerID) {
        List<Event> events = new ArrayList<>();

        String query = "SELECT * FROM Events WHERE OrganizerID = ? And Status = 'Scheduled'";

        try (PreparedStatement statement = getConnection().prepareStatement(query)) {

            statement.setInt(1, organizerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Event event = new Event();
                    event.setEventID(resultSet.getInt("EventID"));
                    event.setName(resultSet.getString("Name"));
                    event.setDescription(resultSet.getString("Description"));
                    event.setStartDate(resultSet.getString("StartDate"));
                    event.setEndDate(resultSet.getString("EndDate"));
                    event.setStatus(resultSet.getString("Status"));
                    event.setMaxCapacity(resultSet.getInt("MaxCapacity"));
                    event.setVenue(resultSet.getString("Venue"));
                    event.setTicketPrice(resultSet.getInt("TicketPrice"));
                    events.add(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }
    
    public boolean cancelEvent(int eventID) {
        String query = "UPDATE Events SET Status = 'Canceled' WHERE EventID = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, eventID);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Venue> getAllVenues() {
        List<Venue> venues = new ArrayList<>();
        String query = "SELECT * FROM Venue WHERE availability = TRUE";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int venueID = rs.getInt("venueID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                int capacity = rs.getInt("capacity");
                int pricePerHour = rs.getInt("pricePerHour");
                boolean availability = rs.getBoolean("availability");

                Venue venue = new Venue(venueID, name, address, capacity, pricePerHour, availability);
                venues.add(venue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return venues;
    }
    
    public boolean saveVenueForEvent(int organizerID, int eventID, int venueID, String venueName) {
        String query = "INSERT INTO Event_Venue_Assignment (organizerID, eventID, venueID, venueName) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, organizerID);
            stmt.setInt(2, eventID);
            stmt.setInt(3, venueID);
            stmt.setString(4, venueName);

            stmt.executeUpdate();
            return true; 
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }
    
    public List<Event> getAllEvents(int userID) {
        List<Event> events = new ArrayList<>();
        String sql = """
        	    SELECT * 
        	    FROM events e
        	    WHERE e.status = 'Scheduled'
        	      AND e.eventID NOT IN (
        	          SELECT ue.eventID 
        	          FROM registrations ue
        	          WHERE ue.userID = ?
        	      )
        	""";

        	try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        	    preparedStatement.setInt(1, userID);

        	    try (ResultSet resultSet = preparedStatement.executeQuery()) {
        	        while (resultSet.next()) {
        	            Event event = new Event();
        	            event.setEventID(resultSet.getInt("eventID"));
        	            event.setName(resultSet.getString("Name"));
        	            event.setDescription(resultSet.getString("Description"));
        	            event.setStartDate(resultSet.getString("StartDate"));
        	            event.setEndDate(resultSet.getString("EndDate"));
        	            event.setStatus(resultSet.getString("Status"));
        	            event.setMaxCapacity(resultSet.getInt("MaxCapacity"));
        	            event.setVenue(resultSet.getString("Venue"));
        	            event.setTicketPrice(resultSet.getInt("TicketPrice"));  
        	            events.add(event);
        	        }
        	    }
        	} catch (SQLException e) {
        	    e.printStackTrace();
        	}

        return events;
    }

    public boolean registerUserForEvent(int userID, int eventID) {
        String sql = "INSERT INTO registrations (userID, eventID) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userID);  
            statement.setInt(2, eventID);  
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  
        }
    }
    public List<Event> getRegisteredEvents(int userID) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT e.* " +
                "FROM events e " +
                "JOIN registrations r ON e.eventID = r.eventID " +
                "JOIN user u ON r.userID = u.userID " +
                "WHERE u.userID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Event event = new Event();
                    event.setEventID(resultSet.getInt("eventID"));
                    event.setName(resultSet.getString("Name"));
                    event.setDescription(resultSet.getString("Description"));
                    event.setStartDate(resultSet.getString("StartDate"));
                    event.setEndDate(resultSet.getString("EndDate"));
                    event.setStatus(resultSet.getString("Status"));
                    event.setMaxCapacity(resultSet.getInt("MaxCapacity"));
                    event.setVenue(resultSet.getString("Venue"));
                    event.setTicketPrice(resultSet.getInt("TicketPrice"));
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    public void insertFeedback(int eventID, Double rating, String comment) {
        String sql = "INSERT INTO feedback (eventID, rating, comment) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, eventID);
            preparedStatement.setDouble(2, rating); 
            preparedStatement.setString(3, comment);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public EventDetails getEventDetails(int eventID) {
        String query = "SELECT E.Name AS EventName, " +
                        "COUNT(R.registrationID) AS RegisteredAttendees, " +
                        "AVG(F.rating) AS EventRating " +
                        "FROM Events E " +
                        "LEFT JOIN registrations R ON E.EventID = R.eventID " +
                        "LEFT JOIN feedback F ON E.EventID = F.eventID " +
                        "WHERE E.EventID = ? " +
                        "GROUP BY E.EventID";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eventID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String eventName = resultSet.getString("EventName");
                int registeredAttendees = resultSet.getInt("RegisteredAttendees");
                double eventRating = resultSet.getDouble("EventRating");

                return new EventDetails(eventName, registeredAttendees, eventRating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean generateEventReport(Event event) {
        System.out.println("Generating report for event: " + event.getName());
        return true;
    }

}
