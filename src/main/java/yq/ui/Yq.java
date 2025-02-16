package yq.ui;

import yq.exceptions.EmptyDeadlineCommandException;
import yq.exceptions.EmptyEventCommandException;
import yq.exceptions.EmptyListException;
import yq.exceptions.InvalidCommandException;
import yq.exceptions.InvalidFromToIndexesException;
import yq.exceptions.MissingByKeywordException;
import yq.exceptions.MissingDeadlineDescriptionException;
import yq.exceptions.MissingDeleteNumberException;
import yq.exceptions.MissingEventDescriptionException;
import yq.exceptions.MissingFromKeywordException;
import yq.exceptions.MissingMarkNumberException;
import yq.exceptions.MissingToKeywordException;
import yq.exceptions.MissingTodoDescriptionException;
import yq.exceptions.MissingUnmarkNumberException;
import yq.tasks.Task;
import yq.tasks.Todo;
import yq.tasks.Deadline;
import yq.tasks.Event;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class Yq {
    private static final int LIST_INDEX_ADJUSTMENT = 1;
    private static final int ONE_SECOND = 1;
    private static final int START_INDEX_STRING_CMD = 0;
    private static final ArrayList<Task> taskArrayList = new ArrayList<>(); // Create an empty main list.

    public static void main(String[] args) {
        printWelcomeMessage();
        String userCmd; // Represents the user command with 'cmd' stands for 'command'.
        String lcUserCmd; // Represents the lower case of the user command with lc' stands for 'lower case'.
        String substringOfUserCmd; // Shows the substring of the user command.
        int indexAfterCmdWord; // Extracts the substring after the command word.
        Task newTask = null; // Assigns the newly created task a variable name called 'newTask'.
        Scanner userInput = new Scanner(System.in);
        while (true) {
            printCommandOptions();
            userCmd = userInput.nextLine();
            userCmd = userCmd.trim();
            // Standardise the user inputs to lower case, so it is easier to check for the command keywords.
            lcUserCmd = userCmd.toLowerCase();
            lcUserCmd = checkValidCommand(lcUserCmd);
            if (lcUserCmd.contains("bye")) {
                break;

            } else if (lcUserCmd.contains("list")) {
                checkValidPrintListCmd();

            } else if (lcUserCmd.contains("unmark")) {
                indexAfterCmdWord = lcUserCmd.indexOf("unmark") + "unmark".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                checkValidUnmarkCmd(substringOfUserCmd);

            } else if (lcUserCmd.contains("mark")) {
                indexAfterCmdWord = lcUserCmd.indexOf("mark") + "mark".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                checkValidMarkCmd(substringOfUserCmd);

            } else if (lcUserCmd.contains("todo")) {
                indexAfterCmdWord = lcUserCmd.indexOf("todo") + "todo".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                newTask = makeValidTodo(substringOfUserCmd);

            } else if (lcUserCmd.contains("deadline")) {
                indexAfterCmdWord = lcUserCmd.indexOf("deadline") + "deadline".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                newTask = makeValidDeadline(substringOfUserCmd);

            } else if (lcUserCmd.contains("event")) {
                indexAfterCmdWord = lcUserCmd.indexOf("event") + "event".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                newTask = makeValidEvent(substringOfUserCmd);

            } else if (lcUserCmd.contains("delete")) {
                indexAfterCmdWord = lcUserCmd.indexOf("delete") + "delete".length();
                substringOfUserCmd = userCmd.substring(indexAfterCmdWord).trim();
                checkValidDeleteCmd(substringOfUserCmd);

            }
            if (newTask != null) {
                addTask(newTask);
                newTask = null;
            }

            System.out.println();
            printStraightLine();
            processForOneSecond();
            System.out.println("    Is there anything else I can do for you?" + "\n");
        }
        System.out.println("    Bye. Hope to see you again soon!");
    }

    private static void printWelcomeMessage() {
        String logo = """
                 __    __    _________
                |  |  |  |  |   ___   |
                 \\  \\/  /   |  |  _|  |
                  \\    /    |  |_|    |
                   |  |     |______   \\
                   |__|            \\___\\
                """;
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

    private static void printInstructionToDeleteTask() {
        System.out.println("        delete - to choose a task that you want to delete from the list");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    delete 3" + "\n");
    }

    private static void printInstructionToExit() {
        System.out.println("        bye    - to quit");
        System.out.println("            Example: bye");
    }

    private static void printCommandOptions() {
        printStraightLine();
        System.out.println("    Please key in one of the following options:");
        printInstructionToPrintList();
        printInstructionToMarkTask();
        printInstructionToUnmarkTask();
        printInstructionToDeleteTask();
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
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks the command inputted by the user contains one of the following valid command: list, unmark, mark, todo,
     * deadline, event, delete and bye. If it is none of the above, an exception will be thrown.
     *
     * @param userCommand user command inputted by the user.
     * @return The valid user command.
     * @throws InvalidCommandException If the user command does not contain any one of the valid commands.
     */
    private static String returnValidCommand(String userCommand) throws InvalidCommandException {
        if (userCommand.contains("list") || userCommand.contains("unmark") || userCommand.contains("mark") ||
                userCommand.contains("todo") || userCommand.contains("deadline") || userCommand.contains("event")
                || userCommand.contains("delete") || userCommand.contains("bye")) {
            return userCommand;
        }
        throw new InvalidCommandException();
    }

    /**
     * Checks and returns the user command if it does not throw any exception. If the user command is not valid,
     * error messages will be generated to remind the user to input a valid command.
     *
     * @param userCommand User command inputted by the user.
     * @return User command if it is valid. Else, an empty string will be returned instead.
     */
    private static String checkValidCommand(String userCommand) {
        try {
            return returnValidCommand(userCommand);
        } catch (InvalidCommandException invalidCommandException) {
            printStraightLine();
            processForOneSecond();
            System.out.println("    Unknown option: " + userCommand);
            System.out.println("    Please enter a valid option.");
            return "";
        }
    }

    /**
     * Prints the list of tasks as requested by the user.
     *
     * @throws EmptyListException If the list is empty.
     */
    private static void printList() throws EmptyListException {
        printStraightLine();
        processForOneSecond();
        if (Yq.taskArrayList.isEmpty()) {
            throw new EmptyListException();

        }
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < Yq.taskArrayList.size(); i++) {
            Task selectedTask = Yq.taskArrayList.get(i);
            System.out.println("    " + (i + LIST_INDEX_ADJUSTMENT) + ". " + selectedTask.toString());
        }
    }

    private static void checkValidPrintListCmd() {
        try {
            printList();
        } catch (EmptyListException emptyListException) {
            System.out.println("    The task list is empty. There is nothing to show.");
        }
    }

    /**
     * Checks whether the substring of the 'unmark' command inputted by the user is valid. A message is printed to
     * inform users that the list is empty, hence there is no task for them to unmark. It ensures that the unmarking of
     * the task can only be carried out if the substring of the 'unmark' command is not empty, and it contains a valid
     * integer for extracting the task index from the list. An exception is thrown according to the error found in the
     * user's input. Prints a reminder to input a valid 'unmark' command.
     *
     * @param substringOfUnmarkCmd Substring of the 'unmark' command inputted by the user after
     *                             the 'unmark' word is being verified and removed from the command.
     */
    private static void checkValidUnmarkCmd(String substringOfUnmarkCmd) {
        try {
            unmarkTask(substringOfUnmarkCmd);
            return;
        } catch (EmptyListException emptyListException) {
            System.out.println("    The task list is empty. There is nothing to unmark.");
        } catch (MissingUnmarkNumberException missingUnmarkNumberException) {
            System.out.println("    The index of the task to be unmarked cannot be missing " +
                    "from the 'unmark' command.");
        } catch (NumberFormatException numberFormatException) {
            System.out.println("    A valid integer starting from 1 and nothing else must be present " +
                    "after the 'unmark' word.");
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.out.println("    The integer cannot be out of range.");
        }
        System.out.println("    Please enter a valid 'unmark' command.");
    }

    /**
     * Checks whether the substring of the 'mark' command inputted by the user is valid. A message is printed to
     * inform users that the list is empty, hence there is no task for them to mark. It ensures that the marking of
     * the task can only be carried out if the substring of the 'mark' command is not empty, and it contains a valid
     * integer for extracting the task index from the list. An exception is thrown according to the error found in the
     * user's input.  Prints a reminder to input a valid 'mark' command.
     *
     * @param substringOfMarkCmd Substring of the 'mark' command inputted by the user after
     *                           the 'mark' word is being verified and removed from the command.
     */
    private static void checkValidMarkCmd(String substringOfMarkCmd) {
        try {
            markTask(substringOfMarkCmd);
            return;
        } catch (EmptyListException emptyListException) {
            System.out.println("    The task list is empty. There is nothing to mark.");
        } catch (MissingMarkNumberException missingMarkNumberException) {
            System.out.println("    The index of the task to be marked cannot be missing from the 'mark' command.");
        } catch (NumberFormatException numberFormatException) {
            System.out.println("    A valid integer starting from 1 and nothing else must be present " +
                    "after the 'mark' word.");
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.out.println("    The integer cannot be out of range.");
        }
        System.out.println("    Please enter a valid 'mark' command.");
    }

    /**
     * Marks the task, which is selected by the user, as not done.
     *
     * @param substringOfUnmarkCmd Substring of the 'unmark' command inputted by the user.
     * @throws EmptyListException           If the list is empty.
     * @throws MissingUnmarkNumberException If the index of the task to be unmarked is not present in the user input.
     */
    private static void unmarkTask(String substringOfUnmarkCmd) throws EmptyListException,
            MissingUnmarkNumberException {
        printStraightLine();
        processForOneSecond();
        if (Yq.taskArrayList.isEmpty()) {
            throw new EmptyListException();
        } else if (substringOfUnmarkCmd.isEmpty()) {
            throw new MissingUnmarkNumberException();
        }
        int chosenUnmarkIndex = Integer.parseInt(substringOfUnmarkCmd);
        Task selectedTask = Yq.taskArrayList.get(chosenUnmarkIndex - LIST_INDEX_ADJUSTMENT);
        System.out.println("    The 'unmark' command is valid." + "\n");
        selectedTask.markAsNotDone();
    }

    /**
     * Marks the task, which is selected by the user, as done.
     *
     * @param substringOfMarkCmd Substring of the 'mark' command inputted by the user.
     * @throws EmptyListException         If the list is empty.
     * @throws MissingMarkNumberException If the index of the task to be marked is not present in the user input.
     */
    private static void markTask(String substringOfMarkCmd) throws EmptyListException,
            MissingMarkNumberException {
        printStraightLine();
        processForOneSecond();
        if (Yq.taskArrayList.isEmpty()) {
            throw new EmptyListException();
        } else if (substringOfMarkCmd.isEmpty()) {
            throw new MissingMarkNumberException();
        }
        int chosenMarkIndex = Integer.parseInt(substringOfMarkCmd);
        Task selectedTask = Yq.taskArrayList.get(chosenMarkIndex - LIST_INDEX_ADJUSTMENT);
        System.out.println("    The 'mark' command is valid." + "\n");
        selectedTask.markAsDone();
    }

    /**
     * Checks whether the substring of the 'todo' command inputted by the user is empty.
     * Prints a message that indicates whether the 'todo' command is valid or it does not
     * contain the todo task.
     *
     * @param substringOfTodoCmd Substring of the 'todo' command inputted by the user after
     *                           the 'todo' word is being verified and removed from the command.
     * @return The newly created Todo task if the substring of the 'todo' command is valid. Else, it returns null.
     */
    private static Task makeValidTodo(String substringOfTodoCmd) {
        try {
            return createTodo(substringOfTodoCmd);
        } catch (MissingTodoDescriptionException missingTodoDescriptionException) {
            System.out.println("    Todo description cannot be missing.");
            System.out.println("    Please enter a valid 'todo' command.");
        }
        return null;
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
     * @param substringOfDeadlineCmd Substring of the 'deadline' command inputted by the user after
     *                               the 'deadline' word is being verified and removed from the command.
     * @return The newly created Deadline Task if the substring of the 'deadline' command is valid.
     * Else, it returns null.
     */
    private static Task makeValidDeadline(String substringOfDeadlineCmd) {
        try {
            return createDeadline(substringOfDeadlineCmd);
        } catch (EmptyDeadlineCommandException emptyDeadlineCommandException) {
            System.out.println("    The substring of the 'deadline' command cannot be empty.");
        } catch (MissingByKeywordException missingByKeywordException) {
            System.out.println("    The '/by' keyword cannot be missing.");
        } catch (MissingDeadlineDescriptionException missingDeadlineDescriptionException) {
            System.out.println("    The deadline and '/by' descriptions cannot be missing.");
        }
        System.out.println("    Please enter a valid 'deadline' command.");
        return null;
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
     * @param substringOfEventCmd Substring of the 'event' command inputted by the user after
     *                            the 'event' word is being verified and removed from the command.
     * @return The validity of the substring of 'event' command.
     */
    private static Task makeValidEvent(String substringOfEventCmd) {
        try {
            return createEvent(substringOfEventCmd);
        } catch (EmptyEventCommandException emptyEventCommandException) {
            System.out.println("    The substring of the 'event' command cannot be empty.");
        } catch (MissingFromKeywordException missingFromKeywordException) {
            System.out.println("    The '/from' keyword cannot be missing.");
        } catch (MissingToKeywordException missingToKeywordException) {
            System.out.println("    The '/to' keyword cannot be missing.");
        } catch (InvalidFromToIndexesException invalidFromToIndexesException) {
            System.out.println("    The '/from' keyword cannot be inputted after the '/to' keyword.");
        } catch (MissingEventDescriptionException missingEventDescriptionException) {
            System.out.println("    The event, '/from' and '/to' descriptions cannot be missing.");
        }
        System.out.println("    Please enter a valid 'event' command.");
        return null;
    }

    /**
     * Creates a Todo task from the substring of the 'todo' command.
     *
     * @param substringOfTodoCmd Substring of the 'todo' command inputted by the user.
     * @return A new Todo Task.
     * @throws MissingTodoDescriptionException If the todo description is empty.
     */
    private static Task createTodo(String substringOfTodoCmd) throws MissingTodoDescriptionException {
        printStraightLine();
        processForOneSecond();
        if (substringOfTodoCmd.isEmpty()) {
            throw new MissingTodoDescriptionException();
        }
        System.out.println("    'Todo' command is valid." + "\n");
        return new Todo(substringOfTodoCmd);
    }

    /**
     * Creates a Deadline task from the substring of the 'deadline' command. A duplicate of the substring of the
     * 'deadline' command in lower case is formed. This substring would act as a medium to check for the presence of the
     * '/by' keyword and extract the index of the keyword in the original substring if present. Based on the index of
     * '/by' keyword, the deadline task description and the deadline datetime could be partitioned and extracted. An
     * exception is thrown according to the error found in the user's input.
     *
     * @param substringOfDeadlineCmd Substring of the 'deadline' command inputted by the user.
     * @return A new Deadline Task.
     * @throws EmptyDeadlineCommandException       If the user input only contains the 'deadline' word.
     * @throws MissingByKeywordException           If the '/by' keyword is missing.
     * @throws MissingDeadlineDescriptionException If the deadline command does not contain deadline description and/or
     *                                             'by' description.
     */
    private static Task createDeadline(String substringOfDeadlineCmd) throws EmptyDeadlineCommandException,
            MissingByKeywordException, MissingDeadlineDescriptionException {
        printStraightLine();
        processForOneSecond();
        String lcSubstringOfDeadlineCmd = substringOfDeadlineCmd.toLowerCase();
        if (substringOfDeadlineCmd.isEmpty()) {
            throw new EmptyDeadlineCommandException();
        } else if (!lcSubstringOfDeadlineCmd.contains("/by")) {
            throw new MissingByKeywordException();
        }
        int byIndex = lcSubstringOfDeadlineCmd.indexOf("/by");
        int indexAfterByWord = byIndex + "/by".length();
        String deadlineDescription = substringOfDeadlineCmd.substring(START_INDEX_STRING_CMD, byIndex).trim();
        String by = substringOfDeadlineCmd.substring(indexAfterByWord).trim();
        if (deadlineDescription.isEmpty() || by.isEmpty()) {
            throw new MissingDeadlineDescriptionException();
        }
        System.out.println("    'Deadline' command is valid." + "\n");
        return new Deadline(deadlineDescription, by);
    }

    /**
     * Creates an Event task from the substring of the 'event' command. A duplicate of the substring of the 'event'
     * command in lower case is formed. This substring would act as a medium to check for the presence of the
     * '/from' and '/to' keywords and extract the indexes of the keywords in the original substring if present. Based on
     * the indexes of both '/from' and '/to' keywords, the event task description, start datetime and end datetime could
     * be partitioned and extracted. An exception is thrown according to the error found in the user's input.
     *
     * @param substringOfEventCmd Substring of the 'event' command inputted by the user.
     * @return A new Event Task.
     * @throws EmptyEventCommandException       If the user input only contains the 'event' word and nothing else.
     * @throws MissingFromKeywordException      If the '/from' keyword is missing.
     * @throws MissingToKeywordException        If the '/to' keyword is missing.
     * @throws InvalidFromToIndexesException    If the '/to' keyword appears before the '/from' keyword.
     * @throws MissingEventDescriptionException If the event command does not contain the event description and/or
     *                                          'from' description and/or 'by' description.
     */
    private static Task createEvent(String substringOfEventCmd) throws EmptyEventCommandException,
            MissingFromKeywordException, MissingToKeywordException, InvalidFromToIndexesException,
            MissingEventDescriptionException {
        printStraightLine();
        processForOneSecond();
        String lcSubstringOfEventCmd = substringOfEventCmd.toLowerCase();
        if (substringOfEventCmd.isEmpty()) {
            throw new EmptyEventCommandException();
        } else if (!lcSubstringOfEventCmd.contains("/from")) {
            throw new MissingFromKeywordException();
        } else if (!lcSubstringOfEventCmd.contains("/to")) {
            throw new MissingToKeywordException();
        }
        int fromIndex = lcSubstringOfEventCmd.indexOf("/from");
        int toIndex = lcSubstringOfEventCmd.indexOf("/to");
        int indexAfterFromWord = fromIndex + "/from".length();
        int indexAfterToWord = toIndex + "/to".length();
        if (fromIndex >= toIndex) {
            throw new InvalidFromToIndexesException();
        }
        String eventDescription = substringOfEventCmd.substring(START_INDEX_STRING_CMD, fromIndex).trim();
        String from = substringOfEventCmd.substring(indexAfterFromWord, toIndex).trim();
        String to = substringOfEventCmd.substring(indexAfterToWord).trim();
        if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new MissingEventDescriptionException();
        }
        System.out.println("    'Event' command is valid." + "\n");
        return new Event(eventDescription, from, to);
    }

    /**
     * Adds the newly formed task into the task list and returns the updated list with the latest task being added.
     *
     * @param newTask New task to be added into the task list.
     */
    private static void addTask(Task newTask) {
        // Add latest task into the new list.
        Yq.taskArrayList.add(newTask);
        // Set new list as the main list.
        System.out.println("    Got it. I have added this task to the task list:");
        System.out.println("        " + newTask.toString());
        System.out.println("    Now you have " + Yq.taskArrayList.size() + " tasks in the list.");
    }

    /**
     * Delete the task from the task list according to the index selected by the user.
     *
     * @param substringOfDeleteCmd Substring of the delete command inputted by the user.
     * @throws EmptyListException           If the list is empty.
     * @throws MissingDeleteNumberException If the index of the task to be deleted is not present in the user input.
     */
    private static void deleteTask(String substringOfDeleteCmd) throws EmptyListException,
            MissingDeleteNumberException {
        printStraightLine();
        processForOneSecond();
        if (Yq.taskArrayList.isEmpty()) {
            throw new EmptyListException();
        } else if (substringOfDeleteCmd.isEmpty()) {
            throw new MissingDeleteNumberException();
        }
        int chosenDeleteIndex = Integer.parseInt(substringOfDeleteCmd);
        Task deletedTask = Yq.taskArrayList.remove(chosenDeleteIndex - LIST_INDEX_ADJUSTMENT);
        System.out.println("    Noted. I have removed this task:");
        System.out.println("        " + deletedTask.toString());
        System.out.println("    Now you have " + Yq.taskArrayList.size() + " tasks in the list.");
    }

    /**
     * Checks whether the substring of the 'delete' command inputted by the user is valid. A message is printed to
     * inform users that the list is empty, hence there is no task for them to delete. It ensures that the deleting of
     * the task can only be carried out if the substring of the 'delete' command is not empty, and it contains a valid
     * integer for extracting the task index from the list. An exception is thrown according to the error found in the
     * user's input.  Prints a reminder to input a valid 'delete' command.
     *
     * @param substringOfDeleteCmd Substring of the 'delete' command inputted by the user after
     *                             the 'delete' word is being verified and removed from the command.
     */
    private static void checkValidDeleteCmd(String substringOfDeleteCmd) {
        try {
            deleteTask(substringOfDeleteCmd);
            return;
        } catch (EmptyListException emptyListException) {
            System.out.println("    The task list is empty. There is nothing to delete.");
        } catch (MissingDeleteNumberException missingDeleteNumberException) {
            System.out.println("    The index of the task to be deleted cannot be missing from the 'delete' command.");
        } catch (NumberFormatException numberFormatException) {
            System.out.println("    A valid integer starting from 1 and nothing else must be present " +
                    "after the 'delete' word.");
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.out.println("    The integer cannot be out of range.");
        }
        System.out.println("    Please enter a valid 'delete' command.");
    }
}
