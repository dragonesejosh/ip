import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Yale {
    private static final String NAME = "Yale";
    private static final String LINE = "\t____________________________________________________________";
    private static final Pattern MARK_REGEX = Pattern.compile("mark (\\d+)");
    private static final Pattern UNMARK_REGEX = Pattern.compile("unmark (\\d+)");

    private static final ArrayList<Task> list = new ArrayList<>();

    public static void main(String[] args) {
        greet();
        Scanner in = new Scanner(System.in);
        Matcher m;
        while (true) {
            System.out.print("> ");
            String msg = in.nextLine();
            if (msg.equals("bye")) {
                break;
            } else if (msg.equals("list")) {
                listOut();
            } else if ((m = MARK_REGEX.matcher(msg)).matches()) {
                markDone(Integer.parseInt(m.group(1)), true);
            } else if ((m = UNMARK_REGEX.matcher(msg)).matches()) {
                markDone(Integer.parseInt(m.group(1)), false);
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

    private static void markDone(int id, boolean done) {
        System.out.println(LINE);
        if (done) {
            System.out.println("\tNice! I've marked this task as done:");
        } else {
            System.out.println("\tOK, I've marked this task as not done yet:");
        }
        Task task = list.get(id-1);
        task.setDone(done);
        System.out.printf("\t  %s\n", task);
        System.out.println(LINE);
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
