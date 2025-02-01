package yale;

/**
 * A chatbot which can interact with the user.
 */
public class Yale {
    private static final String TASKS_FILE = "tasks.txt";

    private final Ui ui;
    private final Parser parser;

    public static void main(String[] args) {
        new Yale(TASKS_FILE).run();
    }

    /**
     * Creates an instance of Yale which can be run.
     *
     * @param filename Filename of the task file
     */
    public Yale(String filename) {
        this.ui = new Ui();
        Storage storage = new Storage(filename);
        TaskList taskList = new TaskList(ui, storage.readTasks());
        this.parser = new Parser(ui, storage, taskList);
    }

    /**
     * Runs the chatbot until it exits.
     */
    public void run() {
        ui.greet();
        while (true) {
            String msg = ui.getUserInput();
            if (!parser.parseMsg(msg)) {
                break;
            }
        }
        ui.goodbye();
    }
}
