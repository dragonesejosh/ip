import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

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

    private final Ui ui;
    private final Storage storage;
    private final TaskList taskList;

    public Parser(Ui ui, Storage storage, TaskList taskList) {
        this.ui = ui;
        this.storage = storage;
        this.taskList = taskList;
    }

    public boolean parseMsg(String msg) {
        Matcher m;
        boolean needWriteTasks = false;
        if (msg.equals("bye")) {
            return false;
        } else if (msg.equals("list")) {
            taskList.listOut();
        } else if ((m = MARK_REGEX.matcher(msg)).matches()) {
            needWriteTasks = taskList.markDone(Integer.parseInt(m.group(1)), true);
        } else if ((m = UNMARK_REGEX.matcher(msg)).matches()) {
            needWriteTasks = taskList.markDone(Integer.parseInt(m.group(1)), false);
        } else if ((m = TODO_REGEX.matcher(msg)).matches()) {
            needWriteTasks = taskList.addTask(new Task.ToDo(m.group(1)));
        } else if ((m = DEADLINE_REGEX.matcher(msg)).matches()) {
            needWriteTasks = taskList.addTask(new Task.Deadline(m.group(1), m.group(2)));
        } else if ((m = EVENT_REGEX.matcher(msg)).matches()) {
            needWriteTasks = taskList.addTask(new Task.Event(m.group(1), m.group(2), m.group(3)));
        } else if ((m = DELETE_REGEX.matcher(msg)).matches()) {
            needWriteTasks = taskList.deleteTask(Integer.parseInt(m.group(1)));
        } else {
            tryFindCommand(msg);
        }
        if (needWriteTasks) {
            storage.writeTasks(taskList.getTasks());
        }
        return true;
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
}
