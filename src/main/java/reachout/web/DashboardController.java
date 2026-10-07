package reachout.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import reachout.dao.DonorDAO;
import reachout.dao.RequestDAO;
import reachout.dao.ResourceDAO;
import reachout.model.Request;
import reachout.model.Resource;

import java.sql.SQLException;
import java.util.List;

@Controller
public class DashboardController {

    private final DonorDAO donorDAO = new DonorDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            int donorCount = donorDAO.getAllDonors().size();

            List<Resource> resources = resourceDAO.getAllResources();
            long availableResources = resources.stream().filter(r -> "AVAILABLE".equals(r.getStatus())).count();

            List<Request> requests = requestDAO.getAllRequests();
            long pendingRequests = requests.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
            long delivered = requests.stream().filter(r -> "DELIVERED".equals(r.getStatus())).count();

            model.addAttribute("donorCount", donorCount);
            model.addAttribute("availableResources", availableResources);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("delivered", delivered);
            model.addAttribute("recentRequests", requests.size() > 5 ? requests.subList(0, 5) : requests);
        } catch (SQLException ex) {
            model.addAttribute("error", "Database error: " + ex.getMessage());
        }
        return "dashboard";
    }
}
