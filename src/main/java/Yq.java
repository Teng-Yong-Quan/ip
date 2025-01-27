import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Yq {
    public static void main(String[] args) {
        String logo = " __   __    _________\n"
                + "|  | |  |  |   ___   |\n"
                + " \\     /   |  |  _|  |\n"
                + "  |   |    |  |_|    |\n"
                + "  |   |    |______   \\\n"
                + "  |___|           \\___\\\n";
        System.out.println("Hello from\n" + logo);
        System.out.println();
        System.out.println("Hello! I'm Yq.");
        System.out.println("What can I do for you?");

        String line;
        // Create an empty main list
        Task[] list = new Task[0];

        Scanner userInput = new Scanner(System.in);
        while (true) {
            // Process for 1 second after receiving command from the user
            processForOneSecond();
            line = userInput.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                break;
            } else if (line.equalsIgnoreCase("list")) {
                printList(list);
            } else if (line.contains("unmark")) {
                int spacingPosition = line.indexOf(' ');
                int chosenUnmarkIndex = Integer.parseInt(line.substring(spacingPosition + 1));
                unmarkTask(chosenUnmarkIndex, list);
            } else if (line.contains("mark")) {
                int spacingPosition = line.indexOf(' ');
                int chosenMarkIndex = Integer.parseInt(line.substring(spacingPosition + 1));
                markTask(chosenMarkIndex, list);
            } else {
                list = addTask(line, list);
            }
            processForOneSecond();
            System.out.println();
            System.out.println("    Is there anything else I can do for you?");
            System.out.println();
        }
        System.out.println("    Bye. Hope to see you again soon!");
    }

    /**
     * Imitates the processing time of 1 second by sleeping for 1 second.
     */
    public static void processForOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException exception) {
        }
    }

    /**
     * Prints the list of tasks as requested by the user.
     *
     * @param list The task list.
     */
    public static void printList(Task[] list) {
        System.out.println("    Ok. Processing...");
        processForOneSecond();
        System.out.println();

        if (list.length == 0) {
            System.out.println("    The list is empty. There is nothing to show.");
        } else {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 0; i < list.length; i++) {
                Task selectedTask = list[i];
                System.out.println("    " + Integer.toString(i + 1) + ": "
                        + "[" + selectedTask.getStatusIcon() + "] "
                        + selectedTask.getDescription());
            }
        }
    }

    /**
     * Returns the updated list with the latest task being added.
     *
     * @param line The task description input by the user.
     * @param list The task list.
     * @return Updated list.
     */
    public static Task[] addTask(String line, Task[] list) {
        System.out.println("    Ok. Processing...");
        processForOneSecond();
        System.out.println();

        //Create a new task
        Task newTask = new Task(line);
        // Create a new list that is longer than the main list by 1
        Task[] newList = new Task[list.length + 1];

        // Transfer all tasks in the main list into the new list
        System.arraycopy(list, 0, newList, 0, list.length);

        // Add latest task into the new list
        newList[newList.length - 1] = newTask;

        // Set new list as the main list
        list = newList;
        System.out.println("    added: " + line);
        return list;
    }

    /**
     * Marks the task, which is selected by the user, as done.
     *
     * @param num  Task number or position in the list.
     * @param list The task list.
     */
    public static void markTask(int num, Task[] list) {
        System.out.println("    Ok. Processing...");
        processForOneSecond();
        if (list.length == 0) {
            System.out.println();
            System.out.println("    There is nothing in the task list to mark as done.");
        } else {
            // Check whether the number input by the user is a valid index to extract the
            // respective task from the task list
            try {
                Task selectedTask = list[num - 1];
                System.out.println();
                selectedTask.markAsDone();
            } catch (ArrayIndexOutOfBoundsException exception) {
                // Catch exception and print error message if the number input is either
                // less than or equals to 0 or larger than the length of the task list
                System.out.println();
                System.out.println("    Invalid number is either too small or too large.");
                if (list.length == 1) {
                    // Print error message when there is only 1 task in the task list
                    System.out.println("    Please try again by entering 1 as there is only "
                            + "1 task in the task list.");
                } else {
                    // Print error message when there are more than 1 tasks in the task list
                    System.out.println("    Please try again by entering a valid number "
                            + "ranging from 1 to " + list.length + ".");
                }
            }
        }
    }

    /**
     * Marks the task, which is selected by the user, as not done.
     *
     * @param num  Task number or position in the list.
     * @param list The task list.
     */
    public static void unmarkTask(int num, Task[] list) {
        System.out.println("    Ok. Processing...");
        processForOneSecond();
        if (list.length == 0) {
            System.out.println();
            System.out.println("    There is nothing in the task list to mark as not done.");
        } else {
            try {
                // Check whether the number input by the user is a valid index to extract the
                // respective task from the task list
                Task selectedTask = list[num - 1];
                System.out.println();
                selectedTask.markAsNotDone();
            } catch (ArrayIndexOutOfBoundsException exception) {
                // Catch exception and print error message if the number input is either
                // less than or equals to 0 or larger than the length of the task list
                System.out.println();
                System.out.println("    Invalid number is either too small or too large.");
                if (list.length == 1) {
                    // Print error message when there is only 1 task in the task list
                    System.out.println("    Please try again by entering 1 as there is only "
                            + "1 task in the task list.");
                } else {
                    // Print error message when there are more than 1 tasks in the task list
                    System.out.println("    Please try again by entering a valid number "
                            + "ranging from 1 to " + list.length + ".");
                }
            }
        }
    }
}
