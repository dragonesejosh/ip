import java.util.Scanner;

public class Ui {
    private static final String NAME = "Yale";
    private static final String LINE = "\t____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String getUserInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    public void beginOutput() {
        System.out.println(LINE);
    }

    public void endOutput() {
        System.out.println(LINE);
    }

    public void print(String msg, Object... args) {
        System.out.printf("\t" + msg + "\n", args);
    }

    public void printError(String msg, Object... args) {
        System.out.printf("\tERROR: " + msg + "\n", args);
    }

    public void greet() {
        beginOutput();
        print("Hello! I'm %s.", NAME);
        print("What can I do for you?");
        endOutput();
    }

    public void goodbye() {
        beginOutput();
        print("Bye. Hope to see you again soon!");
        endOutput();
    }
}
