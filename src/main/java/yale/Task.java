package yale;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class Task {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    protected final String name;
    protected boolean isDone = false;

    private Task(String name) {
        assert name != null;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public String toString() {
        return "[%s] %s".formatted(isDone ? "X" : " ", name);
    }

    protected abstract String[] getParams();

    /**
     * Returns the Task as a row in a CSV file.
     */
    public String toCsv() {
        return String.join("\0", getParams());
    }

    /**
     * Creates a Task from a row in a CSV file.
     *
     * @param csvString The row from the CSV file.
     * @return The created Task, or null if tf the format was incorrect.
     */
    public static Task fromCsv(String csvString) {
        assert csvString != null;
        String[] tokens = csvString.split("\0");
        Task task =  switch (tokens.length) {
            case 2 -> new ToDo(tokens[1]);
            case 3 -> new Deadline(tokens[1], tokens[2]);
            case 4 -> new Event(tokens[1], tokens[2], tokens[3]);
            default -> null;
        };
        if (task != null && tokens[0].equals("X")) {
            task.setDone(true);
        }
        return task;
    }

    private static LocalDate tryParseDate(String dateStr) {
        assert dateStr != null;
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * A Task which has no due date.
     */
    public static class ToDo extends Task {
        public ToDo(String name) {
            super(name);
        }

        public String toString() {
            return "[T]%s".formatted(super.toString());
        }

        protected String[] getParams() {
            return new String[] {isDone ? "X" : " ", name};
        }
    }

    /**
     * A Task which is due at a certain date.
     */
    public static class Deadline extends Task {
        private final String deadlineStr;
        private final LocalDate deadlineDate;

        public Deadline(String name, String deadline) {
            super(name);
            this.deadlineDate = tryParseDate(deadline);
            this.deadlineStr = (this.deadlineDate == null) ? deadline : this.deadlineDate.format(DATE_FORMAT);
        }

        public String toString() {
            return "[D]%s (by: %s)".formatted(super.toString(), deadlineStr);
        }

        protected String[] getParams() {
            return new String[] {isDone ? "X" : " ", name, deadlineStr};
        }
    }

    /**
     * A Task which starts and ends at certain dates.
     */
    public static class Event extends Task {
        private final String startStr;
        private final String endStr;
        private final LocalDate startDate;
        private final LocalDate endDate;

        public Event(String name, String start, String end) {
            super(name);
            this.startDate = tryParseDate(start);
            this.startStr = (this.startDate == null) ? start : this.startDate.format(DATE_FORMAT);
            this.endDate = tryParseDate(end);
            this.endStr = (this.endDate == null) ? end : this.endDate.format(DATE_FORMAT);
        }

        public String toString() {
            return "[E]%s (from: %s, to: %s)".formatted(super.toString(), startStr, endStr);
        }

        protected String[] getParams() {
            return new String[]{isDone ? "X" : " ", name, startStr, endStr};
        }
    }
}