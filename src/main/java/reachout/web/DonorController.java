package reachout.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import reachout.dao.DonorDAO;
import reachout.dao.ResourceDAO;
import reachout.model.Donor;
import reachout.model.Resource;

import java.sql.SQLException;
import java.util.List;

/**
 * Replaces DonorPanel.java. Same DonorDAO / ResourceDAO calls as the Swing
 * version; the two sections (Donors, Resources) that used to be stacked in
 * one JPanel are now two cards on one page.
 */
@Controller
public class DonorController {

    private final DonorDAO donorDAO = new DonorDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();

    @GetMapping("/donors")
    public String donorsPage(Model model) {
        try {
            List<Donor> donors = donorDAO.getAllDonors();
            List<Resource> resources = resourceDAO.getAllResources();
            model.addAttribute("donors", donors);
            model.addAttribute("resources", resources);
            model.addAttribute("donorTypes", new String[]{"INDIVIDUAL", "NGO", "BUSINESS"});
        } catch (SQLException ex) {
            model.addAttribute("error", "Database error: " + ex.getMessage());
        }
        return "donors";
    }

    // Equivalent to DonorPanel's "Add Donor" button handler
    @PostMapping("/donors/add")
    public String addDonor(@RequestParam String name,
                            @RequestParam String type,
                            @RequestParam(required = false) String contact,
                            @RequestParam String location,
                            RedirectAttributes redirect) {
        if (name.isBlank() || location.isBlank()) {
            redirect.addFlashAttribute("error", "Name and Location are required.");
            return "redirect:/donors";
        }
        try {
            donorDAO.addDonor(new Donor(0, name.trim(), type, contact == null ? "" : contact.trim(), location.trim()));
            redirect.addFlashAttribute("message", "Donor added successfully!");
        } catch (SQLException ex) {
            redirect.addFlashAttribute("error", "Database error: " + ex.getMessage());
        }
        return "redirect:/donors";
    }

    // Equivalent to DonorPanel's "Add Resource" button handler
    @PostMapping("/resources/add")
    public String addResource(@RequestParam int donorId,
                               @RequestParam String itemName,
                               @RequestParam String quantity,
                               @RequestParam String location,
                               RedirectAttributes redirect) {
        int qty;
        try {
            qty = Integer.parseInt(quantity.trim());
        } catch (NumberFormatException ex) {
            redirect.addFlashAttribute("error", "Quantity must be a number.");
            return "redirect:/donors";
        }

        if (itemName.isBlank() || location.isBlank() || qty <= 0) {
            redirect.addFlashAttribute("error", "Enter a valid item, quantity (>0), and location.");
            return "redirect:/donors";
        }

        try {
            resourceDAO.addResource(new Resource(0, donorId, itemName.trim(), qty, location.trim(), "AVAILABLE"));
            redirect.addFlashAttribute("message", "Resource added successfully!");
        } catch (SQLException ex) {
            redirect.addFlashAttribute("error", "Database error: " + ex.getMessage());
        }
        return "redirect:/donors";
    }
}
