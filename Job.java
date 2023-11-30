public class Job {
    private int jobId;
    private Client requester;
    private boolean completed;

    public Job(int jobId, Client requester, boolean completed) {
        this.jobId = jobId;
        this.requester = requester;
        this.completed = completed;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public Client getRequester() {
        return requester;
    }

    public void setRequester(Client requester) {
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
