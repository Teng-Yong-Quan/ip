package yq.ui;

import yq.tasks.Task;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Ui {
    Scanner userInput;

    public Ui() {
        userInput = new Scanner(System.in);
    }

    void printWelcomeMessage() {
        printStraightLine();
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

    public void printStraightLine() {
        System.out.println("----------------------------------------------------------------------------------------");
    }

    void printInstructionToPrintList() {
        System.out.println("        list   - to show the list of tasks");
        System.out.println("            Example: list" + "\n");
    }

    void printInstructionToUnmarkTask() {
        System.out.println("        unmark - to choose a task that you want to mark as not done");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    unmark 2" + "\n");
    }

    void printInstructionToMarkTask() {
        System.out.println("        mark   - to choose a task that you want to mark as done");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    mark 1" + "\n");
    }

    void printInstructionToAddTask() {
        System.out.println("        Task type: todo/deadline/event - to add a task to the list");
        System.out.println("            Parameters for todo:     KEYWORD [TASK DESCRIPTION]");
        System.out.println("            Example:                 todo read book");
        System.out.println("            Parameters for deadline: KEYWORD [TASK DESCRIPTION] /by [DAY/DATE/TIME]");
        System.out.println("            Example:                 deadline return book /by Sunday");
        System.out.println("            Parameters for event:    KEYWORD [TASK DESCRIPTION] " +
                "/from [DAY/DATE/TIME] /to [DAY/DATE/TIME]");
        System.out.println("            Example:                 event project meeting /from Mon 2pm /to 4pm" + "\n");
    }

    void printInstructionToDeleteTask() {
        System.out.println("        delete - to choose a task that you want to delete from the list");
        System.out.println("            Parameters: KEYWORD [POSITIVE INTEGER]");
        System.out.println("            Example:    delete 3" + "\n");
    }

    void printInstructionToExit() {
        System.out.println("        bye    - to quit");
        System.out.println("            Example: bye");
    }

    void printCommandOptions() {
        printStraightLine();
        printKeyInFollowingOptions();
        printInstructionToPrintList();
        printInstructionToMarkTask();
        printInstructionToUnmarkTask();
        printInstructionToDeleteTask();
        printInstructionToAddTask();
        printInstructionToExit();
        printStraightLine();
    }

    private static void printKeyInFollowingOptions() {
        System.out.println("    Please key in one of the following options:");
    }

    void showError(String message) {
        System.out.println(message);
    }

    String readCommand() {
        String userCmd = "";
        if (userInput.hasNextLine()) {
            userCmd = userInput.nextLine();
            userCmd = userCmd.trim();
        }
        return userCmd;
    }

    /**
     * Imitates the processing time of 1 second by sleeping for 1 second.
     */
    public void processForOneSecond() {
        final int ONE_SECOND = 1;
        try {
            System.out.println("    Processing..." + "\n");
            TimeUnit.SECONDS.sleep(ONE_SECOND);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Adds the newly formed task into the task list and returns the updated list with the latest task being added.
     */
    public void printAddedTaskMessage(ArrayList<Task> taskArrayList, Task newTask) {
        System.out.println("    Got it. I have added this task to the task list:");
        System.out.println("        " + newTask.toString());
        System.out.println("    Now you have " + taskArrayList.size() + " tasks in the list.");
        System.out.println();
    }

    public void printAddedEventMessage(ArrayList<Task> taskArrayList, Task newTaskAdded) {
        System.out.println("    'Event' command is valid." + "\n");
        printAddedTaskMessage(taskArrayList, newTaskAdded);
    }

    public void printAddedDeadlineMessage(ArrayList<Task> taskArrayList, Task newTaskAdded) {
        System.out.println("    'Deadline' command is valid." + "\n");
        printAddedTaskMessage(taskArrayList, newTaskAdded);
    }

    public void printAddedTodoMessage(ArrayList<Task> taskArrayList, Task newTaskAdded) {
        System.out.println("    'Todo' command is valid." + "\n");
        printAddedTaskMessage(taskArrayList, newTaskAdded);
    }

    public void printGoodByeMessage() {
        userInput.close();
        System.out.println("    Bye. Hope to see you again soon!");
    }

    public void printDeletedTaskMessage(Task deletedTask, ArrayList<Task> taskArrayList) {
        System.out.println("    Noted. I have removed this task:");
        System.out.println("        " + deletedTask.toString());
        System.out.println("    Now you have " + taskArrayList.size() + " tasks in the list.");
        System.out.println();
    }

    public void printList(ArrayList<Task> taskArrayList) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < taskArrayList.size(); i++) {
            Task selectedTask = taskArrayList.get(i);
            System.out.println("    " + (i + LIST_INDEX_ADJUSTMENT) + ". " + selectedTask.toString());
        }
        System.out.println();
    }

    public void printTaskMarkedAsDone(Task selectedTask) {
        System.out.println("    The 'mark' command is valid." + "\n");
        selectedTask.markAsDone();
        System.out.println();
    }

    public void printTaskUnmarked(Task selectedTask) {
        System.out.println("    The 'unmark' command is valid." + "\n");
        selectedTask.markAsNotDone();
        System.out.println();
    }

    public void printIgnoreInvalidLineMessage() {
        printStraightLine();
        processForOneSecond();
        System.out.println("    An invalid line is detected and it will be ignored." + "\n");
    }

    public void printPrompt() {
        printStraightLine();
        processForOneSecond();
        System.out.println("    Is there anything else I can do for you?" + "\n");
    }

    public void printMatchingTaskList(ArrayList<Task> matchingTaskList) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        System.out.println("    Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTaskList.size(); i++) {
            Task selectedTask = matchingTaskList.get(i);
            System.out.println("    " + (i + LIST_INDEX_ADJUSTMENT) + ". " + selectedTask.toString());
        }
        System.out.println();
    }
}
