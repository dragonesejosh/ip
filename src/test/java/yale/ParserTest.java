package yale;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
    @Test
    public void parseTest() {
        Parser parser = new Parser(Dummy.DUMMY_UI, Dummy.DUMMY_STORAGE, Dummy.DUMMY_TASKLIST);

        assertFalse(parser.parseMsg("bye"));

        assertTrue(parser.parseMsg("list"));
    }
}
