package application.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String userType; 
    private String address;
    private String phoneNumber;

    // Constructor
    public User(int id, String name, String email, String password, String userType, String address, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Common Methods
    public String getProfileDetails() {
        return "ID: " + id + "\nName: " + name + "\nEmail: " + email + "\nType: " + userType +
               "\nAddress: " + address + "\nPhone: " + phoneNumber;
    }

    public void updateProfile(String name, String email, String address, String phoneNumber) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (address != null) this.address = address;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", userType='" + userType + '\'' +
               '}';
    }

    
}
