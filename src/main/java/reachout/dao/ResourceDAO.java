package reachout.dao;

import reachout.model.Resource;
import reachout.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    public int addResource(Resource r) throws SQLException {
        String sql = "INSERT INTO resources (donor_id, item_name, quantity, location, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, r.getDonorId());
            stmt.setString(2, r.getItemName());
            stmt.setInt(3, r.getQuantity());
            stmt.setString(4, r.getLocation());
            stmt.setString(5, r.getStatus());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public List<Resource> getAllResources() throws SQLException {
        List<Resource> list = new ArrayList<>();
        String sql = "SELECT * FROM resources";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Resource(
                        rs.getInt("resource_id"), rs.getInt("donor_id"),
                        rs.getString("item_name"), rs.getInt("quantity"),
                        rs.getString("location"), rs.getString("status")
                ));
            }
        }
        return list;
    }

    // Only resources still marked AVAILABLE — this is what MatchingService will search
    public List<Resource> getAvailableResources() throws SQLException {
        List<Resource> list = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE status = 'AVAILABLE'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Resource(
                        rs.getInt("resource_id"), rs.getInt("donor_id"),
                        rs.getString("item_name"), rs.getInt("quantity"),
                        rs.getString("location"), rs.getString("status")
                ));
            }
        }
        return list;
    }

    public boolean updateResourceStatus(int resourceId, String status) throws SQLException {
        String sql = "UPDATE resources SET status=? WHERE resource_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, resourceId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteResource(int resourceId) throws SQLException {
        String sql = "DELETE FROM resources WHERE resource_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resourceId);
            return stmt.executeUpdate() > 0;
        }
    }
}