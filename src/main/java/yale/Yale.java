package yale;

/**
 * A chatbot which can interact with the user.
 */
public class Yale {
    private static final String TASKS_FILE = "tasks.txt";

    private final Ui ui;
    private final Parser parser;

    public Yale() {
        this(TASKS_FILE);
    }

    /**
     * Creates an instance of Yale which can be run.
     *
     * @param filename Filename of the task file
     */
    public Yale(String filename) {
        this.ui = new Ui();
        Storage storage = new Storage(ui, filename);
        TaskList taskList = new TaskList(ui, storage.readTasks());
        this.parser = new Parser(ui, storage, taskList);
    }

    public String greet() {
        return ui.beginOutput().greet().getOutput();
    }

    public String getResponse(String input) {
        ui.beginOutput();
        parser.parseMsg(input);
        return ui.getOutput();
    }
}
