package yq.ui;


import yq.commands.Command;
import yq.datetime.DateTimeHandler;
import yq.exceptions.InvalidCommandException;
import yq.exceptions.InvalidTimeIntervalException;
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
    private static final Ui UI = new Ui();
    private String fileName;
    private static ArrayList<Task> taskArrayList = new ArrayList<>();
    static final int TASK_TYPE_INDEX = 1; // Shows the task type T (Todo), D (Deadline) and E (Event).
    static final int TASK_DESCRIPTION_INDEX = 7; // The index whereby it shows the full task description.

    Storage(String fileName) {
        UI.printStraightLine();
        System.out.println("    Starting..." + "\n");
        setFileName(fileName);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    ArrayList<Task> load() throws IOException, InvalidCommandException {
        getTasksFromFile();
        populateTaskArraylist();
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

    private void populateTaskArraylist() {
        UI.printStraightLine();
        UI.processForOneSecond();
        System.out.println("    Retrieving saved tasks from " + fileName + " file " +
                "and populating task arraylist..." + "\n");
        if (TASKS_FROM_FILE_ARRAY_LIST.isEmpty()) {
            System.out.println("    There is nothing to retrieve from " + fileName + " file." + "\n");
            return;
        }
        for (String taskDescription : TASKS_FROM_FILE_ARRAY_LIST) {
            UI.printStraightLine();
            UI.processForOneSecond();
            checkValidTaskLine(taskDescription);
        }
    }

    private static void checkValidTaskLine(String taskDescription) {
        try {
            extractTaskFromFile(taskDescription);
        } catch (YqException yqException) {
            UI.showError(yqException.getMessage());
        }
    }

    private static void extractTaskFromFile(String taskDescription) throws YqException {
        String finalTaskDescription;
        taskDescription = taskDescription.trim();
        finalTaskDescription = convertTaskDescription(taskDescription);
        if (finalTaskDescription.isEmpty()) {
            UI.printIgnoreInvalidLineMessage();
            return;
        }
        processAutoParsedCommand(finalTaskDescription);
        markExtractedTask(taskDescription);
    }

    private static void processAutoParsedCommand(String finalTaskDescription) throws YqException {
        Command autoParsedCommand = Parser.autoParse(finalTaskDescription);
        autoParsedCommand.autoExecute(taskArrayList, UI);
    }

    private static void markExtractedTask(String taskDescription) throws YqException {
        final int MARK_OR_UNMARK_INDEX = 4; // The index whereby it shows the task is marked or unmarked.
        final char MARK_SYMBOL = 'X';
        if (taskDescription.charAt(MARK_OR_UNMARK_INDEX) == MARK_SYMBOL) {
            autoMark(taskArrayList);
        }
    }

    private static String convertTaskDescription(String taskDescription) throws YqException {
        String finalTaskDescription = "";
        if (!getFinalTodoDescription(taskDescription).isEmpty()) {
            finalTaskDescription = getFinalTodoDescription(taskDescription);
        } else if (!getFinalDlDescription(taskDescription).isEmpty()) {
            finalTaskDescription = getFinalDlDescription(taskDescription);
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

    private static String getFinalDlDescription(String taskDescription) throws YqException {
        final char DEADLINE_CHARACTER = 'D';
        final String DEADLINE_WORD = "deadline ";
        String finalTaskDescription = "";
        if (taskDescription.charAt(TASK_TYPE_INDEX) == DEADLINE_CHARACTER) {
            String trimmedTaskDescription = taskDescription.substring(TASK_DESCRIPTION_INDEX).trim();
            String modifiedTaskDescription = modifyDeadlineDescription(trimmedTaskDescription);
            finalTaskDescription = DEADLINE_WORD + " " + modifiedTaskDescription.trim();
        }
        return finalTaskDescription;
    }

    private static String getFinalEventDescription(String taskDescription) throws YqException {
        final char EVENT_CHARACTER = 'E';
        final String EVENT_WORD = "event ";
        String finalTaskDescription = "";
        if (taskDescription.charAt(TASK_TYPE_INDEX) == EVENT_CHARACTER) {
            String trimmedTaskDescription = taskDescription.substring(TASK_DESCRIPTION_INDEX).trim();
            String modifiedTaskDescription = modifyEventDescription(trimmedTaskDescription);
            finalTaskDescription = EVENT_WORD + modifiedTaskDescription.trim();
        }
        return finalTaskDescription;
    }

    private static void autoMark(ArrayList<Task> taskArrayList) throws YqException {
        final String MARK_WORD = "mark ";
        Command parsedMarkCommand;
        String markCommand = MARK_WORD + taskArrayList.size();
        parsedMarkCommand = Parser.parse(markCommand);
        UI.printStraightLine();
        UI.processForOneSecond();
        parsedMarkCommand.autoExecute(taskArrayList, UI);
    }

    private static String modifyDeadlineDescription(String deadlineDescription) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        final String EDITED_BY = "/by";
        final String CLOSE_BRACKET = ")";
        final String ORIGINAL_BY = "by:";
        final String OPEN_BRACKET = "(";
        final int ZERO_INDEX = 0;
        int openBracketIndex = deadlineDescription.indexOf(OPEN_BRACKET);
        int byIndex = deadlineDescription.indexOf(ORIGINAL_BY) + ORIGINAL_BY.length();
        int closeBracketIndex = deadlineDescription.indexOf(CLOSE_BRACKET);
        String dlDescriptionPart = deadlineDescription.substring(ZERO_INDEX, openBracketIndex);
        String byDescription = deadlineDescription.substring(byIndex, closeBracketIndex);
        dateTimeHandler.revertDateTime(byDescription);
        return dlDescriptionPart + " " + EDITED_BY + " " + dateTimeHandler.getFinalDateTimeString();
    }


    private static String modifyEventDescription(String eventDescription) throws YqException {
        final String ORIGINAL_FROM = "from:";
        final String EDITED_FROM = "/from";
        final String ORIGINAL_TO = "to:";
        final String EDITED_TO = "/to";
        final String CLOSE_BRACKET = ")";
        final String OPEN_BRACKET = "(";
        final int ZERO_INDEX = 0;
        int openBracketIndex = eventDescription.indexOf(OPEN_BRACKET);
        int fromIndex = eventDescription.indexOf(ORIGINAL_FROM) + ORIGINAL_FROM.length();
        int beforeToIndex = eventDescription.indexOf(ORIGINAL_TO);
        int toIndex = beforeToIndex + ORIGINAL_TO.length();
        int closeBracketIndex = eventDescription.indexOf(CLOSE_BRACKET);
        String eventDescriptionPart = eventDescription.substring(ZERO_INDEX, openBracketIndex);
        String finalFromDescription = getEditedDescription(eventDescription, fromIndex, beforeToIndex);
        String finalToDescription = getEditedDescription(eventDescription, toIndex, closeBracketIndex);
        if (checkValidTimeInterval(finalFromDescription, finalToDescription)) {
            return eventDescriptionPart + EDITED_FROM + " " + finalFromDescription + " " + EDITED_TO + finalToDescription;
        }
        throw new InvalidTimeIntervalException();
    }

    private static boolean checkValidTimeInterval(String from, String to) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        return dateTimeHandler.compareDates(from, to);
    }
    private static String getEditedDescription(String eventDescription, int startIndex, int endIndex) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        String editedDescription = eventDescription.substring(startIndex, endIndex);
        dateTimeHandler.revertDateTime(editedDescription);
        return dateTimeHandler.getFinalDateTimeString();
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
