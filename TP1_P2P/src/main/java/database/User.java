package database;

public class User {
    private String username;
    private double winnings;
    private Status status;
    private String role;

    public User(String username, double winnings, Status status, String role) {
        this.username = username;
        this.winnings = winnings;
        this.status = status;
        this.role = role;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getWinnings() {
        return winnings;
    }

    public void setWinnings(double winnings) {
        this.winnings = winnings;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Enum for status
    public enum Status {
        BLACKLISTED,
        NOT_BLACKLISTED
    }
}
