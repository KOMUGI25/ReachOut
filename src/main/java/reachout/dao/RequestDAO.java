package reachout.dao;

import reachout.model.Request;
import reachout.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public int addRequest(Request r) throws SQLException {
        String sql = "INSERT INTO requests (requester_name, item_needed, location, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, r.getRequesterName());
            stmt.setString(2, r.getItemNeeded());
            stmt.setString(3, r.getLocation());
            stmt.setString(4, r.getStatus());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public List<Request> getAllRequests() throws SQLException {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT * FROM requests ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Request(
                        rs.getInt("request_id"), rs.getString("requester_name"),
                        rs.getString("item_needed"), rs.getString("location"),
                        rs.getString("status"),
                        (Integer) rs.getObject("matched_resource_id"),
                        (Integer) rs.getObject("assigned_volunteer_id")
                ));
            }
        }
        return list;
    }

    // Called by MatchingService once it finds a resource + volunteer
    public boolean assignMatch(int requestId, int resourceId, int volunteerId) throws SQLException {
        String sql = "UPDATE requests SET matched_resource_id=?, assigned_volunteer_id=?, status='ASSIGNED' WHERE request_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resourceId);
            stmt.setInt(2, volunteerId);
            stmt.setInt(3, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE requests SET status=? WHERE request_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        }
    }
}