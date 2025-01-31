package yale;

public class Yale {
    private static final String TASKS_FILE = "tasks.txt";

    private final Ui ui;
    private final Parser parser;

    public static void main(String[] args) {
        new Yale(TASKS_FILE).run();
    }

    public Yale(String filename) {
        this.ui = new Ui();
        Storage storage = new Storage(filename);
        TaskList taskList = new TaskList(ui, storage.readTasks());
        this.parser = new Parser(ui, storage, taskList);
    }

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
