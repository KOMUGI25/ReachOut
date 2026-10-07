package reachout.model;

public class Volunteer extends Person {
    private int volunteerId;
    private boolean available;
    private int credits;

    public Volunteer(int volunteerId, String name, String contact, String location, boolean available, int credits) {
        super(name, contact, location);
        this.volunteerId = volunteerId;
        this.available = available;
        this.credits = credits;
    }

    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    @Override
    public String toString() {
        return getName() + " - " + getLocation() + (available ? " (Available)" : " (Busy)");
    }
}