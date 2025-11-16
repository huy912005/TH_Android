package model;

public class Building {
    private int id;
    private String name;
    private String street;
    private String ward;
    private int numberOfBasement;
    private Long districtId;

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }

    public int getNumberOfBasement() { return numberOfBasement; }
    public void setNumberOfBasement(int numberOfBasement) { this.numberOfBasement = numberOfBasement; }
    public Long getDistrictId() {
        return districtId;
    }
    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
}
