package reachout.model;

public class Request {
    private int requestId;
    private String requesterName;
    private String itemNeeded;
    private String location;
    private String status;   // PENDING/MATCHED/ASSIGNED/IN_TRANSIT/DELIVERED
    private Integer matchedResourceId;
    private Integer assignedVolunteerId;

    public Request(int requestId, String requesterName, String itemNeeded, String location,
                   String status, Integer matchedResourceId, Integer assignedVolunteerId) {
        this.requestId = requestId;
        this.requesterName = requesterName;
        this.itemNeeded = itemNeeded;
        this.location = location;
        this.status = status;
        this.matchedResourceId = matchedResourceId;
        this.assignedVolunteerId = assignedVolunteerId;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }

    public String getItemNeeded() { return itemNeeded; }
    public void setItemNeeded(String itemNeeded) { this.itemNeeded = itemNeeded; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getMatchedResourceId() { return matchedResourceId; }
    public void setMatchedResourceId(Integer matchedResourceId) { this.matchedResourceId = matchedResourceId; }

    public Integer getAssignedVolunteerId() { return assignedVolunteerId; }
    public void setAssignedVolunteerId(Integer assignedVolunteerId) { this.assignedVolunteerId = assignedVolunteerId; }
}