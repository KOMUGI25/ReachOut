package reachout.dao;

import reachout.model.Volunteer;
import reachout.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {

    public int addVolunteer(Volunteer v) throws SQLException {
        String sql = "INSERT INTO volunteers (name, contact, location, available, credits) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, v.getName());
            stmt.setString(2, v.getContact());
            stmt.setString(3, v.getLocation());
            stmt.setBoolean(4, v.isAvailable());
            stmt.setInt(5, v.getCredits());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public List<Volunteer> getAvailableVolunteers() throws SQLException {
        List<Volunteer> list = new ArrayList<>();
        String sql = "SELECT * FROM volunteers WHERE available = TRUE";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Volunteer(
                        rs.getInt("volunteer_id"), rs.getString("name"), rs.getString("contact"),
                        rs.getString("location"), rs.getBoolean("available"), rs.getInt("credits")
                ));
            }
        }
        return list;
    }

    public boolean setAvailability(int volunteerId, boolean available) throws SQLException {
        String sql = "UPDATE volunteers SET available=? WHERE volunteer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, volunteerId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Adds delta credits to a volunteer — called when a delivery completes
    public boolean addCredits(int volunteerId, int delta) throws SQLException {
        String sql = "UPDATE volunteers SET credits = credits + ? WHERE volunteer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, delta);
            stmt.setInt(2, volunteerId);
            return stmt.executeUpdate() > 0;
        }
    }
}