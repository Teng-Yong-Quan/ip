import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Yq {
    public static void main(String[] args) {
        String logo = " __   __    _________\n"
                +     "|  | |  |  |   ___   |\n"
                +     " \\     /   |  |  _|  |\n"
                +     "  |   |    |  |_|    |\n"
                +     "  |   |    |______   \\\n"
                +     "  |___|           \\___\\\n";
        System.out.println("Hello from\n" + logo);
        System.out.println();
        System.out.println("Hello! I'm Yq.");
        System.out.println("What can I do for you?");

        String line;
        Scanner userInput = new Scanner(System.in);
        while (true) {
            line = userInput.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                break;
            } else {
                System.out.println("    Ok. Processing...");
                // Process for 3 seconds after receiving command from user.
                processForThreeSeconds();

                System.out.println("    " + line);
                System.out.println("    Is there anything else I can do for you?");
                System.out.println();
            }
        }
        System.out.println("    Bye. Hope to see you again soon!");
    }

    /**
     * Imitates the processing time of 3 seconds by sleeping for 3 seconds.
     */
    public static void processForThreeSeconds() {
        try{
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException exception) {}
    }
}
