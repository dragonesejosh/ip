package yale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private final String name;
    private final String prettyFormat;
    private final Pattern regex;
    private final TriFunction command;

    @FunctionalInterface
    public interface TriFunction {
        boolean apply(Ui ui, TaskList taskList, Matcher matcher);
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
                   TriFunction command) {
        this.name = name;
        this.prettyFormat = name + " " + params;
        this.regex = Pattern.compile(name + " " + regex);
        this.command = command;
    }

    public Command(String name, TriFunction command) {
        this.name = name;
        this.prettyFormat = name;
        this.regex = Pattern.compile(name);
        this.command = command;
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
}
