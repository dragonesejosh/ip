import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Yale {
    private static final String NAME = "Yale";
    private static final String LINE = "\t____________________________________________________________";

    private static final Pattern MARK_REGEX = Pattern.compile("mark (\\d+)");
    private static final Pattern UNMARK_REGEX = Pattern.compile("unmark (\\d+)");
    private static final Pattern TODO_REGEX = Pattern.compile("todo (.+)");
    private static final Pattern DEADLINE_REGEX = Pattern.compile("deadline (.+) /by (.+)");
    private static final Pattern EVENT_REGEX = Pattern.compile("event (.+) /from (.+) /to (.+)");

    private static final String[][] COMMANDS = {
            {"mark", "mark [id]"},
            {"unmark", "unmark [id]"},
            {"todo", "todo [name]"},
            {"deadline", "deadline [name] /by [date]"},
            {"event", "event [name] /from [start] /to [end]"},
    };

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
            } else if ((m = TODO_REGEX.matcher(msg)).matches()) {
                addToList(new ToDo(m.group(1)));
            } else if ((m = DEADLINE_REGEX.matcher(msg)).matches()) {
                addToList(new Deadline(m.group(1), m.group(2)));
            } else if ((m = EVENT_REGEX.matcher(msg)).matches()) {
                addToList(new Event(m.group(1), m.group(2), m.group(3)));
            } else {
                tryFindCommand(msg);
            }
        }
        goodbye();
    }

    private static void tryFindCommand(String msg) {
        System.out.println(LINE);
        for (String[] command : COMMANDS) {
            if (msg.startsWith(command[0])) {
                System.out.printf("The proper format for %s is '%s'.%n", command[0].toUpperCase(), command[1]);
                System.out.println(LINE);
                return;
            }
        }
        System.out.println("\tSorry, I don't know what that command means.");
        System.out.println(LINE);
    }

    private static abstract class Task {
        private final String name;
        private boolean done = false;
        public Task(String name) {
            this.name = name;
        }
        public void setDone(boolean done) {
            this.done = done;
        }
        public String toString() {
            return "[%s] %s".formatted(done ? "X" : " ", name);
        }
    }

    private static class ToDo extends Task {
        public ToDo(String name) {
            super(name);
        }
        public String toString() {
            return "[T]%s".formatted(super.toString());
        }
    }

    private static class Deadline extends Task {
        private final String deadline;
        public Deadline(String name, String deadline) {
            super(name);
            this.deadline = deadline;
        }
        public String toString() {
            return "[D]%s (by: %s)".formatted(super.toString(), deadline);
        }
    }

    private static class Event extends Task {
        private final String start;
        private final String end;
        public Event(String name, String start, String end) {
            super(name);
            this.start = start;
            this.end = end;
        }
        public String toString() {
            return "[E]%s (from: %s, to: %s)".formatted(super.toString(), start, end);
        }
    }

    private static void markDone(int id, boolean done) {
        System.out.println(LINE);
        if (list.isEmpty()) {
            System.out.println("You don't have any tasks!");
            System.out.println(LINE);
            return;
        }
        if (id > list.size() || id <= 0) {
            System.out.printf("The id should be from 1 to %d.\n", list.size());
            System.out.println(LINE);
            return;
        }
        Task task = list.get(id-1);
        task.setDone(done);
        if (done) {
            System.out.println("\tNice! I've marked this task as done:");
        } else {
            System.out.println("\tOK, I've marked this task as not done yet:");
        }
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

    private static void addToList(Task task) {
        list.add(task);
        System.out.println(LINE);
        System.out.println("\tGot it. I've added this task:");
        System.out.printf("\t  %s\n", task);
        if (list.size() == 1) {
            System.out.println("\tNow you have 1 task in the list.");
        } else {
            System.out.printf("\tNow you have %d tasks in the list.\n", list.size());
        }
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
