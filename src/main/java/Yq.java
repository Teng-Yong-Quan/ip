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
        // Create an empty main list.
        String[] list = new String[0];
        Scanner userInput = new Scanner(System.in);
        while (true) {
            line = userInput.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                break;
            } else if (line.equalsIgnoreCase("list")) {
                System.out.println("    Ok. Processing...");
                // Process for 3 seconds after receiving command from user.
                processForThreeSeconds();
                System.out.println();

                if (list.length == 0) {
                    System.out.println("    The list is empty. There is nothing to show.");
                } else {
                    for (int i = 0; i < list.length; i++) {
                        if (list[i] != null) {
                            System.out.println("    " + Integer.toString(i + 1) + ": " + list[i]);
                        }
                    }
                }
            } else {
                System.out.println("    Ok. Processing...");
                // Process for 3 seconds after receiving command from user.
                processForThreeSeconds();
                System.out.println();

                // Create a new list that is longer than the main list by 1.
                String[] newList = new String[list.length + 1];

                // Transfer all items in the main list into the new list.
                System.arraycopy(list, 0, newList, 0, list.length);

                // Add latest item into the new list.
                newList[newList.length - 1] = line;

                // Set new list as the main list.
                list = newList;
                System.out.println("    added: " + line);
            }
            System.out.println();
            System.out.println("    Is there anything else I can do for you?");
            System.out.println();
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
