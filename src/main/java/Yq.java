import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Yq {
    public static void main(String[] args) {
        printWelcomeMessage();
        String userCommand;
        String lowerCaseUserCommand;
        // Create an empty main list.
        Task[] list = new Task[0];
        Scanner userInput = new Scanner(System.in);
        while (true) {
            printCommandOptions();
            userCommand = userInput.nextLine();
            userCommand = userCommand.trim();
            lowerCaseUserCommand = userCommand.toLowerCase();
            if (userCommand.equalsIgnoreCase("bye")) {
                break;
            } else if (userCommand.equalsIgnoreCase("list")) {
                printList(list);
            } else if (lowerCaseUserCommand.contains("unmark")) {
                processForOneSecond();
                String substringOfUnmarkCommand = userCommand.substring(
                        lowerCaseUserCommand.indexOf("unmark") + "unmark".length()).trim();
                if (list.length > 0) {
                    if (isValidUnmarkCommand(substringOfUnmarkCommand, list)) {
                        unmarkTask(list, substringOfUnmarkCommand);
                    }
                } else {
                    System.out.println("    The task list is empty.");
                }
            } else if (lowerCaseUserCommand.contains("mark")) {
                processForOneSecond();
                String substringOfMarkCommand = userCommand.substring(
                        lowerCaseUserCommand.indexOf("mark") + "mark".length()).trim();
                if (list.length > 0) {
                    if (isValidMarkCommand(substringOfMarkCommand, list)) {
                        markTask(list, substringOfMarkCommand);
                    }
                } else {
                    System.out.println("    The task list is empty.");
                }
            } else if (lowerCaseUserCommand.contains("todo") ||
                    lowerCaseUserCommand.contains("deadline") || lowerCaseUserCommand.contains("event")) {
                Task newTask = null;
                processForOneSecond();
                if (lowerCaseUserCommand.contains("todo")) {
                    String substringOfTodoCommand = userCommand.substring(
                            lowerCaseUserCommand.indexOf("todo") + "todo".length()).trim();
                    if (isValidTodoCommand(substringOfTodoCommand)) {
                        newTask = createTodo(substringOfTodoCommand);
                    }
                } else if (lowerCaseUserCommand.contains("deadline")) {
                    String substringOfDeadlineCommand = userCommand.substring(
                            lowerCaseUserCommand.indexOf("deadline") + "deadline".length()).trim();
                    if (isValidDeadlineCommand(substringOfDeadlineCommand)) {
                        newTask = createDeadline(substringOfDeadlineCommand);
                    }
                } else if (lowerCaseUserCommand.contains("event")) {
                    String substringOfEventCommand = userCommand.substring(
                            lowerCaseUserCommand.indexOf("event") + "event".length()).trim();
                    if (isValidEventCommand(substringOfEventCommand)) {
                        newTask = createEvent(substringOfEventCommand);
                    }
                }
                if (newTask != null) {
                    list = addTask(list, newTask);
                }
            } else {
                processForOneSecond();
                System.out.println("    Unknown option: " + userCommand);
                System.out.println("    Please enter a valid option.");
            }
            System.out.println();
            processForOneSecond();
            System.out.println("    Is there anything else I can do for you?" + "\n");
        }
        System.out.println("    Bye. Hope to see you again soon!");
    }

    private static void printWelcomeMessage() {
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
    }

    private static void printCommandOptions() {
        System.out.println("    Please key in one of the following options:");
        System.out.println("        list   - to show the list of tasks");
        System.out.println("            Example: list" + "\n");
        System.out.println("        unmark - to choose a task that you want to mark as not done");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    unmark 2" + "\n");
        System.out.println("        mark   - to choose a task that you want to mark as done");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    mark 1" + "\n");
        System.out.println("        Task type: todo/deadline/event - to add a task to the list");
        System.out.println("            Parameters for todo:     KEYWORD [TASK DESCRIPTION]");
        System.out.println("            Example:                 todo read book");
        System.out.println("            Parameters for deadline: KEYWORD [TASK DESCRIPTION] /by [DAY/DATE/TIME]");
        System.out.println("            Example:                 deadline return book /by Sunday");
        System.out.println("            Parameters for event:    KEYWORD [TASK DESCRIPTION] " +
                "/from [DAY/DATE/TIME] /to [DAY/DATE/TIME]");
        System.out.println("            Example:                 event project meeting /from Mon 2pm /to 4pm" + "\n");
        System.out.println("        bye    - to quit ");
        System.out.println("            Example: bye");
    }

    /**
     * Imitates the processing time of 1 second by sleeping for 1 second.
     */
    private static void processForOneSecond() {
        try {
            System.out.println("    Processing..." + "\n");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException interruptedException) {
        }
    }

    /**
     * Prints the list of tasks as requested by the user.
     *
     * @param list The task list.
     */
    private static void printList(Task[] list) {
        processForOneSecond();
        if (list.length > 0) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 0; i < list.length; i++) {
                Task selectedTask = list[i];
                System.out.println("    " + (i + 1) + ". "
                        + selectedTask.toString());
            }
        } else {
            System.out.println("    The list is empty. There is nothing to show.");
        }
    }

    private static boolean isValidUnmarkCommand(String substringOfUnmarkCommand, Task[] list) {
        if (!substringOfUnmarkCommand.isEmpty()) {
            try {
                int intValue = Integer.parseInt(substringOfUnmarkCommand);
                if (intValue > 0 && intValue <= list.length) {
                    System.out.println("    'Unmark' command is valid." + "\n");
                    return true;
                } else {
                    System.out.println("    The integer entered is out of range.");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("    No integer detected.");
            }
        }
        System.out.println("    Please enter a valid 'unmark' command.");
        return false;
    }

    private static boolean isValidMarkCommand(String substringOfMarkCommand, Task[] list) {
        if (!substringOfMarkCommand.isEmpty()) {
            try {
                int intValue = Integer.parseInt(substringOfMarkCommand);
                if (intValue > 0 && intValue <= list.length) {
                    System.out.println("    'Mark' command is valid." + "\n");
                    return true;
                } else {
                    System.out.println("    The integer entered is out of range.");
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println("    No integer detected.");
            }
        }
        System.out.println("    Please enter a valid 'mark' command.");
        return false;
    }

    /**
     * Marks the task, which is selected by the user, as not done.
     *
     * @param list                     The task list containing the selected task that is being marked as not done.
     * @param substringOfUnmarkCommand The substring of the 'unmark' command inputted by the user.
     */
    private static void unmarkTask(Task[] list, String substringOfUnmarkCommand) {
        int chosenUnmarkIndex = Integer.parseInt(substringOfUnmarkCommand);
        processForOneSecond();
        Task selectedTask = list[chosenUnmarkIndex - 1];
        selectedTask.markAsNotDone();
    }

    /**
     * Marks the task, which is selected by the user, as done.
     *
     * @param list                   The task list containing the selected task that is being marked as done.
     * @param substringOfMarkCommand The substring of the 'mark' command inputted by the user.
     */
    private static void markTask(Task[] list, String substringOfMarkCommand) {
        int chosenMarkIndex = Integer.parseInt(substringOfMarkCommand);
        processForOneSecond();
        Task selectedTask = list[chosenMarkIndex - 1];
        selectedTask.markAsDone();
    }

    private static boolean isValidTodoCommand(String substringOfTodoCommand) {
        if (!substringOfTodoCommand.isEmpty()) {
            System.out.println("    'Todo' command is valid." + "\n");
            return true;
        }
        System.out.println("    No todo task is being detected.");
        System.out.println("    Please enter a valid 'todo' command.");
        return false;
    }

    private static boolean isValidDeadlineCommand(String substringOfDeadlineCommand) {
        if (!substringOfDeadlineCommand.isEmpty()) {
            String lowerCaseSubstringOfDlCommand = substringOfDeadlineCommand.toLowerCase();
            if (lowerCaseSubstringOfDlCommand.contains("/by")) {
                int byIndex = lowerCaseSubstringOfDlCommand.indexOf("/by");
                String dlDescription = substringOfDeadlineCommand.substring(0, byIndex).trim();
                String by = substringOfDeadlineCommand.substring(byIndex + "/by".length()).trim();
                if (!dlDescription.isEmpty() && !by.isEmpty()) {
                    System.out.println("    'Deadline' command is valid." + "\n");
                    return true;
                }
            }
        } else {
            System.out.println("    No deadline task is being detected");
        }
        System.out.println("    Please enter a valid 'deadline' command.");
        return false;
    }

    private static boolean isValidEventCommand(String substringOfEventCommand) {
        if (!substringOfEventCommand.isEmpty()) {
            String lowerCaseSubstringOfEventCommand = substringOfEventCommand.toLowerCase();
            if (lowerCaseSubstringOfEventCommand.contains("/from")
                    && lowerCaseSubstringOfEventCommand.contains("/to")) {
                int fromIndex = lowerCaseSubstringOfEventCommand.indexOf("/from");
                int toIndex = lowerCaseSubstringOfEventCommand.indexOf("/to");
                String eventDescription = substringOfEventCommand.substring(0, fromIndex).trim();
                String from = substringOfEventCommand.substring(fromIndex + "/from".length(), toIndex).trim();
                String to = substringOfEventCommand.substring(toIndex + "/to".length()).trim();
                if (!eventDescription.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
                    System.out.println("    'Event' command is valid." + "\n");
                    return true;
                }
            }
        } else {
            System.out.println("    No event task is being detected");
        }
        System.out.println("    Please enter a valid 'event' command.");
        return false;
    }

    private static Task createTodo(String substringOfTodoCommand) {
        String taskDescription = substringOfTodoCommand.toLowerCase();
        return new ToDo(taskDescription);
    }

    private static Task createDeadline(String substringOfDeadlineCommand) {
        String lowerCaseSubstringOfDeadlineCommand = substringOfDeadlineCommand.toLowerCase();
        int byIndex = lowerCaseSubstringOfDeadlineCommand.indexOf("/by");
        String deadlineDescription = lowerCaseSubstringOfDeadlineCommand.substring(0, byIndex).trim();
        String by = substringOfDeadlineCommand.substring(byIndex + "/by".length()).trim();
        return new Deadline(deadlineDescription, by);
    }

    private static Task createEvent(String substringOfEventCommand) {
        String lowerCaseSubstringOfEventCommand = substringOfEventCommand.toLowerCase();
        int fromIndex = lowerCaseSubstringOfEventCommand.indexOf("/from");
        int toIndex = lowerCaseSubstringOfEventCommand.indexOf("/to");
        String eventDescription = lowerCaseSubstringOfEventCommand.substring(0, fromIndex).trim();
        String from = substringOfEventCommand.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = substringOfEventCommand.substring(toIndex + "/to".length()).trim();
        return new Event(eventDescription, from, to);
    }

    /**
     * Returns the updated list with the latest task being added.
     *
     * @param newTask The task description input by the user.
     * @param list    The task list.
     * @return Updated task list.
     */
    private static Task[] addTask(Task[] list, Task newTask) {
        processForOneSecond();
        // Create a new empty list that is longer than the main list by 1.
        Task[] newList = new Task[list.length + 1];
        // Transfer all tasks in the main list into the new list.
        System.arraycopy(list, 0, newList, 0, list.length);
        // Add latest task into the new list.
        newList[newList.length - 1] = newTask;
        // Set new list as the main list.
        list = newList;
        System.out.println("    Got it. I have added this task to the task list:");
        System.out.println("        " + newTask.toString());
        System.out.println("    Now you have " + list.length + " tasks in the list.");
        return list;
    }
}
