import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Yq {
    public static void main(String[] args) {
        printWelcomeMessage();
        String userCommand;
        String lowerCaseUserCommand;
        Task[] list = new Task[0]; // Create an empty main list.
        Scanner userInput = new Scanner(System.in);
        while (true) {
            printCommandOptions();
            userCommand = userInput.nextLine();
            userCommand = userCommand.trim();
            // Standardise the user inputs to lower case, so it is easier to check for the command keywords.
            lowerCaseUserCommand = userCommand.toLowerCase();
            if (lowerCaseUserCommand.contains("bye")) {
                break;
            } else if (lowerCaseUserCommand.contains("list")) {
                printList(list);
            } else if (lowerCaseUserCommand.contains("unmark")) {
                printStraightLine();
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
                printStraightLine();
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
                printStraightLine();
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
                printStraightLine();
                processForOneSecond();
                System.out.println("    Unknown option: " + userCommand);
                System.out.println("    Please enter a valid option.");
            }
            System.out.println();
            printStraightLine();
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

    private static void printStraightLine() {
        System.out.println("----------------------------------------------------------------------------------------");
    }

    private static void printInstructionToPrintList() {
        System.out.println("        list   - to show the list of tasks");
        System.out.println("            Example: list" + "\n");
    }

    private static void printInstructionToUnmarkTask() {
        System.out.println("        unmark - to choose a task that you want to mark as not done");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    unmark 2" + "\n");
    }

    private static void printInstructionToMarkTask() {
        System.out.println("        mark   - to choose a task that you want to mark as done");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    mark 1" + "\n");
    }

    private static void printInstructionToAddTask() {
        System.out.println("        Task type: todo/deadline/event - to add a task to the list");
        System.out.println("            Parameters for todo:     KEYWORD [TASK DESCRIPTION]");
        System.out.println("            Example:                 todo read book");
        System.out.println("            Parameters for deadline: KEYWORD [TASK DESCRIPTION] /by [DAY/DATE/TIME]");
        System.out.println("            Example:                 deadline return book /by Sunday");
        System.out.println("            Parameters for event:    KEYWORD [TASK DESCRIPTION] " +
                "/from [DAY/DATE/TIME] /to [DAY/DATE/TIME]");
        System.out.println("            Example:                 event project meeting /from Mon 2pm /to 4pm" + "\n");
    }

    private static void printInstructionToExit() {
        System.out.println("        bye    - to quit");
        System.out.println("            Example: bye");
    }

    private static void printCommandOptions() {
        printStraightLine();
        System.out.println("    Please key in one of the following options:");
        printInstructionToPrintList();
        printInstructionToUnmarkTask();
        printInstructionToMarkTask();
        printInstructionToAddTask();
        printInstructionToExit();
        printStraightLine();
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
        printStraightLine();
        processForOneSecond();
        if (list.length > 0) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 0; i < list.length; i++) {
                Task selectedTask = list[i];
                System.out.println("    " + (i + 1) + ". " + selectedTask.toString());
            }
        } else {
            System.out.println("    The list is empty. There is nothing to show.");
        }
    }

    /**
     * Checks whether the substring of the 'unmark' command inputted by the user is valid by detecting
     * integers without neighbouring characters. An exception message is printed when the substring of the
     * 'unmark' command do not contain integers without neighbouring characters.
     * It also checks whether the integer stated in the command is out of range or valid by comparing with
     * the length of the list. An error message is printed when the integer detected is out of range.
     * Prints a reminder to input a valid 'unmark' command.
     *
     * @param list                     The task list which allows the extraction of its length.
     * @param substringOfUnmarkCommand The substring of the 'unmark' command inputted by the user after
     *                                 the 'unmark' word is being verified and removed from the command.
     * @return The validity of the substring of 'unmark' command.
     */
    private static boolean isValidUnmarkCommand(String substringOfUnmarkCommand, Task[] list) {
        printStraightLine();
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

    /**
     * Checks whether the substring of the 'mark' command inputted by the user is valid by detecting
     * integers without neighbouring characters. An exception message is printed when the substring of the
     * 'mark' command do not contain integers without neighbouring characters.
     * It also checks whether the integer stated in the command is out of range or valid by comparing with
     * the length of the list. An error message is printed when the integer detected is out of range.
     * Prints a reminder to input a valid 'mark' command.
     *
     * @param list                   The task list which allows the extraction of its length.
     * @param substringOfMarkCommand The substring of the 'mark' command inputted by the user after
     *                               the 'mark' word is being verified and removed from the command.
     * @return The validity of the substring of 'mark' command.
     */
    private static boolean isValidMarkCommand(String substringOfMarkCommand, Task[] list) {
        printStraightLine();
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

    /**
     * Checks whether the substring of the 'todo' command inputted by the user is empty.
     * Prints a message that indicates whether the 'todo' command is valid or it does not
     * contain the todo task.
     *
     * @param substringOfTodoCommand The substring of the 'todo' command inputted by the user after
     *                               the 'todo' word is being verified and removed from the command.
     * @return The validity of the substring of 'todo' command.
     */
    private static boolean isValidTodoCommand(String substringOfTodoCommand) {
        if (substringOfTodoCommand.isEmpty()) {
            System.out.println("    No todo task is being detected.");
            System.out.println("    Please enter a valid 'todo' command.");
            return false;
        }
        System.out.println("    'Todo' command is valid." + "\n");
        return true;
    }

    /**
     * Checks whether the substring of the 'deadline' command inputted by the user is valid. A duplicate of the
     * substring of the 'deadline' command in lower case is formed. This substring would act as a medium to check for
     * the presence of the '/by' keyword and extract the index of the '/by' keyword in the original substring if
     * present. Based on the index of the '/by' keyword, the deadline task description and the deadline datetime ('by')
     * could be partitioned and extracted. The substring of the 'deadline' is valid only if it contains both the
     * deadline task description and the deadline datetime. Else, an error message and a remainder to input a valid
     * deadline command is printed.
     *
     * @param substringOfDeadlineCommand The substring of the 'deadline' command inputted by the user after
     *                                   the 'deadline' word is being verified and removed from the command.
     * @return The validity of the substring of 'deadline' command.
     */
    private static boolean isValidDeadlineCommand(String substringOfDeadlineCommand) {
        String lowerCaseSubstringOfDeadlineCommand = substringOfDeadlineCommand.toLowerCase();
        if (lowerCaseSubstringOfDeadlineCommand.contains("/by")) {
            int byIndex = lowerCaseSubstringOfDeadlineCommand.indexOf("/by");
            String deadlineDescription = substringOfDeadlineCommand.substring(0, byIndex).trim();
            String by = substringOfDeadlineCommand.substring(byIndex + "/by".length()).trim();
            if (!deadlineDescription.isEmpty() && !by.isEmpty()) {
                System.out.println("    'Deadline' command is valid." + "\n");
                return true;
            }
        } else if (substringOfDeadlineCommand.isEmpty()) {
            System.out.println("    No deadline task is being detected");
        }
        System.out.println("    Please enter a valid 'deadline' command.");
        return false;
    }

    /**
     * Checks whether the substring of the 'event' command inputted by the user is valid. A duplicate of the
     * substring of the 'event' command in lower case is formed. This substring would act as a medium to check for
     * the presence of the '/from' and '/to' keywords and extract the indexes of the keywords in the original substring
     * if present. Based on the indexes of '/from' and '/to' keywords, the event task description, the event start
     * datetime and end datetime could be partitioned and extracted. The substring of the 'event' command is valid only
     * if it contains the event description, start datetime and end datetime. Else, an error message and a remainder to
     * input a valid deadline command is printed.
     *
     * @param substringOfEventCommand The substring of the 'event' command inputted by the user after
     *                                the 'event' word is being verified and removed from the command.
     * @return The validity of the substring of 'event' command.
     */
    private static boolean isValidEventCommand(String substringOfEventCommand) {
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
        } else if (substringOfEventCommand.isEmpty()) {
            System.out.println("    No event task is being detected");
        }
        System.out.println("    Please enter a valid 'event' command.");
        return false;
    }

    /**
     * Creates a Todo task from the substring of the 'todo' command.
     *
     * @param substringOfTodoCommand The substring of the 'todo' command inputted by the user.
     * @return A new Todo Task.
     */

    private static Task createTodo(String substringOfTodoCommand) {
        return new ToDo(substringOfTodoCommand);
    }

    /**
     * Creates a Deadline task from the substring of the 'deadline' command. A duplicate of the substring of the
     * 'deadline' command in lower case is formed. This substring would act as a medium to check for the presence of the
     * '/by' keyword and extract the index of the keyword in the original substring if present. Based on the index of
     * '/by' keyword, the deadline task description and the deadline datetime could be partitioned and extracted.
     *
     * @param substringOfDeadlineCommand The substring of the 'deadline' command inputted by the user.
     * @return A new Deadline Task.
     */


    private static Task createDeadline(String substringOfDeadlineCommand) {
        String lowerCaseSubstringOfDeadlineCommand = substringOfDeadlineCommand.toLowerCase();
        int byIndex = lowerCaseSubstringOfDeadlineCommand.indexOf("/by");
        String deadlineDescription = substringOfDeadlineCommand.substring(0, byIndex).trim();
        String by = substringOfDeadlineCommand.substring(byIndex + "/by".length()).trim();
        return new Deadline(deadlineDescription, by);
    }

    /**
     * Creates an Event task from the substring of the 'event' command. A duplicate of the substring of the 'event'
     * command in lower case is formed. This substring would act as a medium to check for the presence of the
     * '/from' and '/to' keywords and extract the indexes of the keywords in the original substring if present. Based on
     * the indexes of both '/from' and '/to' keywords, the event task description, start datetime and end datetime could
     * be partitioned and extracted.
     *
     * @param substringOfEventCommand The substring of the 'event' command inputted by the user.
     * @return A new Deadline Task.
     */

    private static Task createEvent(String substringOfEventCommand) {
        String lowerCaseSubstringOfEventCommand = substringOfEventCommand.toLowerCase();
        int fromIndex = lowerCaseSubstringOfEventCommand.indexOf("/from");
        int toIndex = lowerCaseSubstringOfEventCommand.indexOf("/to");
        String eventDescription = substringOfEventCommand.substring(0, fromIndex).trim();
        String from = substringOfEventCommand.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = substringOfEventCommand.substring(toIndex + "/to".length()).trim();
        return new Event(eventDescription, from, to);
    }

    /**
     * Adds the newly formed task into the task list and returns the updated list with the latest task being added.
     *
     * @param newTask The task description input by the user.
     * @param list    The task list.
     * @return Updated task list.
     */
    private static Task[] addTask(Task[] list, Task newTask) {
        printStraightLine();
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
