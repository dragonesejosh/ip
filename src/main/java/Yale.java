import java.io.File;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Yale {
    private static final String NAME = "Yale";
    private static final String LINE = "\t____________________________________________________________";
    private static final String TASKS_FILE = "tasks.txt";

    private static final Pattern MARK_REGEX = Pattern.compile("mark (\\d+)");
    private static final Pattern UNMARK_REGEX = Pattern.compile("unmark (\\d+)");
    private static final Pattern TODO_REGEX = Pattern.compile("todo (.+)");
    private static final Pattern DEADLINE_REGEX = Pattern.compile("deadline (.+) /by (.+)");
    private static final Pattern EVENT_REGEX = Pattern.compile("event (.+) /from (.+) /to (.+)");
    private static final Pattern DELETE_REGEX = Pattern.compile("delete (\\d+)");

    private static final String[][] COMMANDS = {
            {"mark", "mark [id]"},
            {"unmark", "unmark [id]"},
            {"todo", "todo [name]"},
            {"deadline", "deadline [name] /by [date]"},
            {"event", "event [name] /from [start] /to [end]"},
            {"delete", "delete [id]"}
    };

    private static final ArrayList<Task> tasks = retrieveTasks();

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
                addTask(new ToDo(m.group(1)));
            } else if ((m = DEADLINE_REGEX.matcher(msg)).matches()) {
                addTask(new Deadline(m.group(1), m.group(2)));
            } else if ((m = EVENT_REGEX.matcher(msg)).matches()) {
                addTask(new Event(m.group(1), m.group(2), m.group(3)));
            } else if ((m = DELETE_REGEX.matcher(msg)).matches()) {
                deleteTask(Integer.parseInt(m.group(1)));
            } else {
                tryFindCommand(msg);
            }
        }
        goodbye();
    }

    private static ArrayList<Task> retrieveTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        try (Scanner in = new Scanner(new File(TASKS_FILE))) {
            while (in.hasNextLine()) {
                Task task = Task.fromCsv(in.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return tasks;
    }

    private static void writeTasksToFile() {
        try (PrintWriter out = new PrintWriter(TASKS_FILE)) {
            for (Task task : tasks) {
                out.println(task.toCsv());
            }
            out.flush();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void deleteTask(int id) {
        System.out.println(LINE);
        if (checkInvalidID(id)) {
            System.out.println(LINE);
            return;
        }
        Task task = tasks.remove(id-1);
        writeTasksToFile();
        System.out.println("\tNoted. I've removed this task:");
        System.out.printf("\t  %s\n", task);
        if (tasks.size() == 1) {
            System.out.println("\tNow you have 1 task in the list.");
        } else {
            System.out.printf("\tNow you have %d tasks in the list.\n", tasks.size());
        }
        System.out.println(LINE);
    }

    private static boolean checkInvalidID(int id) {
        if (tasks.isEmpty()) {
            System.out.println("\tERROR: You don't have any tasks!");
            return true;
        }
        if (id > tasks.size() || id <= 0) {
            System.out.printf("\tERROR: The id should be from 1 to %d.\n", tasks.size());
            return true;
        }
        return false;
    }

    private static void tryFindCommand(String msg) {
        System.out.println(LINE);
        for (String[] command : COMMANDS) {
            if (msg.startsWith(command[0])) {
                System.out.printf("\tERROR: The proper format for %s is '%s'.%n", command[0].toUpperCase(), command[1]);
                System.out.println(LINE);
                return;
            }
        }
        System.out.println("\tERROR: Sorry, I don't know what that command means.");
        System.out.println(LINE);
    }

    private static abstract class Task {
        protected final String name;
        protected boolean isDone = false;
        public Task(String name) {
            this.name = name;
        }
        public void setDone(boolean done) {
            this.isDone = done;
        }
        public String toString() {
            return "[%s] %s".formatted(isDone ? "X" : " ", name);
        }
        protected abstract String[] getParams();
        public String toCsv() {
            return String.join("\0", getParams());
        }
        public static Task fromCsv(String csvString) {
            String[] tokens = csvString.split("\0");
            return switch (tokens.length) {
                case 2 -> new ToDo(tokens[1]);
                case 3 -> new Deadline(tokens[1], tokens[2]);
                case 4 -> new Event(tokens[1], tokens[2], tokens[3]);
                default -> null;
            };
        }
    }

    private static class ToDo extends Task {
        public ToDo(String name) {
            super(name);
        }
        public String toString() {
            return "[T]%s".formatted(super.toString());
        }
        protected String[] getParams() {
            return new String[] {isDone ? "X" : " ", name};
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
        protected String[] getParams() {
            return new String[] {isDone ? "X" : " ", name, deadline};
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
        protected String[] getParams() {
            return new String[]{isDone ? "X" : " ", name, start, end};
        }
    }

    private static void markDone(int id, boolean done) {
        System.out.println(LINE);
        if (checkInvalidID(id)) {
            System.out.println(LINE);
            return;
        }
        Task task = tasks.get(id-1);
        task.setDone(done);
        writeTasksToFile();
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
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("\t%d.%s\n", i+1, tasks.get(i));
        }
        System.out.println(LINE);
    }

    private static void addTask(Task task) {
        tasks.add(task);
        writeTasksToFile();
        System.out.println(LINE);
        System.out.println("\tGot it. I've added this task:");
        System.out.printf("\t  %s\n", task);
        if (tasks.size() == 1) {
            System.out.println("\tNow you have 1 task in the list.");
        } else {
            System.out.printf("\tNow you have %d tasks in the list.\n", tasks.size());
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
