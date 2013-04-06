package net.jmhertlein.core.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The BugReportDaemon class is the main class for the BRD project.
 * This class handles the CLI front-end to the daemon.
 * @author joshua
 */
public class BugReportDaemon {

    private static ConnectionListenTask connectionListener;
    private static LinkedHashSet<BugReport> reports;
    private static boolean done, running;
    private static Scanner scan;
    private static Thread th;

    public static void main(String[] args) {
        setupShutdownHook();
        reports = new LinkedHashSet<>();
        running = false;
        done = false;
        scan = new Scanner(System.in);

        start();

        printMenu();

        while (!done) {
            String resp = scan.nextLine().trim();

            switch (resp) {
                default:
                    System.out.println("Invalid command");
                    printMenu();
                    break;
                case "stop":
                    stop();
                    break;
                case "start":
                    start();
                    break;
                case "dump":
                    dumpReportsToFile(reports);
                    break;
                case "exit":
                    quit();
                    break;
                case "help":
                    printMenu();
                    break;

                case "info":
                    System.out.println("Currently holding " + reports.size() + " reports in volatile memory.");
                    break;

                case "clear":
                    reports.clear();
                    System.out.println("Deleted reports.");
                    break;

                case "print":
                    print();
                    break;
            }
        }


    }

    private static void printMenu() {
        System.out.println("Options are:");
        System.out.println("dump - dumps all reports to file");
        System.out.println("stop - stop listening on port");
        System.out.println("exit - quit program");
        System.out.println("start - start listening on port");
        System.out.println("info - prints info about current state");
        System.out.println("clear - delete currently held reports");
        System.out.println("print - print all currently held reports");
    }

    private static void dumpReportsToFile(Set<BugReport> reports) {
        File f = new File("./bug_reports0");
        int c = 0;
        while (f.exists()) {
            c++;
            f = new File("./bug_reports" + c);
        }

        try {
            f.createNewFile();
        } catch (IOException ex) {
            System.err.println("Error making file: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        try (PrintStream ps = new PrintStream(f)) {
            for (BugReport report : reports) {
                ps.println(report.toString());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BugReportDaemon.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        System.out.println("Reports dumped.");
    }

    /**
     * Stops the daemon from listening on the network port. 
     * Does NOT cause the daemon to exit- it will continue to respond to stdin
     */
    public static void stop() {
        if (!running) {
            System.out.println("Already stopped.");
            return;
        }
        connectionListener.stop();
        connectionListener = null;
        System.out.println("Waiting for connection thread to join...");
        try {
            th.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(BugReportDaemon.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Joined.");

        th = null;
        running = false;
        System.out.println("Stopped.");
    }

    /**
     * Prints all reports to stdout
     */
    public static void print() {
        System.out.println("Printing reports...");
        for (BugReport report : reports) {
            System.out.println(report.toString());
        }
    }

    /**
     * Starts the daemon listening on the network port.
     */
    public static void start() {
        if (running) {
            System.out.println("Already listening.");
            return;
        }
        connectionListener = new ConnectionListenTask(reports);
        th = new Thread(connectionListener);
        th.start();
        running = true;
        System.out.println("Started.");
    }

    /**
     * Causes the daemon to exit. The CLI will stop responding.
     * All threads will be requested to terminate
     */
    public static void quit() {
        System.out.println("Quitting...");
        stop();
        done = true;
    }

    private static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                if (running)
                    quit();
                if (reports.size() > 0)
                    dumpReportsToFile(reports);
            }
        }));
    }
}
