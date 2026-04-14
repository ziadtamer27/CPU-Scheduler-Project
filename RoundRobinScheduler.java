import java.util.*;

public class RoundRobinScheduler {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Arrival Time for P" + (i+1) + ": ");
            int at = sc.nextInt();

            System.out.print("Burst Time for P" + (i+1) + ": ");
            int bt = sc.nextInt();

            processes.add(new Process("P" + (i+1), at, bt));
        }

        System.out.print("Enter Time Quantum: ");
        int quantum = sc.nextInt();

        roundRobin(processes, quantum);
    }

    public static void roundRobin(List<Process> processes, int quantum) {

    Queue<Process> queue = new LinkedList<>();
    int time = 0;
    int completed = 0;
    int n = processes.size();

    // Sort by arrival time
    processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

    int i = 0;

    while (completed < n) {

        // Add arrived processes to queue
        while (i < n && processes.get(i).arrivalTime <= time) {
            queue.add(processes.get(i));
            i++;
        }

        if (queue.isEmpty()) {
            time++;
            continue;
        }

        Process current = queue.poll();

        int executeTime = Math.min(current.remainingTime, quantum);

        System.out.println("Time " + time + " → " + (time + executeTime) + ": " + current.pid);
        
        time += executeTime;
        current.remainingTime -= executeTime;
        printRemainingTable(processes, time);

        // Add newly arrived processes during execution
        while (i < n && processes.get(i).arrivalTime <= time) {
            queue.add(processes.get(i));
            i++;
        }

        if (current.remainingTime > 0) {
            queue.add(current);
        } else {
            current.completionTime = time;
            current.turnaroundTime = current.completionTime - current.arrivalTime;
            current.waitingTime = current.turnaroundTime - current.burstTime;
            completed++;
        }
    }

    printResults(processes);
}


    public static void printResults(List<Process> processes) {

        double totalWT = 0, totalTAT = 0;

        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        System.out.println("\nAverage Waiting Time: " + (totalWT / processes.size()));
        System.out.println("Average Turnaround Time: " + (totalTAT / processes.size()));
    }

    public static void printRemainingTable(List<Process> processes, int time) {

    System.out.println("\nTime = " + time);
    System.out.println("---------------------------------");
    System.out.printf("%-10s %-10s\n", "Process", "Remaining BT");

    for (Process p : processes) {
        System.out.printf("%-10s %-10d\n", p.pid, p.remainingTime);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    System.out.println("---------------------------------");
}

}

