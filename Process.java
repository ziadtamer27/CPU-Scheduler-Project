public class Process {
    // Basic information
    public int id;
    public String pid;
    public int arrivalTime;
    public int burstTime;
    public int priority;
    
    // Scheduling metrics
    public int remainingTime;
    public int completionTime;
    public int turnaroundTime;
    public int waitingTime;
    public int startTime = -1;
    
    // Status tracking
    public boolean completed = false;
    
    // Legacy field names (for backward compatibility)
    public int at;      // arrival time
    public int bt;      // burst time
    public int dt;      // departure time
    public int remaining_bt;
    public int burst;
    public int arrival;
    public int remaining;
    
    // Constructors for different scheduler types
    
    // Constructor for FCFS, SJF, Round Robin (int id variant)
    public Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.remaining = burstTime;
        
        // Legacy field mapping
        this.at = arrivalTime;
        this.bt = burstTime;
        this.remaining_bt = burstTime;
        this.arrival = arrivalTime;
        this.burst = burstTime;
    }
    
    // Constructor for Round Robin, SJF Live (String id variant)
    public Process(String pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.remaining = burstTime;
        
        // Legacy field mapping
        this.at = arrivalTime;
        this.bt = burstTime;
        this.remaining_bt = burstTime;
        this.arrival = arrivalTime;
        this.burst = burstTime;
    }
    
    // Constructor for Priority Scheduler
    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.remaining = burstTime;
        
        // Legacy field mapping
        this.at = arrivalTime;
        this.bt = burstTime;
        this.remaining_bt = burstTime;
        this.arrival = arrivalTime;
        this.burst = burstTime;
    }
}
