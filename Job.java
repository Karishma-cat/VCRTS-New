public class Job {
    private int jobId;
    private Client requester;
    private boolean completed;
    private final int duration;
    private final String deadline;
    private final int completionTime;
   

    public Job(int jobId, Client requester, boolean completed, int duration, String deadline, int completionTime) {
        this.jobId = jobId;
        this.requester = requester;
        this.completed = completed;
        this.duration = duration;
        this.deadline = deadline;
        this.completionTime = completionTime;
    }

   

    public int getJobId() {
        return jobId;
    }

    public int getDuration() {
		return duration;
	}

    public String getDeadline() {
		return this.deadline;
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
    public int getCompletionTime() {
		return this.completionTime;
	}

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    //Add functionality
    public void start() {}

    //Added functionality
    //public void transferToServer(Server s) {}

    
}
