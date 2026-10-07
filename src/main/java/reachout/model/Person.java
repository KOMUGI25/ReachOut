package reachout.model;

public abstract class Person {
    private String name;
    private String contact;
    private String location;

    public Person(String name, String contact, String location) {
        this.name = name;
        this.contact = contact;
        this.location = location;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}