package yale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private final String name;
    private final String prettyFormat;
    private final Pattern regex;
    private final TriFunction command;
    private final String helpDetails;

    @FunctionalInterface
    public interface TriFunction {
        boolean apply(Ui ui, TaskList taskList, Matcher matcher);
    }

    @FunctionalInterface
    public interface TriConsumer {
        void apply(Ui ui, TaskList taskList, Matcher matcher);
    }

    /**
     * Creates a Command which can be matched against inputs and
     * run its function if matched.
     *
     * @param name The name of the command. e.g. "deadline"
     * @param params The parameters of the command displayed to the user. e.g. "[name] /by [date]"
     * @param regex The regex of the params for matching against the command. e.g. "(.+) /by (.+)"
     * @param command The function to run if matched, which returns true if updating tasks.
     */
    public Command(String name, String params, String regex,
                   TriFunction command, String helpDetails) {
        assert name != null && !name.isEmpty();
        assert params != null && !params.isEmpty();
        assert regex != null && !regex.isEmpty();
        assert command != null;
        assert helpDetails != null && !helpDetails.isEmpty();
  
        this.name = name;
        this.prettyFormat = name + " " + params;
        this.regex = Pattern.compile(name + " " + regex);
        this.command = command;
        this.helpDetails = helpDetails;
    }

    public Command(String name, TriConsumer command, String helpDetails) {
        this.name = name;
        this.prettyFormat = name;
        this.regex = Pattern.compile(name);
        this.command = (ui, t, m) -> {
            command.apply(ui, t, m);
            return false;
        };
        this.helpDetails = helpDetails;
    }

    /**
     * Tests if the input string matches or partially matches
     * the regex, and outputs the result.
     * If it matches fully, it also runs its function.
     *
     * @param taskList The TaskList containing the tasks.
     * @param storage The Storage which reads and writes to the task file.
     * @param msg The input string to test against.
     * @return true if it matches, false otherwise.
     */
    public boolean tryCommand(Ui ui, TaskList taskList, Storage storage, String msg) {
        assert ui != null;
        assert taskList != null;
        assert storage != null;
        assert msg != null;

        if (!msg.startsWith(name)) {
            return false;
        }

        Matcher m = regex.matcher(msg);
        if (!m.matches()) {
            ui.printError("The proper format for %s is '%s'.",
                    name.toUpperCase(), prettyFormat);
            return true;
        }

        if (command.apply(ui, taskList, m)) {
            storage.writeTasks(taskList.getTasks());
        }
        return true;
    }

    public String toString() {
        return "COMMAND: %s\n%s".formatted(prettyFormat, helpDetails);
    }
}
