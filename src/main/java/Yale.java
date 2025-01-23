import java.util.ArrayList;
import java.util.Scanner;

public class Yale {
    private static final String NAME = "Yale";
    private static final String LINE = "\t____________________________________________________________";

    private static final ArrayList<Task> list = new ArrayList<>();

    public static void main(String[] args) {
        greet();
        Scanner in = new Scanner(System.in);
        while (true) {
            String msg = in.nextLine();
            if (msg.equals("bye")) {
                break;
            } else if (msg.equals("list")) {
                listOut();
            } else {
                addToList(msg);
            }
        }
        goodbye();
    }

    private static class Task {
        private final String name;
        private boolean done = false;
        public Task(String name) {
            this.name = name;
        }
        public void setDone(boolean done) {
            this.done = done;
        }
        public String toString() {
            return "[" + (done ? "X" : " ") + "] " + name;
        }
    }

    private static void listOut() {
        System.out.println(LINE);
        System.out.println("\tHere are the tasks in your list:");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("\t%d.%s\n", i+1, list.get(i));
        }
        System.out.println(LINE);
    }

    private static void addToList(String msg) {
        Task task = new Task(msg);
        list.add(task);
        System.out.println(LINE);
        System.out.printf("\tadded: %s\n", msg);
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
