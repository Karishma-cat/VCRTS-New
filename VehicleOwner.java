public class VehicleOwner {
    private int ownerId;
    private String name;

    public VehicleOwner(int ownerId, String name) {
        this.ownerId = ownerId;
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    //Add functionality to this
    public void rentVehicle(Vehicle v) {}
    
}
