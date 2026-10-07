package reachout.service;

import reachout.dao.*;
import reachout.model.*;

import java.sql.SQLException;
import java.util.List;

public class MatchingService {

    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    /**
     * Finds an available resource matching the request's item (preferring same location),
     * assigns an available volunteer, and updates all three tables accordingly.
     * Throws ResourceNotFoundException if nothing suitable is available right now.
     */
    public void matchRequest(Request request) throws SQLException, ResourceNotFoundException {
        List<Resource> available = resourceDAO.getAvailableResources();

        // First pass: same item AND same location (the "nearest" match)
        Resource matched = available.stream()
                .filter(r -> r.getItemName().equalsIgnoreCase(request.getItemNeeded())
                        && r.getLocation().equalsIgnoreCase(request.getLocation()))
                .findFirst()
                .orElse(null);

        // Fallback: same item, any location
        if (matched == null) {
            matched = available.stream()
                    .filter(r -> r.getItemName().equalsIgnoreCase(request.getItemNeeded()))
                    .findFirst()
                    .orElse(null);
        }

        if (matched == null) {
            throw new ResourceNotFoundException("No available resource found for: " + request.getItemNeeded());
        }

        List<Volunteer> volunteers = volunteerDAO.getAvailableVolunteers();
        if (volunteers.isEmpty()) {
            throw new ResourceNotFoundException("No available volunteer to assign right now.");
        }
        Volunteer volunteer = volunteers.get(0);

        // Update all three: link the request, reserve the resource, mark the volunteer busy
        requestDAO.assignMatch(request.getRequestId(), matched.getResourceId(), volunteer.getVolunteerId());
        resourceDAO.updateResourceStatus(matched.getResourceId(), "RESERVED");
        volunteerDAO.setAvailability(volunteer.getVolunteerId(), false);
    }
}