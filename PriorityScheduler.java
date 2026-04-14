import java.util.*;

//Non preemptive
class PriorityNonPreemptive {

    public static void schedule(List<Process> processes, int mode, int execType, Scanner sc) {
        int time = 0, completed = 0;
        int n = processes.size();
        

        List<String> gantt = new ArrayList<>();

        while (completed < n) {

            if (execType == 2 && time % 5 == 0) {
                System.out.print("Add new process? (y/n): ");
                String choice = sc.next();
                if (choice.equalsIgnoreCase("y")) {
                    System.out.print("Arrival Time: ");
                    int at = sc.nextInt();
                    System.out.print("Burst Time: ");
                    int bt = sc.nextInt();
                    System.out.print("Priority: ");
                    int pr = sc.nextInt();

                    processes.add(new Process(processes.size() + 1, at, bt, pr));
                    n++;
                }
            }

            Process selected = null;

            for (Process p : processes) {
                if (p.arrivalTime <= time && p.remainingTime > 0) {
                    if (selected == null || p.priority < selected.priority) {
                        selected = p;
                    }
                }
            }

            if (selected == null) {
                gantt.add("Idle");
                time++;
                sleep(mode);
                continue;
            }

            for (int i = 0; i < selected.burstTime; i++) {
                gantt.add("P" + selected.id);
                time++;
                sleep(mode);
            }

            selected.remainingTime = 0;
            selected.turnaroundTime = time - selected.arrivalTime;
            selected.waitingTime = selected.turnaroundTime - selected.burstTime;

            completed++;
        }

        printResults(processes, gantt);
    }

    private static void sleep(int mode) {
        if (mode == 2) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }

    private static void printResults(List<Process> processes, List<String> gantt) {
        double avgWT = 0, avgTAT = 0;

        System.out.println("\nProcess\tWT\tTAT");
        for (Process p : processes) {
            System.out.println("P" + p.id + "\t" + p.waitingTime + "\t" + p.turnaroundTime);
            avgWT += p.waitingTime;
            avgTAT += p.turnaroundTime;
        }

        System.out.println("\nGantt Chart:");
        for (String s : gantt) System.out.print("|" + s);
        System.out.println("|");

        System.out.println("\nAverage Waiting Time = " + (avgWT / processes.size()));
        System.out.println("Average Turnaround Time = " + (avgTAT / processes.size()));
    }
}
        //Preemptive
class PriorityPreemptive {

    public static void schedule(List<Process> processes, int mode, int execType, Scanner sc) {
        int time = 0, completed = 0;
        int n = processes.size();

        List<String> gantt = new ArrayList<>();

        while (completed < n) {

            if (execType == 2 && time % 5 == 0) {
                System.out.print("Add new process? (y/n): ");
                String choice = sc.next();
                if (choice.equalsIgnoreCase("y")) {
                    System.out.print("Arrival Time: ");
                    int at = sc.nextInt();
                    System.out.print("Burst Time: ");
                    int bt = sc.nextInt();
                    System.out.print("Priority: ");
                    int pr = sc.nextInt();

                    processes.add(new Process(processes.size() + 1, at, bt, pr));
                    n++;
                }
            }

            Process selected = null;

            for (Process p : processes) {
                if (p.arrivalTime <= time && p.remainingTime > 0) {
                    if (selected == null || p.priority < selected.priority) {
                        selected = p;
                    }
                }
            }

            if (selected == null) {
                gantt.add("Idle");
                time++;
                sleep(mode);
                continue;
            }

            selected.remainingTime--;
            gantt.add("P" + selected.id);
            time++;
            sleep(mode);

            if (selected.remainingTime == 0) {
                selected.turnaroundTime = time - selected.arrivalTime;
                selected.waitingTime = selected.turnaroundTime - selected.burstTime;
                completed++;
            }
        }

        printResults(processes, gantt);
    }

    private static void sleep(int mode) {
        if (mode == 2) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }

    private static void printResults(List<Process> processes, List<String> gantt) {
        double avgWT = 0, avgTAT = 0;

        System.out.println("\nProcess\tWT\tTAT");
        for (Process p : processes) {
            System.out.println("P" + p.id + "\t" + p.waitingTime + "\t" + p.turnaroundTime);
            avgWT += p.waitingTime;
            avgTAT += p.turnaroundTime;
        }

        System.out.println("\nGantt Chart:");
        for (String s : gantt) System.out.print("|" + s);
        System.out.println("|");

        System.out.println("\nAverage Waiting Time = " + (avgWT / processes.size()));
        System.out.println("Average Turnaround Time = " + (avgTAT / processes.size()));
    }
}
        //MAIN
public class PriorityScheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("====== Process " + (i + 1) + " ======");
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            System.out.print("Priority: ");
            int pr = sc.nextInt();

            processes.add(new Process(i + 1, at, bt, pr));
        }

        System.out.println("Choose Execution Type:");
        System.out.println("1. Static (no new processes)");
        System.out.println("2. Dynamic (allow adding processes)");
        int execType = sc.nextInt();

        System.out.println("Choose Mode:\n1. Non-Live\n2. Live");
        int mode = sc.nextInt();

        System.out.println("Choose Scheduler:\n1. Non-Preemptive\n2. Preemptive");
        int choice = sc.nextInt();

        if (choice == 1)    PriorityNonPreemptive.schedule(processes, mode, execType, sc);
        else                PriorityPreemptive.schedule(processes, mode, execType, sc);
    }
}
