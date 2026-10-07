package reachout.model;

public class Resource {
    private int resourceId;
    private int donorId;
    private String itemName;
    private int quantity;
    private String location;
    private String status;   // AVAILABLE / RESERVED / DELIVERED

    public Resource(int resourceId, int donorId, String itemName, int quantity, String location, String status) {
        this.resourceId = resourceId;
        this.donorId = donorId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.location = location;
        this.status = status;
    }

    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return itemName + " x" + quantity + " @ " + location + " [" + status + "]";
    }
}