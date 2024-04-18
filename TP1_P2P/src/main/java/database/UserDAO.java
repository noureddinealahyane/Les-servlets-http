package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        this.connection = MySQLConnection.getInstance().getConnection();
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT IGNORE INTO user (username, winnings, status, role) VALUES (?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setDouble(2, user.getWinnings());
            statement.setString(3, user.getStatus().toString());
            statement.setString(4, user.getRole());
            statement.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String role= resultSet.getString("role");
                double winnings = resultSet.getDouble("winnings");
                User.Status status = User.Status.valueOf(resultSet.getString("status"));
                users.add(new User(username, winnings, status, role));
            }
            resultSet.close();
        }
        return users;
    }
    
    public User getUserByUsername(String username) throws SQLException {
        User user = null;
        String query = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double winnings = resultSet.getDouble("winnings");
                    User.Status status = User.Status.valueOf(resultSet.getString("status"));
                    String role = resultSet.getString("role");
                    user = new User(username, winnings, status, role);
                }
            }
        }
        return user;
    }
    
    public Boolean verifyRole(String username) {
    	try {
			User user = getUserByUsername(username);
			return user.getRole().toLowerCase().equals("tomcat");
    	} catch (SQLException e) {
			return false;
		}
    }
}
