package yale;

public class Parser {

    private static final Command[] COMMANDS = {
            new Command("bye", (ui, t, m) -> {
                ui.goodbye();
                return false;
            }),
            new Command("list", (ui, t, m) -> {
                t.listOut();
                return false;
            }),
            new Command("mark", "[id]", "(\\d+)",
                    (ui, t, m) -> t.markDone(Integer.parseInt(m.group(1)), true)),
            new Command("unmark", "[id]", "(\\d+)",
                    (ui, t, m) -> t.markDone(Integer.parseInt(m.group(1)), false)),
            new Command("todo", "[name]", "(.+)",
                    (ui, t, m) -> t.addTask(new Task.ToDo(m.group(1)))),
            new Command("deadline", "[name] /by [date]", "(.+) /by (.+)",
                    (ui, t, m) -> t.addTask(new Task.Deadline(m.group(1), m.group(2)))),
            new Command("event", "[name] /from [start] /to [end]", "(.+) /from (.+) /to (.+)",
                    (ui, t, m) -> t.addTask(new Task.Event(m.group(1), m.group(2), m.group(3)))),
            new Command("delete", "[id]", "(\\d+)",
                    (ui, t, m) -> t.deleteTask(Integer.parseInt(m.group(1)))),
            new Command("find", "[keyword]", "(.+)",
                    (ui, t, m) -> t.listSearch(m.group(1)))
    };

    private final Ui ui;
    private final Storage storage;
    private final TaskList taskList;

    /**
     * Creates a Parser to control the flow of the program based on inputs.
     *
     * @param ui The Ui to display the output.
     * @param storage The Storage which reads and writes to the task file.
     * @param taskList The TaskList containing the tasks.
     */
    public Parser(Ui ui, Storage storage, TaskList taskList) {
        assert ui != null;
        assert storage != null;
        assert taskList != null;
        
        this.ui = ui;
        this.storage = storage;
        this.taskList = taskList;
    }

    /**
     * Performs different actions based on the input string.
     *
     * @param msg The input string to parse.
     */
    public void parseMsg(String msg) {
        for (Command cmd : COMMANDS) {
            if (cmd.tryCommand(ui, taskList, storage, msg)) {
                return;
            }
        }
        ui.printError("Sorry, I don't know what that command means.");
    }
}
