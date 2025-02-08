import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Yq {
    private static final int EMPTY_LIST_LENGTH = 0;
    private static final int LIST_INDEX_ADJUSTMENT = 1;
    private static final int ONE_SECOND = 1;
    private static final int START_INDEX_STRING_CMD = 0;
    // Shows the item at the index 0 of the string command.
    private static final int START_INDEX_LIST = 0;
    // Shows the item at the index 0 of the list.
    public static final int ADD_ONE_TASK = 1;

    public static void main(String[] args) {
        printWelcomeMessage();
        String userCmd; // Represents the user command with 'cmd' stands for 'command'.
        String lcUserCmd; // Represents the lower case of the user command with lc' stands for 'lower case'.
        String substringOfUserCmd; // Shows the substring of the user command.
        int indexAfterCmdWord; // Extracts the substring after the command word.
        Task newTask = null; // Assigns the newly created task a variable name called 'newTask'.
        Task[] list = new Task[EMPTY_LIST_LENGTH]; // Create an empty main list.
        int listLength = 0;
        Scanner userInput = new Scanner(System.in);
        while (true) {
            printCommandOptions();
            userCmd = userInput.nextLine();
            userCmd = userCmd.trim();
            // Standardise the user inputs to lower case, so it is easier to check for the command keywords.
            lcUserCmd = userCmd.toLowerCase();
            if (lcUserCmd.contains("bye")) {
                break;
            } else if (lcUserCmd.contains("list")) {
                printList(list);
            } else if (lcUserCmd.contains("unmark")) {
                printStraightLine();
                processForOneSecond();
                indexAfterCmdWord = lcUserCmd.indexOf("unmark") + "unmark".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                if (isValidUnmarkCmd(substringOfUserCmd, listLength)) {
                    unmarkTask(list, substringOfUserCmd);
                }
            } else if (lcUserCmd.contains("mark")) {
                printStraightLine();
                processForOneSecond();
                indexAfterCmdWord = lcUserCmd.indexOf("mark") + "mark".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                if (isValidMarkCmd(substringOfUserCmd, listLength)) {
                    markTask(list, substringOfUserCmd);
                }
            } else if (lcUserCmd.contains("todo")) {
                printStraightLine();
                processForOneSecond();
                indexAfterCmdWord = lcUserCmd.indexOf("todo") + "todo".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                if (isValidTodoCommand(substringOfUserCmd)) {
                    newTask = createTodo(substringOfUserCmd);
                }
            } else if (lcUserCmd.contains("deadline")) {
                printStraightLine();
                processForOneSecond();
                indexAfterCmdWord = lcUserCmd.indexOf("deadline") + "deadline".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                if (isValidDeadlineCmd(substringOfUserCmd)) {
                    newTask = createDeadline(substringOfUserCmd);
                }
            } else if (lcUserCmd.contains("event")) {
                printStraightLine();
                processForOneSecond();
                indexAfterCmdWord = lcUserCmd.indexOf("event") + "event".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                if (isValidEventCmd(substringOfUserCmd)) {
                    newTask = createEvent(substringOfUserCmd);
                }
            } else {
                printStraightLine();
                processForOneSecond();
                System.out.println("    Unknown option: " + userCmd);
                System.out.println("    Please enter a valid option.");
            }
            if (newTask != null) {
                list = addTask(list, newTask);
                newTask = null;
                listLength = list.length; // Update the variable 'listLength' with the length of the new task list.
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
            TimeUnit.SECONDS.sleep(ONE_SECOND);
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
        if (list.length > EMPTY_LIST_LENGTH) {
            System.out.println("    Here are the tasks in your list:");
            for (int i = 0; i < list.length; i++) {
                Task selectedTask = list[i];
                System.out.println("    " + (i + LIST_INDEX_ADJUSTMENT) + ". " + selectedTask.toString());
            }
        } else {
            System.out.println("    The task list is empty. There is nothing to show.");
        }
    }

    /**
     * Checks whether the substring of the 'unmark' command inputted by the user is valid. A message is printed to
     * inform users that the list is empty, hence there is no task for them to unmark. It ensures that the unmarking of
     * the task can only be carried out if the substring of the 'unmark' command is not empty, and it contains a valid
     * integer for extracting the task index from the list after passing the isValidInputInteger method.
     * Prints a reminder to input a valid 'unmark' command.
     *
     * @param listLength           The length of the task list.
     * @param substringOfUnmarkCmd The substring of the 'unmark' command inputted by the user after
     *                             the 'unmark' word is being verified and removed from the command.
     * @return The validity of the substring of 'unmark' command.
     */
    private static boolean isValidUnmarkCmd(String substringOfUnmarkCmd, int listLength) {
        if (listLength == EMPTY_LIST_LENGTH) {
            System.out.println("    The task list is empty. There is nothing to unmark.");
        } else if (substringOfUnmarkCmd.isEmpty()) {
            System.out.println("    The substring of the 'unmark' command is empty.");
        } else if (isValidInputInteger(substringOfUnmarkCmd, listLength)) {
            System.out.println("    The 'unmark' command is valid." + "\n");
            return true;
        }
        System.out.println("    Please enter a valid 'unmark' command.");
        return false;
    }

    /**
     * Checks for the presence of the integer in the input command. This method caters to both 'mark' and 'unmark'
     * commands. An exception message is printed when the substring of the command do not contain integers without
     * neighbouring characters. It also checks whether the integer stated in the command is out of range or valid by
     * comparing with the length of the list. An error message is printed when the integer detected is out of range.
     *
     * @param listLength     The length of the current Task list.
     * @param substringOfCmd The substring of the input 'mark' or 'unmark' command.
     * @return Returns the validity of the input integer.
     */
    private static boolean isValidInputInteger(String substringOfCmd, int listLength) {
        try {
            int inputInteger = Integer.parseInt(substringOfCmd);
            boolean isValidInteger = inputInteger > EMPTY_LIST_LENGTH && inputInteger <= listLength;
            if (isValidInteger) {
                System.out.println("    The integer is valid.");
            } else {
                System.out.println("    The integer is out of range.");
            }
            return isValidInteger;
        } catch (NumberFormatException numberFormatException) {
            System.out.println("    No integer detected.");
            return false;
        }
    }

    /**
     * Checks whether the substring of the 'mark' command inputted by the user is valid. A message is printed to
     * inform users that the list is empty, hence there is no task for them to mark. It ensures that the marking of
     * the task can only be carried out if the substring of the 'mark' command is not empty, and it contains a valid
     * integer for extracting the task index from the list after passing the isValidInputInteger method.
     * Prints a reminder to input a valid 'mark' command.
     *
     * @param listLength         The length of the task list.
     * @param substringOfMarkCmd The substring of the 'mark' command inputted by the user after
     *                           the 'mark' word is being verified and removed from the command.
     * @return The validity of the substring of 'mark' command.
     */
    private static boolean isValidMarkCmd(String substringOfMarkCmd, int listLength) {
        if (listLength == EMPTY_LIST_LENGTH) {
            System.out.println("    The task list is empty. There is nothing to mark.");
        } else if (substringOfMarkCmd.isEmpty()) {
            System.out.println("    The substring of the 'mark' command is empty.");
        } else if (isValidInputInteger(substringOfMarkCmd, listLength)) {
            System.out.println("    The 'mark' command is valid." +"\n");
            return true;
        }
        System.out.println("    Please enter a valid 'mark' command.");
        return false;
    }

    /**
     * Marks the task, which is selected by the user, as not done.
     *
     * @param list                 The task list containing the selected task that is being marked as not done.
     * @param substringOfUnmarkCmd The substring of the 'unmark' command inputted by the user.
     */
    private static void unmarkTask(Task[] list, String substringOfUnmarkCmd) {
        printStraightLine();
        processForOneSecond();
        int chosenUnmarkIndex = Integer.parseInt(substringOfUnmarkCmd);
        Task selectedTask = list[chosenUnmarkIndex - LIST_INDEX_ADJUSTMENT];
        selectedTask.markAsNotDone();
    }

    /**
     * Marks the task, which is selected by the user, as done.
     *
     * @param list               The task list containing the selected task that is being marked as done.
     * @param substringOfMarkCmd The substring of the 'mark' command inputted by the user.
     */
    private static void markTask(Task[] list, String substringOfMarkCmd) {
        printStraightLine();
        processForOneSecond();
        int chosenMarkIndex = Integer.parseInt(substringOfMarkCmd);
        Task selectedTask = list[chosenMarkIndex - LIST_INDEX_ADJUSTMENT];
        selectedTask.markAsDone();
    }

    /**
     * Checks whether the substring of the 'todo' command inputted by the user is empty.
     * Prints a message that indicates whether the 'todo' command is valid or it does not
     * contain the todo task.
     *
     * @param substringOfTodoCmd The substring of the 'todo' command inputted by the user after
     *                           the 'todo' word is being verified and removed from the command.
     * @return The validity of the substring of 'todo' command.
     */
    private static boolean isValidTodoCommand(String substringOfTodoCmd) {
        if (substringOfTodoCmd.isEmpty()) {
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
     * @param substringOfDeadlineCmd The substring of the 'deadline' command inputted by the user after
     *                               the 'deadline' word is being verified and removed from the command.
     * @return The validity of the substring of 'deadline' command.
     */
    private static boolean isValidDeadlineCmd(String substringOfDeadlineCmd) {
        String lcSubstringOfDeadlineCmd = substringOfDeadlineCmd.toLowerCase();
        if (lcSubstringOfDeadlineCmd.contains("/by")) {
            int byIndex = lcSubstringOfDeadlineCmd.indexOf("/by");
            int indexAfterByWord = byIndex + "/by".length();

            String deadlineDescription = substringOfDeadlineCmd.substring(START_INDEX_STRING_CMD, byIndex).trim();
            String by = substringOfDeadlineCmd.substring(indexAfterByWord).trim();

            if (!deadlineDescription.isEmpty() && !by.isEmpty()) {
                System.out.println("    'Deadline' command is valid." + "\n");
                return true;
            }
        } else if (substringOfDeadlineCmd.isEmpty()) {
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
     * @param substringOfEventCmd The substring of the 'event' command inputted by the user after
     *                            the 'event' word is being verified and removed from the command.
     * @return The validity of the substring of 'event' command.
     */
    private static boolean isValidEventCmd(String substringOfEventCmd) {
        String lcSubstringOfEventCmd = substringOfEventCmd.toLowerCase();
        if (lcSubstringOfEventCmd.contains("/from") && lcSubstringOfEventCmd.contains("/to")) {
            int fromIndex = lcSubstringOfEventCmd.indexOf("/from");
            int toIndex = lcSubstringOfEventCmd.indexOf("/to");
            int indexAfterFromWord = fromIndex + "/from".length();
            int indexAfterToWord = toIndex + "/to".length();

            String eventDescription = substringOfEventCmd.substring(START_INDEX_STRING_CMD, fromIndex).trim();
            String from = substringOfEventCmd.substring(indexAfterFromWord, toIndex).trim();
            String to = substringOfEventCmd.substring(indexAfterToWord).trim();

            if (!eventDescription.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
                System.out.println("    'Event' command is valid." + "\n");
                return true;
            }
        } else if (substringOfEventCmd.isEmpty()) {
            System.out.println("    No event task is being detected");
        }
        System.out.println("    Please enter a valid 'event' command.");
        return false;
    }

    /**
     * Creates a Todo task from the substring of the 'todo' command.
     *
     * @param substringOfTodoCmd The substring of the 'todo' command inputted by the user.
     * @return A new Todo Task.
     */

    private static Task createTodo(String substringOfTodoCmd) {
        return new ToDo(substringOfTodoCmd);
    }

    /**
     * Creates a Deadline task from the substring of the 'deadline' command. A duplicate of the substring of the
     * 'deadline' command in lower case is formed. This substring would act as a medium to check for the presence of the
     * '/by' keyword and extract the index of the keyword in the original substring if present. Based on the index of
     * '/by' keyword, the deadline task description and the deadline datetime could be partitioned and extracted.
     *
     * @param substringOfDeadlineCmd The substring of the 'deadline' command inputted by the user.
     * @return A new Deadline Task.
     */


    private static Task createDeadline(String substringOfDeadlineCmd) {
        String lcSubstringOfDeadlineCmd = substringOfDeadlineCmd.toLowerCase();
        int byIndex = lcSubstringOfDeadlineCmd.indexOf("/by");
        int indexAfterByWord = byIndex + "/by".length();

        String deadlineDescription = substringOfDeadlineCmd.substring(START_INDEX_STRING_CMD, byIndex).trim();
        String by = substringOfDeadlineCmd.substring(indexAfterByWord).trim();
        return new Deadline(deadlineDescription, by);
    }

    /**
     * Creates an Event task from the substring of the 'event' command. A duplicate of the substring of the 'event'
     * command in lower case is formed. This substring would act as a medium to check for the presence of the
     * '/from' and '/to' keywords and extract the indexes of the keywords in the original substring if present. Based on
     * the indexes of both '/from' and '/to' keywords, the event task description, start datetime and end datetime could
     * be partitioned and extracted.
     *
     * @param substringOfEventCmd The substring of the 'event' command inputted by the user.
     * @return A new Deadline Task.
     */

    private static Task createEvent(String substringOfEventCmd) {
        String lcSubstringOfEventCmd = substringOfEventCmd.toLowerCase();
        int fromIndex = lcSubstringOfEventCmd.indexOf("/from");
        int toIndex = lcSubstringOfEventCmd.indexOf("/to");
        int indexAfterFromWord = fromIndex + "/from".length();
        int indexAfterToWord = toIndex + "/to".length();

        String eventDescription = substringOfEventCmd.substring(START_INDEX_STRING_CMD, fromIndex).trim();
        String from = substringOfEventCmd.substring(indexAfterFromWord, toIndex).trim();
        String to = substringOfEventCmd.substring(indexAfterToWord).trim();
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
        Task[] newList = new Task[list.length + ADD_ONE_TASK];
        // Transfer all tasks in the main list into the new list.
        System.arraycopy(list, START_INDEX_LIST, newList, START_INDEX_LIST, list.length);
        // Add latest task into the new list.
        newList[newList.length - LIST_INDEX_ADJUSTMENT] = newTask;
        // Set new list as the main list.
        list = newList;
        System.out.println("    Got it. I have added this task to the task list:");
        System.out.println("        " + newTask.toString());
        System.out.println("    Now you have " + list.length + " tasks in the list.");
        return list;
    }
}
