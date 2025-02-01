package yale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class CommandTest {
    @Test
    public void regexTest() {
        String[] output = {""};
        Command command = new Command("", "",
                "dummy (.+) /next (\\d+)",
                (t, m) -> {
                    output[0] = m.group(1) + "|" + m.group(2);
                    return false;
                });

        command.tryCommand(null, null,
                "dummy hello there /next 1234");
        assertEquals("hello there|1234", output[0]);

        output[0] = "";
        command.tryCommand(null, null,
                "dummy goodbye //next 1234");
        assertEquals("", output[0]);

        output[0] = "";
        command.tryCommand(null, null,
                "dummy greetings /next 12d34");
        assertEquals("", output[0]);
    }

    @Test
    public void partialMatchTest() {
        Command command = new Command("dummy", "", "",
                (t, m) -> false);

        assertTrue(command.partialMatch(Dummy.DUMMY_UI, "dummy complete /next 1234"));

        assertTrue(command.partialMatch(Dummy.DUMMY_UI, "dummy incomplete"));

        assertTrue(command.partialMatch(Dummy.DUMMY_UI, "dummycombine"));

        assertFalse(command.partialMatch(Dummy.DUMMY_UI, "dumm"));
    }
}
