package reachout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the ReachOut web application.
 *
 * This REPLACES the old Main.java that used to launch the Swing LoginPanel.
 * Running this class starts an embedded web server (Tomcat) on port 8080.
 *
 * After running it, open a browser to: http://localhost:8080/login
 *
 * Your existing reachout.model and reachout.dao packages (Person, Donor,
 * Volunteer, Resource, Request, User, and all the DAO classes) do NOT need
 * to change at all -- the web controllers in reachout.web call them exactly
 * the way your Swing panels used to.
 */
@SpringBootApplication
public class ReachOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReachOutApplication.class, args);
    }
}
