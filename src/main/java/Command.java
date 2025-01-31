import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private final String name;
    private final String prettyFormat;
    private final Pattern regex;
    private final BiFunction<TaskList, Matcher, Boolean> command;

    public Command(String name, String prettyFormat, String regex,
                   BiFunction<TaskList, Matcher, Boolean> command) {
        this.name = name;
        this.prettyFormat = prettyFormat;
        this.regex = Pattern.compile(regex);
        this.command = command;
    }

    public boolean tryCommand(TaskList taskList, Storage storage, String msg) {
        Matcher m = regex.matcher(msg);
        if (m.matches() && command.apply(taskList, m)) {
            storage.writeTasks(taskList.getTasks());
            return true;
        }
        return false;
    }

    public boolean partialMatch(Ui ui, String msg) {
        if (msg.startsWith(name)) {
            ui.printError("The proper format for %s is '%s'.",
                    name.toUpperCase(), prettyFormat);
            return true;
        }
        return false;
    }
}
