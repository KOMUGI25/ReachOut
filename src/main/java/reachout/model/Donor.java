package reachout.model;

public class Donor extends Person {
    private int donorId;
    private String type;   // INDIVIDUAL / NGO / BUSINESS

    public Donor(int donorId, String name, String type, String contact, String location) {
        super(name, contact, location);
        this.donorId = donorId;
        this.type = type;
    }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return getName() + " (" + type + ") - " + getLocation();
    }
}