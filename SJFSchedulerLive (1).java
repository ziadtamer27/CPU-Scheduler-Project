import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SJFSchedulerLive {

    static class GanttEntry {
        String pid;
        int start;
        int end;

        public GanttEntry(String pid, int start, int end) {
            this.pid = pid;
            this.start = start;
            this.end = end;
        }
    }

    private static final List<Process> processes = Collections.synchronizedList(new ArrayList<>());
    private static final List<GanttEntry> ganttChart = Collections.synchronizedList(new ArrayList<>());
    private static final Scanner scanner = new Scanner(System.in);

    private static final AtomicBoolean schedulingFinished = new AtomicBoolean(false);

    public static void main(String[] args) {
        System.out.println("===== SJF (Non-Preemptive) Scheduler =====");
        System.out.println("1. Live Scheduling (can add processes dynamically)");
        System.out.println("2. Run Current Processes Only");
        System.out.print("Choose option: ");
        int mode = readInt();

        System.out.print("Enter number of processes ready initially: ");
        int n = readInt();

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for Process " + (i + 1));
            System.out.print("Process ID: ");
            String pid = scanner.next();
            System.out.print("Arrival Time: ");
            int at = readInt();
            System.out.print("Burst Time: ");
            int bt = readInt();

            synchronized (processes) {
                processes.add(new Process(pid, at, bt));
            }
        }

        if (mode == 1) {
            Thread inputThread = new Thread(() -> {
                while (!schedulingFinished.get()) {
                    System.out.println("\nDo you want to add a new process? (yes/no)");
                    String choice = scanner.next();
                    if (choice.equalsIgnoreCase("yes")) {
                        System.out.print("Process ID: ");
                        String pid = scanner.next();
                        System.out.print("Arrival Time: ");
                        int at = readInt();
                        System.out.print("Burst Time: ");
                        int bt = readInt();

                        synchronized (processes) {
                            processes.add(new Process(pid, at, bt));
                            System.out.println("Process " + pid + " added successfully.");
                        }
                    } else if (choice.equalsIgnoreCase("no")) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
            });

            inputThread.setDaemon(true);
            inputThread.start();

            runScheduler(true);
        } else {
            runScheduler(false);
        }
    }

    private static void runScheduler(boolean liveMode) {
        int currentTime = 0;
        int completedCount = 0;

        while (true) {
            Process currentProcess = selectShortestJob(currentTime);

            if (currentProcess == null) {
                if (allProcessesCompleted()) {
                    break;
                }

                System.out.println("\nTime " + currentTime + ": CPU Idle");
                printRemainingBurstTable(currentTime);
                sleepOneUnit();
                currentTime++;
                continue;
            }

            if (currentProcess.startTime == -1) {
                currentProcess.startTime = currentTime;
            }

            System.out.println("\nTime " + currentTime + ": Running " + currentProcess.pid);
            int start = currentTime;

            while (currentProcess.remainingTime > 0) {
                printRemainingBurstTable(currentTime);
                sleepOneUnit();
                currentProcess.remainingTime--;
                currentTime++;
            }

            currentProcess.completionTime = currentTime;
            currentProcess.completed = true;
            completedCount++;

            ganttChart.add(new GanttEntry(currentProcess.pid, start, currentTime));

            System.out.println("Process " + currentProcess.pid + " completed at time " + currentTime);

            if (!liveMode && completedCount == processes.size()) {
                break;
            }
        }

        schedulingFinished.set(true);

        printFinalResults();
    }

    private static Process selectShortestJob(int currentTime) {
        Process selected = null;

        synchronized (processes) {
            for (Process p : processes) {
                if (!p.completed && p.arrivalTime <= currentTime) {
                    if (selected == null ||
                            p.burstTime < selected.burstTime ||
                            (p.burstTime == selected.burstTime && p.arrivalTime < selected.arrivalTime)) {
                        selected = p;
                    }
                }
            }
        }

        return selected;
    }

    private static boolean allProcessesCompleted() {
        synchronized (processes) {
            if (processes.isEmpty()) {
                return false;
            }
            for (Process p : processes) {
                if (!p.completed) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printRemainingBurstTable(int currentTime) {
        System.out.println("\nRemaining Burst Time Table at Time " + currentTime);
        System.out.println("-------------------------------------------------");
        System.out.printf("%-10s %-10s %-10s %-15s%n", "PID", "Arrival", "Burst", "Remaining");
        System.out.println("-------------------------------------------------");

        synchronized (processes) {
            for (Process p : processes) {
                System.out.printf("%-10s %-10d %-10d %-15d%n",
                        p.pid, p.arrivalTime, p.burstTime, p.remainingTime);
            }
        }
    }

    private static void printFinalResults() {
        System.out.println("\n===== LIVE GANTT CHART =====");
        for (GanttEntry entry : ganttChart) {
            System.out.print("| " + entry.pid + " ");
        }
        System.out.println("|");

        if (!ganttChart.isEmpty()) {
            System.out.print(ganttChart.get(0).start);
            for (GanttEntry entry : ganttChart) {
                System.out.print("     " + entry.end);
            }
            System.out.println();
        }

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        System.out.println("\n===== PROCESS DETAILS =====");
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-10s %-10s %-10s %-10s %-15s %-15s%n",
                "PID", "Arrival", "Burst", "Start", "Waiting", "Turnaround");
        System.out.println("-----------------------------------------------------------------------");

        synchronized (processes) {
            for (Process p : processes) {
                int turnaroundTime = p.completionTime - p.arrivalTime;
                int waitingTime = turnaroundTime - p.burstTime;

                totalWaitingTime += waitingTime;
                totalTurnaroundTime += turnaroundTime;

                System.out.printf("%-10s %-10d %-10d %-10d %-15d %-15d%n",
                        p.pid, p.arrivalTime, p.burstTime, p.startTime, waitingTime, turnaroundTime);
            }

            int n = processes.size();
            System.out.println("\nAverage Waiting Time = " + (totalWaitingTime / n));
            System.out.println("Average Turnaround Time = " + (totalTurnaroundTime / n));
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    private static void sleepOneUnit() {
        try {
            Thread.sleep(1000); // 1 time unit = 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}