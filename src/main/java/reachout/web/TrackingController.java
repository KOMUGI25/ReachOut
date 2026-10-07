package reachout.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import reachout.dao.RequestDAO;
import reachout.dao.ResourceDAO;
import reachout.dao.VolunteerDAO;
import reachout.model.Request;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Replaces TrackingPanel.java. Same STATUS_FLOW array and advanceStatus(...)
 * logic as the Swing version, including the guard against advancing a
 * request that has no matched resource/volunteer yet, and the +1 credit
 * awarded to the volunteer on DELIVERED.
 */
@Controller
public class TrackingController {

    private static final String[] STATUS_FLOW = {"PENDING", "MATCHED", "ASSIGNED", "IN_TRANSIT", "DELIVERED"};

    private final RequestDAO requestDAO = new RequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final VolunteerDAO volunteerDAO = new VolunteerDAO();

    @GetMapping("/tracking")
    public String trackingPage(Model model) {
        try {
            List<Request> requests = requestDAO.getAllRequests();
            long delivered = requests.stream().filter(r -> "DELIVERED".equals(r.getStatus())).count();
            model.addAttribute("requests", requests);
            model.addAttribute("total", requests.size());
            model.addAttribute("delivered", delivered);
        } catch (SQLException ex) {
            model.addAttribute("error", "Database error: " + ex.getMessage());
        }
        return "tracking";
    }

    @PostMapping("/tracking/advance")
    public String advance(@RequestParam int requestId, RedirectAttributes redirect) {
        try {
            List<Request> all = requestDAO.getAllRequests();
            Request target = all.stream().filter(r -> r.getRequestId() == requestId).findFirst().orElse(null);

            if (target == null) {
                redirect.addFlashAttribute("error", "No request found with that ID.");
                return "redirect:/tracking";
            }

            if (target.getMatchedResourceId() == null || target.getAssignedVolunteerId() == null) {
                redirect.addFlashAttribute("error",
                        "This request has no matched resource/volunteer yet, so it can't be tracked. " +
                        "Try raising a request for an item that currently has an AVAILABLE resource.");
                return "redirect:/tracking";
            }

            int currentIndex = Arrays.asList(STATUS_FLOW).indexOf(target.getStatus());
            if (currentIndex < 0 || currentIndex >= STATUS_FLOW.length - 1) {
                redirect.addFlashAttribute("message", "This request is already at its final status.");
                return "redirect:/tracking";
            }

            String nextStatus = STATUS_FLOW[currentIndex + 1];
            requestDAO.updateStatus(requestId, nextStatus);

            if (nextStatus.equals("DELIVERED")) {
                resourceDAO.updateResourceStatus(target.getMatchedResourceId(), "DELIVERED");
                volunteerDAO.addCredits(target.getAssignedVolunteerId(), 1);
                volunteerDAO.setAvailability(target.getAssignedVolunteerId(), true);
                redirect.addFlashAttribute("message", "Delivered! Volunteer credited +1 point.");
            } else {
                redirect.addFlashAttribute("message", "Request #" + requestId + " advanced to " + nextStatus + ".");
            }
        } catch (SQLException ex) {
            redirect.addFlashAttribute("error", "Database error: " + ex.getMessage());
        }
        return "redirect:/tracking";
    }
}
