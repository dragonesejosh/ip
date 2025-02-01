package yale;

import java.util.ArrayList;

public class TaskList {

    private final Ui ui;
    private final ArrayList<Task> tasks;

    /**
     * Creates a TaskList which holds tasks and modifies them.
     *
     * @param ui The Ui to display the output.
     * @param tasks The TaskList containing the tasks.
     */
    public TaskList(Ui ui, ArrayList<Task> tasks) {
        this.ui = ui;
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Lists out the tasks in the Ui.
     */
    public void listOut() {
        ui.beginOutput();
        ui.print("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.print("%d.%s", i+1, tasks.get(i));
        }
        ui.endOutput();
    }

    /**
     * Adds a new task to the tasklist.
     *
     * @param task The task to be added.
     * @return true to signify that the tasks were updated.
     */
    public boolean addTask(Task task) {
        ui.beginOutput();
        tasks.add(task);
        ui.print("Got it. I've added this task:");
        ui.print("  %s", task);
        ui.print("Now you have %d task%s in the list.",
                tasks.size(), tasks.size() == 1 ? "" : "s");
        ui.endOutput();
        return true;
    }

    /**
     * Deletes a task from the tasklist by id.
     * Fails if the id is out of range.
     *
     * @param id The id of the task to be deleted.
     * @return true if the task was deleted, false otherwise.
     */
    public boolean deleteTask(int id) {
        ui.beginOutput();
        if (checkInvalidID(id)) {
            ui.endOutput();
            return false;
        }
        Task task = tasks.remove(id-1);
        ui.print("Noted. I've removed this task:");
        ui.print("  %s", task);
        ui.print("Now you have %d task%s in the list.",
                tasks.size(), tasks.size() == 1 ? "" : "s");
        ui.endOutput();
        return true;
    }

    private boolean checkInvalidID(int id) {
        if (tasks.isEmpty()) {
            ui.printError("You don't have any tasks!");
            return true;
        }
        if (id > tasks.size() || id <= 0) {
            ui.printError("The id should be from 1 to %d.", tasks.size());
            return true;
        }
        return false;
    }

    /**
     * Marks or unmarks the task by id.
     *
     * @param id The id of the task to mark or unmark.
     * @param done true to mark, false to unmark.
     * @return true if successful, false otherwise.
     */
    public boolean markDone(int id, boolean done) {
        ui.beginOutput();
        if (checkInvalidID(id)) {
            ui.endOutput();
            return false;
        }
        Task task = tasks.get(id-1);
        task.setDone(done);
        if (done) {
            ui.print("Nice! I've marked this task as done:");
        } else {
            ui.print("OK, I've marked this task as not done yet:");
        }
        ui.print("  %s", task);
        ui.endOutput();
        return true;
    }
}
