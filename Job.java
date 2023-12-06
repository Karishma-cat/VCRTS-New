public class Job {
    private final int clientID;
    private final int jobID;
    private final int duration;
    private final String deadline;
    private final int completionTime;

    public Job(int clientID, int jobID, int duration, String deadline, int completionTime) {
        this.clientID = clientID;
        this.jobID = jobID;
        this.duration = duration;
        this.deadline = deadline;
        this.completionTime = completionTime;
    }

    // Getters for your fields if needed

    public int getClientID() {
        return clientID;
    }

    public int getJobID() {
        return jobID;
    }

    public int getDuration() {
        return duration;
    }

    public String getDeadline() {
        return deadline;
    }

    public int getCompletionTime() {
        return completionTime;
    }
}
