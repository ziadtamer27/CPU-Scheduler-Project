import java.util.*;

class Process {
    int id, arrival, burst, remaining, completion;

    Process(int id, int arrival, int burst) {
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
    }
}

public class SJBP {

    public static void sjf_p(List<Process> processes) {
        int time = 0;
        int completed = 0;
        int n = processes.size();

        while (completed < n) {
            Process current = null;

            for (Process p : processes) {
                if (p.arrival <= time && p.remaining > 0) {
                    if (current == null || p.remaining < current.remaining) {
                        current = p;
                    }
                }
            }

            if (current == null) {
                time++; 
                continue;
            }

            current.remaining--;
            time++;

            if (current.remaining == 0) {
                current.completion = time;
                completed++;
            }
        }
    }

    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();

        processes.add(new Process(1, 0, 8));
        processes.add(new Process(2, 1, 4));
        processes.add(new Process(3, 2, 9));
        processes.add(new Process(4, 3, 5));

        sjf_p(processes);

        System.out.println("ID\tCompletion\tTurnaround\tWaiting");

        int totalWaiting = 0;
        int totalTurnaround = 0;

        for (Process p : processes) {
            int turnaround = p.completion - p.arrival;
            int waiting = turnaround - p.burst;
            totalWaiting += waiting;
            totalTurnaround += turnaround;
            System.out.println(p.id + "\t" + p.completion + "\t\t" + turnaround + "\t\t" + waiting);

        }
        float avgWaiting = (float) totalWaiting / processes.size();
        float avgTurnaround = (float) totalTurnaround / processes.size();
        System.out.println("Average Waiting Time: " + avgWaiting);
        System.out.println("Average Turnaround Time: " + avgTurnaround);
    }
}