package yale;

public class Ui {
    private static final String NAME = "Yale";

    private StringBuilder output;

    /**
     * Begins outputting a response.
     */
    public Ui beginOutput() {
        output = new StringBuilder();
        return this;
    }

    /**
     * Finish outputting a response.
     */
    public String getOutput() {
        return output.toString();
    }

    /**
     * Display a formatted message to the user.
     *
     * @param msg The formatted message to display.
     * @param args The arguments of the message.
     */
    public Ui print(String msg, Object... args) {
        output.append(msg.formatted(args)).append("\n");
        return this;
    }

    /**
     * Display a formatted error message to the user.
     *
     * @param msg The formatted error message to display.
     * @param args The arguments of the error message.
     */
    public Ui printError(String msg, Object... args) {
        output.append("Error: ").append(msg.formatted(args)).append("\n");
        return this;
    }

    /**
     * Displays a greeting to the user.
     */
    public Ui greet() {
        return print("Hello! I'm %s.", NAME).print("What can I do for you?");
    }

    /**
     * Displays a goodbye message to the user.
     */
    public Ui goodbye() {
        return print("Bye. Hope to see you again soon!");
    }
}
