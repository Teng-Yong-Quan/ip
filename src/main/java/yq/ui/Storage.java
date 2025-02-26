package yq.ui;


import yq.commands.Command;
import yq.exceptions.InvalidCommandException;
import yq.exceptions.YqException;
import yq.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private static final ArrayList<String> TASKS_FROM_FILE_ARRAY_LIST = new ArrayList<>();
    private String fileName;
    private static ArrayList<Task> taskArrayList = new ArrayList<>();
    static final int TASK_TYPE_INDEX = 1; // Shows the task type T (Todo), D (Deadline) and E (Event).
    static final int TASK_DESCRIPTION_INDEX = 7; // The index whereby it shows the full task description.

    Storage(String fileName, Ui ui) {
        ui.printStraightLine();
        System.out.println("    Starting..." + "\n");
        setFileName(fileName);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    ArrayList<Task> load(Ui ui) throws IOException, InvalidCommandException {
        getTasksFromFile();
        populateTaskArraylist(ui);
        return taskArrayList;
    }

    void showFileNotFoundError() {
        System.out.println("    " + fileName + " file is not found." + "\n");
    }

    private void getTasksFromFile() throws FileNotFoundException {
        File file = new File(this.getFileName());
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            TASKS_FROM_FILE_ARRAY_LIST.add(fileScanner.nextLine());
        }
        fileScanner.close();
    }

    private void populateTaskArraylist(Ui ui) {
        ui.printStraightLine();
        ui.processForOneSecond();
        System.out.println("    Retrieving saved tasks from " + fileName + " file " +
                "and populating task arraylist..." + "\n");
        if (TASKS_FROM_FILE_ARRAY_LIST.isEmpty()) {
            System.out.println("    There is nothing to retrieve from " + fileName + " file." + "\n");
            return;
        }
        for (String taskDescription : TASKS_FROM_FILE_ARRAY_LIST) {
            checkValidTaskLine(ui, taskDescription);
        }
    }

    private static void checkValidTaskLine(Ui ui, String taskDescription) {
        try {
            extractTaskFromFile(ui, taskDescription);
        } catch (YqException yqException) {
            ui.showError(yqException.getMessage());
        }
    }

    private static void extractTaskFromFile(Ui ui, String taskDescription) throws YqException {
        String finalTaskDescription;
        taskDescription = taskDescription.trim();
        finalTaskDescription = convertTaskDescription(taskDescription);
        if (finalTaskDescription.isEmpty()) {
            ui.printIgnoreInvalidLineMessage();
            return;
        }
        processAutoParsedCommand(ui, finalTaskDescription);
        markExtractedTask(ui, taskDescription);
    }

    private static void processAutoParsedCommand(Ui ui, String finalTaskDescription) throws YqException {
        Command autoParsedCommand = Parser.autoParse(finalTaskDescription);
        ui.printStraightLine();
        ui.processForOneSecond();
        autoParsedCommand.autoExecute(taskArrayList, ui);
    }

    private static void markExtractedTask(Ui ui, String taskDescription) throws YqException {
        final int MARK_OR_UNMARK_INDEX = 4; // The index whereby it shows the task is marked or unmarked.
        final char MARK_SYMBOL = 'X';
        if (taskDescription.charAt(MARK_OR_UNMARK_INDEX) == MARK_SYMBOL) {
            autoMark(taskArrayList, ui);
        }
    }

    private static String convertTaskDescription(String taskDescription) {
        String finalTaskDescription = "";
        if (!getFinalTodoDescription(taskDescription).isEmpty()) {
            finalTaskDescription = getFinalTodoDescription(taskDescription);
        } else if (!getFinalDlDescription(taskDescription).isEmpty()) {
            finalTaskDescription = getFinalTodoDescription(taskDescription);
        } else if (!getFinalEventDescription(taskDescription).isEmpty()) {
            finalTaskDescription = getFinalEventDescription(taskDescription);
        }
        return finalTaskDescription;
    }

    private static String getFinalTodoDescription(String taskDescription) {
        final char TODO_CHARACTER = 'T';
        final String TODO_WORD = "todo ";
        String finalTaskDescription = "";
        if (taskDescription.charAt(TASK_TYPE_INDEX) == TODO_CHARACTER) {
            finalTaskDescription = TODO_WORD + taskDescription.substring(TASK_DESCRIPTION_INDEX).trim();
        }
        return finalTaskDescription;
    }

    private static String getFinalDlDescription(String taskDescription) {
        final char DEADLINE_CHARACTER = 'D';
        final String DEADLINE_WORD = "deadline ";
        String finalTaskDescription = "";
        if (taskDescription.charAt(TASK_TYPE_INDEX) == DEADLINE_CHARACTER) {
            taskDescription = modifyDeadlineDescription(taskDescription);
            finalTaskDescription = DEADLINE_WORD + taskDescription.substring(TASK_DESCRIPTION_INDEX).trim();
        }
        return finalTaskDescription;
    }

    private static String getFinalEventDescription(String taskDescription) {
        final char EVENT_CHARACTER = 'E';
        final String EVENT_WORD = "event ";
        String finalTaskDescription = "";
        if (taskDescription.charAt(TASK_TYPE_INDEX) == EVENT_CHARACTER) {
            taskDescription = modifyEventDescription(taskDescription);
            finalTaskDescription = EVENT_WORD + taskDescription.substring(TASK_DESCRIPTION_INDEX).trim();
        }
        return finalTaskDescription;
    }

    private static void autoMark(ArrayList<Task> taskArrayList, Ui ui) throws YqException {
        final String MARK_WORD = "mark ";
        Command parsedMarkCommand;
        String markCommand = MARK_WORD + taskArrayList.size();
        parsedMarkCommand = Parser.parse(markCommand);
        ui.printStraightLine();
        ui.processForOneSecond();
        parsedMarkCommand.autoExecute(taskArrayList, ui);
    }

    private static String modifyDeadlineDescription(String deadlineDescription) {
        final String ORIGINAL_BY = "by:";
        final String EDITED_BY = "/by";
        final String OPEN_BRACKET = "(";
        final String CLOSE_BRACKET = ")";
        final String EMPTY_STRING = "";
        deadlineDescription = deadlineDescription.replace(ORIGINAL_BY, EDITED_BY);
        deadlineDescription = deadlineDescription.replace(OPEN_BRACKET, EMPTY_STRING);
        deadlineDescription = deadlineDescription.replace(CLOSE_BRACKET, EMPTY_STRING);
        return deadlineDescription;
    }

    private static String modifyEventDescription(String eventDescription) {
        final String ORIGINAL_FROM = "from:";
        final String EDITED_FROM = "/from";
        final String ORIGINAL_TO = "to:";
        final String EDITED_TO = "/to";
        final String OPEN_BRACKET = "(";
        final String CLOSE_BRACKET = ")";
        final String EMPTY_STRING = "";
        eventDescription = eventDescription.replace(ORIGINAL_FROM, EDITED_FROM);
        eventDescription = eventDescription.replace(ORIGINAL_TO, EDITED_TO);
        eventDescription = eventDescription.replace(OPEN_BRACKET, EMPTY_STRING);
        eventDescription = eventDescription.replace(CLOSE_BRACKET, EMPTY_STRING);
        return eventDescription;
    }

    public void writeToFile() throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
        FileWriter fileWriter = new FileWriter(fileName, true);
        if (taskArrayList.isEmpty()) {
            System.out.println("    There are no tasks to be saved into " + fileName + " file." + "\n");
            fileWriter.close();
            return;
        }
        writeTaskToFile(fileWriter);
        System.out.println();
        fileWriter.close();
    }

    private void writeTaskToFile(FileWriter fileWriter) throws IOException {
        int count = 1;
        System.out.println("    The following tasks have been saved into " + fileName + " file: ");
        for (Task task : taskArrayList) {
            fileWriter.write(task.toString() + "\n");
            System.out.println("    " + count + ": " + task);
            count++;
        }
    }

    public void saveTaskArraylist(TaskList taskList, ArrayList<Task> newTaskArrayList) {
        taskArrayList = newTaskArrayList;
        taskList.setTaskArrayList(taskArrayList);
    }

}
