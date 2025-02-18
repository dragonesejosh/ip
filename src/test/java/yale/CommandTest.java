package yale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class CommandTest {
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

    private String getCommandOutput(Command command, String input) {
        DUMMY_UI.beginOutput();
        boolean success = command.tryCommand(
                DUMMY_UI,
                DUMMY_TASKLIST,
                DUMMY_STORAGE,
                input
        );
        return success + " " + DUMMY_UI.getOutput();
    }

    @Test
    public void regexTest() {
        Command command = new Command(
                "dummy",
                "[param1] /next [id]",
                "(.+) /next (\\d+)",
                (ui, t, m) -> {
                    ui.print(m.group(1) + "|" + m.group(2));
                    return false;
                },
                "dummy command");

        String msg = getCommandOutput(command, "dummy hello there /next 1234");
        assertTrue(msg.contains("hello there|1234"));

        msg = getCommandOutput(command, "dummy goodbye //next 1234");
        assertFalse(msg.contains("goodbye"));

        msg = getCommandOutput(command, "dummy greetings /next 12d34");
        assertFalse(msg.contains("greetings"));

        msg = getCommandOutput(command, "dummy complete /next 1234");
        assertTrue(msg.startsWith("true"));

        msg = getCommandOutput(command, "dummy incomplete");
        assertTrue(msg.startsWith("true"));

        msg = getCommandOutput(command, "dummycombine");
        assertTrue(msg.startsWith("true"));

        msg = getCommandOutput(command, "dumm");
        assertTrue(msg.startsWith("false"));
    }
}
