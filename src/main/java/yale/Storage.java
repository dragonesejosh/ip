package yale;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {

    private final File file;

    /**
     * Creates a Storage which can read and write to a task file.
     *
     * @param filename Filename of the task file.
     */
    public Storage(String filename) {
        assert filename != null && !filename.isEmpty();
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
        } catch (Exception ignored) {}
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
        } catch (Exception ignored) {}
    }
}
