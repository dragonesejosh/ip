public class Yale {
    private static final String NAME = "Yale";
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        greet();
        goodbye();
    }

    private static void greet() {
        System.out.println(LINE);
        System.out.printf("Hello! I'm %s.\n", NAME);
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void goodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
