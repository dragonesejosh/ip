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
                addTask(new Task.ToDo(m.group(1)));
            } else if ((m = DEADLINE_REGEX.matcher(msg)).matches()) {
                addTask(new Task.Deadline(m.group(1), m.group(2)));
            } else if ((m = EVENT_REGEX.matcher(msg)).matches()) {
                addTask(new Task.Event(m.group(1), m.group(2), m.group(3)));
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
