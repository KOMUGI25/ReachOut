package reachout.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reachout.dao.UserDAO;
import reachout.model.User;

import java.sql.SQLException;

/**
 * Replaces LoginPanel.java. Same UserDAO.validateLogin(username, password)
 * call as the Swing version -- just renders an HTML page instead of a
 * JFrame, and stores the logged-in user in the HTTP session instead of
 * closing one window and opening another.
 */
@Controller
public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                           @RequestParam String password,
                           HttpServletRequest request,
                           Model model) {
        try {
            User user = userDAO.validateLogin(username, password);
            if (user == null) {
                model.addAttribute("error", "Invalid username or password.");
                return "login";
            }
            // Same idea as LoginPanel's dispose() + new MainFrame(): the
            // session now remembers who's logged in for every later page.
            request.getSession(true).setAttribute("username", user.getUsername());
            request.getSession().setAttribute("role", user.getRole());
            request.getSession().setAttribute("userId", user.getUserId());

            return "redirect:/dashboard";
        } catch (SQLException ex) {
            model.addAttribute("error", "Database error: " + ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
