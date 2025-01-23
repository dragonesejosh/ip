import java.util.Scanner;

public class Yale {
    private static final String NAME = "Yale";
    private static final String LINE = "\t____________________________________________________________";

    public static void main(String[] args) {
        greet();
        Scanner in = new Scanner(System.in);
        while (true) {
            String msg = in.nextLine();
            if (msg.equalsIgnoreCase("bye")) {
                break;
            }
            echo(msg);
        }
        goodbye();
    }

    private static void echo(String msg) {
        System.out.println(LINE);
        System.out.println("\t" + msg);
        System.out.println(LINE);
    }

    private static void greet() {
        System.out.println(LINE);
        System.out.printf("\tHello! I'm %s.\n", NAME);
        System.out.println("\tWhat can I do for you?");
        System.out.println(LINE);
    }

    private static void goodbye() {
        System.out.println(LINE);
        System.out.println("\tBye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
