package yale;

import java.util.Scanner;

public class Ui {
    private static final String NAME = "Yale";
    private static final String LINE = "\t____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a Ui to display output.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Asks the user for input and returns the answer.
     */
    public String getUserInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    /**
     * Begins outputting a response.
     */
    public void beginOutput() {
        System.out.println(LINE);
    }

    /**
     * Finish outputting a response.
     */
    public void endOutput() {
        System.out.println(LINE);
    }

    /**
     * Display a formatted message to the user.
     *
     * @param msg The formatted message to display.
     * @param args The arguments of the message.
     */
    public void print(String msg, Object... args) {
        System.out.printf("\t" + msg + "\n", args);
    }

    /**
     * Display a formatted error message to the user.
     *
     * @param msg The formatted error message to display.
     * @param args The arguments of the error message.
     */
    public void printError(String msg, Object... args) {
        System.out.printf("\t" + msg + "\n", args);
    }

    /**
     * Displays a greeting to the user.
     */
    public void greet() {
        beginOutput();
        print("Hello! I'm %s.", NAME);
        print("What can I do for you?");
        endOutput();
    }

    /**
     * Displays a goodbye message to the user.
     */
    public void goodbye() {
        beginOutput();
        print("Bye. Hope to see you again soon!");
        endOutput();
    }
}
