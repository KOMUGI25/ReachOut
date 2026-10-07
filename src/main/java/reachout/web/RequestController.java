package reachout.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import reachout.dao.RequestDAO;
import reachout.model.Request;
import reachout.service.MatchingService;
import reachout.service.ResourceNotFoundException;

import java.sql.SQLException;
import java.util.List;

/**
 * Replaces RequestPanel.java. Same RequestDAO.addRequest(...) followed by
 * MatchingService.matchRequest(...) sequence as the Swing "Raise & Match"
 * button.
 */
@Controller
public class RequestController {

    private final RequestDAO requestDAO = new RequestDAO();
    private final MatchingService matchingService = new MatchingService();

    @GetMapping("/requests")
    public String requestsPage(Model model) {
        try {
            List<Request> requests = requestDAO.getAllRequests();
            model.addAttribute("requests", requests);
        } catch (SQLException ex) {
            model.addAttribute("error", "Database error: " + ex.getMessage());
        }
        return "requests";
    }

    @PostMapping("/requests/add")
    public String raiseRequest(@RequestParam String requesterName,
                                @RequestParam String itemNeeded,
                                @RequestParam String location,
                                RedirectAttributes redirect) {
        if (requesterName.isBlank() || itemNeeded.isBlank() || location.isBlank()) {
            redirect.addFlashAttribute("error", "All fields are required.");
            return "redirect:/requests";
        }

        try {
            int requestId = requestDAO.addRequest(
                    new Request(0, requesterName.trim(), itemNeeded.trim(), location.trim(), "PENDING", null, null));

            Request newRequest = new Request(requestId, requesterName.trim(), itemNeeded.trim(), location.trim(), "PENDING", null, null);
            try {
                matchingService.matchRequest(newRequest);
                redirect.addFlashAttribute("message", "Request raised and matched successfully!");
            } catch (ResourceNotFoundException nfEx) {
                redirect.addFlashAttribute("error", "Request raised, but no match yet: " + nfEx.getMessage());
            }
        } catch (SQLException ex) {
            redirect.addFlashAttribute("error", "Database error: " + ex.getMessage());
        }
        return "redirect:/requests";
    }
}
