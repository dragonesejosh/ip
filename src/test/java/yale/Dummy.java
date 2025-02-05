package yale;

import java.util.ArrayList;

public class Dummy {
    public static final Ui DUMMY_UI = new Ui() {
        @Override
        public String getUserInput() {
            return "";
        }

        @Override
        public void beginOutput() {}

        @Override
        public void getOutput() {}

        @Override
        public void print(String msg, Object... args) {}

        @Override
        public void printError(String msg, Object... args) {}

        @Override
        public void greet() {}

        @Override
        public void goodbye() {}
    };
    public static final TaskList DUMMY_TASKLIST =
            new TaskList(DUMMY_UI, new ArrayList<>());
    public static final Storage DUMMY_STORAGE = new Storage("") {
        @Override
        public ArrayList<Task> readTasks() {
            return new ArrayList<>();
        }

        @Override
        public void writeTasks(ArrayList<Task> tasks) {}
    };
}
