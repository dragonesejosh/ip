package yale;

public class Parser {

    private static final Command[] COMMANDS = {
            new Command("mark", "mark [id]", "mark (\\d+)",
                    (t, m) -> t.markDone(Integer.parseInt(m.group(1)), true)),
            new Command("unmark", "unmark [id]", "unmark (\\d+)",
                    (t, m) -> t.markDone(Integer.parseInt(m.group(1)), false)),
            new Command("todo", "todo [name]", "todo (.+)",
                    (t, m) -> t.addTask(new Task.ToDo(m.group(1)))),
            new Command("deadline", "deadline [name] /by [date]",
                    "deadline (.+) /by (.+)",
                    (t, m) -> t.addTask(new Task.Deadline(m.group(1), m.group(2)))),
            new Command("event", "event [name] /from [start] /to [end]",
                    "event (.+) /from (.+) /to (.+)",
                    (t, m) -> t.addTask(new Task.Event(m.group(1), m.group(2), m.group(3)))),
            new Command("delete", "delete [id]", "delete (\\d+)",
                    (t, m) -> t.deleteTask(Integer.parseInt(m.group(1))))
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
        if (msg.equals("bye")) {
            return false;
        } else if (msg.equals("list")) {
            taskList.listOut();
            return true;
        }
        for (Command cmd : COMMANDS) {
            if (cmd.tryCommand(taskList, storage, msg)) {
                storage.writeTasks(taskList.getTasks());
                return true;
            }
        }
        tryFindCommand(msg);
        return true;
    }

    private void tryFindCommand(String msg) {
        ui.beginOutput();
        for (Command cmd : COMMANDS) {
            if (cmd.partialMatch(ui, msg)) {
                ui.endOutput();
                return;
            }
        }
        ui.printError("Sorry, I don't know what that command means.");
        ui.endOutput();
    }
}
