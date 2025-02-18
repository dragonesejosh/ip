package yale;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ParserTest {
    public static final Ui DUMMY_UI = new Ui();
    public static final TaskList DUMMY_TASKLIST =
            new TaskList(DUMMY_UI, new ArrayList<>());
    public static final Storage DUMMY_STORAGE = new Storage(DUMMY_UI, "dummy_file") {
        @Override
        public ArrayList<Task> readTasks() {
            return new ArrayList<>();
        }

        @Override
        public void writeTasks(ArrayList<Task> tasks) {}
    };

    @Test
    public void parseTest() {
        Parser parser = new Parser(DUMMY_UI, DUMMY_STORAGE, DUMMY_TASKLIST);

        DUMMY_UI.beginOutput();
        parser.parseMsg("bye");
        String msg = DUMMY_UI.getOutput();
        assertTrue(msg.startsWith("Bye. Hope to see you again soon!"));

        DUMMY_UI.beginOutput();
        parser.parseMsg("list");
        msg = DUMMY_UI.getOutput();
        assertTrue(msg.startsWith("Here are the tasks in your list:"));
    }
}
