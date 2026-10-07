package reachout.dao;

import reachout.model.Donor;
import reachout.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    // CREATE — add a new donor, returns the generated donor_id
    public int addDonor(Donor donor) throws SQLException {
        String sql = "INSERT INTO donors (name, type, contact, location) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, donor.getName());
            stmt.setString(2, donor.getType());
            stmt.setString(3, donor.getContact());
            stmt.setString(4, donor.getLocation());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    // READ — get every donor
    public List<Donor> getAllDonors() throws SQLException {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT * FROM donors";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                donors.add(new Donor(
                        rs.getInt("donor_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("contact"),
                        rs.getString("location")
                ));
            }
        }
        return donors;
    }

    // UPDATE — change an existing donor's details
    public boolean updateDonor(Donor donor) throws SQLException {
        String sql = "UPDATE donors SET name=?, type=?, contact=?, location=? WHERE donor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, donor.getName());
            stmt.setString(2, donor.getType());
            stmt.setString(3, donor.getContact());
            stmt.setString(4, donor.getLocation());
            stmt.setInt(5, donor.getDonorId());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE — remove a donor by id
    public boolean deleteDonor(int donorId) throws SQLException {
        String sql = "DELETE FROM donors WHERE donor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, donorId);
            return stmt.executeUpdate() > 0;
        }
    }
}