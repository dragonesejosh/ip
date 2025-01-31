import java.io.File;
import java.io.PrintWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Yale {
    private static final String TASKS_FILE = "tasks.txt";

    private static final Pattern MARK_REGEX = Pattern.compile("mark (\\d+)");
    private static final Pattern UNMARK_REGEX = Pattern.compile("unmark (\\d+)");
    private static final Pattern TODO_REGEX = Pattern.compile("todo (.+)");
    private static final Pattern DEADLINE_REGEX = Pattern.compile("deadline (.+) /by (.+)");
    private static final Pattern EVENT_REGEX = Pattern.compile("event (.+) /from (.+) /to (.+)");
    private static final Pattern DELETE_REGEX = Pattern.compile("delete (\\d+)");

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    private static final String[][] COMMANDS = {
            {"mark", "mark [id]"},
            {"unmark", "unmark [id]"},
            {"todo", "todo [name]"},
            {"deadline", "deadline [name] /by [date]"},
            {"event", "event [name] /from [start] /to [end]"},
            {"delete", "delete [id]"}
    };

    private final ArrayList<Task> tasks = retrieveTasks();

    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private final TaskList taskList;

    public static void main(String[] args) {
        new Yale().run();
    }

    public Yale() {
        this.ui = new Ui();
        this.storage = new Storage();
        this.parser = new Parser();
        this.taskList = new TaskList();
    }

    public void run() {
        ui.greet();
        Matcher m;
        while (true) {
            String msg = ui.getUserInput();
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
        ui.goodbye();
    }

    private ArrayList<Task> retrieveTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        try (Scanner in = new Scanner(new File(TASKS_FILE))) {
            while (in.hasNextLine()) {
                Task task = Task.fromCsv(in.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (Exception ignored) {}
        return tasks;
    }

    private void writeTasksToFile() {
        try (PrintWriter out = new PrintWriter(TASKS_FILE)) {
            for (Task task : tasks) {
                out.println(task.toCsv());
            }
            out.flush();
        } catch (Exception ignored) {}
    }

    private void deleteTask(int id) {
        ui.beginOutput();
        if (checkInvalidID(id)) {
            ui.endOutput();
            return;
        }
        Task task = tasks.remove(id-1);
        writeTasksToFile();
        ui.print("Noted. I've removed this task:");
        ui.print("  %s", task);
        ui.print("Now you have %d task%s in the list.",
                tasks.size(), tasks.size() == 1 ? "" : "s");
        ui.endOutput();
    }

    private boolean checkInvalidID(int id) {
        if (tasks.isEmpty()) {
            ui.printError("You don't have any tasks!");
            return true;
        }
        if (id > tasks.size() || id <= 0) {
            ui.printError("The id should be from 1 to %d.", tasks.size());
            return true;
        }
        return false;
    }

    private void tryFindCommand(String msg) {
        ui.beginOutput();
        for (String[] command : COMMANDS) {
            if (msg.startsWith(command[0])) {
                ui.printError("The proper format for %s is '%s'.", command[0].toUpperCase(), command[1]);
                ui.endOutput();
                return;
            }
        }
        ui.printError("Sorry, I don't know what that command means.");
        ui.endOutput();
    }

    public static LocalDate tryParseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return null;
        }
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
        private final String deadlineStr;
        private final LocalDate deadlineDate;
        public Deadline(String name, String deadline) {
            super(name);
            this.deadlineDate = tryParseDate(deadline);
            this.deadlineStr = (this.deadlineDate == null) ? deadline : this.deadlineDate.format(DATE_FORMAT);
        }
        public String toString() {
            return "[D]%s (by: %s)".formatted(super.toString(), deadlineStr);
        }
        protected String[] getParams() {
            return new String[] {isDone ? "X" : " ", name, deadlineStr};
        }
    }

    private static class Event extends Task {
        private final String startStr;
        private final String endStr;
        private final LocalDate startDate;
        private final LocalDate endDate;
        public Event(String name, String start, String end) {
            super(name);
            this.startDate = tryParseDate(start);
            this.startStr = (this.startDate == null) ? start : this.startDate.format(DATE_FORMAT);
            this.endDate = tryParseDate(end);
            this.endStr = (this.endDate == null) ? end : this.endDate.format(DATE_FORMAT);
        }
        public String toString() {
            return "[E]%s (from: %s, to: %s)".formatted(super.toString(), startStr, endStr);
        }
        protected String[] getParams() {
            return new String[]{isDone ? "X" : " ", name, startStr, endStr};
        }
    }

    private void markDone(int id, boolean done) {
        ui.beginOutput();
        if (checkInvalidID(id)) {
            ui.endOutput();
            return;
        }
        Task task = tasks.get(id-1);
        task.setDone(done);
        writeTasksToFile();
        if (done) {
            ui.print("Nice! I've marked this task as done:");
        } else {
            ui.print("OK, I've marked this task as not done yet:");
        }
        ui.print("  %s", task);
        ui.endOutput();
    }

    private void listOut() {
        ui.beginOutput();
        ui.print("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.print("%d.%s", i+1, tasks.get(i));
        }
        ui.endOutput();
    }

    private void addTask(Task task) {
        ui.beginOutput();
        tasks.add(task);
        writeTasksToFile();
        ui.print("Got it. I've added this task:");
        ui.print("  %s", task);
        ui.print("Now you have %d task%s in the list.",
                tasks.size(), tasks.size() == 1 ? "" : "s");
        ui.endOutput();
    }
}
