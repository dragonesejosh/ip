package yale;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private final String name;
    private final String prettyFormat;
    private final Pattern regex;
    private final BiFunction<TaskList, Matcher, Boolean> command;

    /**
     * Creates a Command which can be matched against inputs and
     * run its function if matched.
     *
     * @param name The simple name of the command.
     * @param prettyFormat The format of the command displayed to the user.
     * @param regex The regex for matching against the command.
     * @param command The function to run if matched, which returns true if updating tasks.
     */
    public Command(String name, String prettyFormat, String regex,
                   BiFunction<TaskList, Matcher, Boolean> command) {
        this.name = name;
        this.prettyFormat = prettyFormat;
        this.regex = Pattern.compile(regex);
        this.command = command;
    }

    /**
     * Tests if the input string matches the regex, and outputs the result.
     * If it matches, it also runs its function.
     *
     * @param taskList The TaskList containing the tasks.
     * @param storage The Storage which reads and writes to the task file.
     * @param msg The input string to test against.
     * @return true if it matches, false otherwise.
     */
    public boolean tryCommand(TaskList taskList, Storage storage, String msg) {
        Matcher m = regex.matcher(msg);
        if (m.matches() && command.apply(taskList, m)) {
            storage.writeTasks(taskList.getTasks());
            return true;
        }
        return false;
    }

    /**
     * Tests if the input string is a partial match,
     * that is if it starts with the simple name of the Command.
     *
     * @param ui The Ui to display the output.
     * @param msg The input string to test against.
     * @return true if it is a partial match, false otherwise.
     */
    public boolean partialMatch(Ui ui, String msg) {
        if (msg.startsWith(name)) {
            ui.printError("The proper format for %s is '%s'.",
                    name.toUpperCase(), prettyFormat);
            return true;
        }
        return false;
    }
}
