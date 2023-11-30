public class Vehicle {
	private int vehicleId;
    private VehicleOwner requester;
    private boolean completed;

    public Vehicle(int ownerId, VehicleOwner requester, boolean completed) {
        this.vehicleId = ownerId;
        this.requester = requester;
        this.completed = completed;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public VehicleOwner getRequester() {
        return requester;
    }

    public void setRequester(VehicleOwner requester) {
        this.requester = requester;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    //Add functionality
    public void start() {}

    //Added functionality
    public void transferToServer(Server s) {}


}
