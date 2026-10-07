package reachout.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Simple session-based gatekeeper: any page other than /login requires a
 * logged-in user in the session. This replaces the old flow where closing
 * LoginPanel and opening MainFrame was itself the "you're logged in" check.
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("username") != null;

        if (!loggedIn) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
