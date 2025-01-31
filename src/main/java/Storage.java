import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {

    private final File file;

    public Storage(String filename) {
        this.file = new File(filename);
    }

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

    public void writeTasks(ArrayList<Task> tasks) {
        try (PrintWriter out = new PrintWriter(file)) {
            for (Task task : tasks) {
                out.println(task.toCsv());
            }
            out.flush();
        } catch (Exception ignored) {}
    }
}
