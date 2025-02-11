package yale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {

    private final Ui ui;
    private final File file;

    /**
     * Creates a Storage which can read and write to a task file.
     *
     * @param filename Filename of the task file.
     */
    public Storage(Ui ui, String filename) {
        assert filename != null && !filename.isEmpty();
        this.ui = ui;
        this.file = new File(filename);
    }

    /**
     * Reads in the tasks in the task file and outputs them.
     *
     * @return ArrayList of the tasks in the file.
     */
    public ArrayList<Task> readTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        try (Scanner in = new Scanner(file)) {
            while (in.hasNextLine()) {
                Task task = Task.fromCsv(in.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
        return tasks;
    }

    /**
     * Writes the tasks to the task file.
     *
     * @param tasks ArrayList of the tasks to write to the file.
     */
    public void writeTasks(ArrayList<Task> tasks) {
        try (PrintWriter out = new PrintWriter(file)) {
            tasks.stream().map(Task::toCsv).forEach(out::println);
            out.flush();
        } catch (FileNotFoundException e) {
            ui.printError("Cannot write tasks to file: %s", e.getMessage());
        }
    }
}
