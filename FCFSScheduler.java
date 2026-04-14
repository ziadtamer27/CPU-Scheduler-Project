import java.util.*;

public class FCFSScheduler {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] p = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Enter Arrival Time and Burst Time for P" + (i+1) + ":");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            p[i] = new Process(i+1, at, bt);
        }

        Arrays.sort(p, Comparator.comparingInt(process -> process.at));

        int time = 0;

        System.out.println();
        System.out.println("Live Execution");

        for (int i = 0; i < n; i++) {

            if(time < p[i].at)time = p[i].at; 
        
            System.err.println();
            System.out.println("Process P" + p[i].pid + " starts at time " + time);

            while (p[i].remaining_bt > 0) {

                System.out.print("Time " + time + " | ");
                for (int j = 0; j < n; j++) {
                    System.out.print("P" + p[j].pid + ": " + p[j].remaining_bt + "  |  ");
                }
                System.out.println();

                p[i].remaining_bt--;
                time++;

                Thread.sleep(500);
            }

            p[i].dt = time;
        }

        double totalWT = 0, totalTAT = 0;

        for (int i = 0; i < n; i++) {
            totalWT += (p[i].dt - p[i].at - p[i].bt);
            totalTAT += (p[i].dt - p[i].at);
        }

        System.out.println();
        System.out.println("Average Waiting Time = " + (totalWT / n));
        System.out.println("Average Turnaround Time = " + (totalTAT / n));

        System.err.println();

        sc.close();
    }
}
