package yq.ui;

import yq.commands.Command;
import yq.datetime.DateTimeHandler;
import yq.exceptions.InvalidCommandException;
import yq.exceptions.InvalidTimeIntervalException;
import yq.exceptions.YqException;
import yq.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    /**
     * The ArrayList which contains the task lines extracted from the input file
     */
    private static final ArrayList<String> TASKS_FROM_FILE_ARRAY_LIST = new ArrayList<>();
    private static final Ui UI = new Ui();
    private String fileName;
    private static ArrayList<Task> taskArrayList = new ArrayList<>();
    /**
     * The index where the task type T (Todo), D (Deadline) and E (Event) is located
     */
    static final int TASK_TYPE_INDEX = 1;
    /**
     * The index where the full task description is located
     */
    static final int TASK_DESCRIPTION_INDEX = 7;

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
        getTaskLinesFromFile();
        populateTaskArraylist();
        return taskArrayList;
    }

    void showFileNotFoundError() {
        System.out.println("    " + fileName + " file is not found." + "\n");
    }

    /**
     * Retrieve lines from the file and add them into TASKS_FROM_FILE_ARRAY_LIST if the file is not empty
     */
    private void getTaskLinesFromFile() throws IOException {
        File file = new File(this.getFileName());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String readLine = bufferedReader.readLine();
        if (readLine == null || readLine.isEmpty()) { // Check if the file is empty
            bufferedReader.close();
            return;
        }
        bufferedReader.close();
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            TASKS_FROM_FILE_ARRAY_LIST.add(line);
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
        for (String taskLine : TASKS_FROM_FILE_ARRAY_LIST) {
            checkValidTaskLine(taskLine);
        }
    }

    /**
     * Checks whether the lines extracted from the input file can be converted into valid tasks.
     *
     * @param taskLine Line of the task extracted from the input file.
     */
    private static void checkValidTaskLine(String taskLine) {
        try {
            makeTask(taskLine);
        } catch (YqException yqException) {
            UI.showError(yqException.getMessage());
        }
    }

    /**
     * Create a valid todo, deadline or event task according to the processed input task line string. If the input task
     * line string is invalid and becomes an empty string, it will be ignored and the user will be notified by such
     * occurrence.
     *
     * @param taskLine Line of the task extracted from the input file.
     * @throws YqException If an issue is encountered during the auto-parsing of the command and marking extracted task.
     */
    private static void makeTask(String taskLine) throws YqException {
        String finalizedCommand;
        taskLine = taskLine.trim();
        finalizedCommand = getFinalCommand(taskLine);
        if (finalizedCommand.isEmpty()) {
            UI.printIgnoreInvalidLineMessage();
            return;
        }
        processAutoParsedCommand(finalizedCommand);
        markExtractedTask(taskLine);
    }

    private static void processAutoParsedCommand(String finalizedCommand) throws YqException {
        Command autoParsedCommand = Parser.parse(finalizedCommand);
        UI.printStraightLine();
        UI.processForOneSecond();
        autoParsedCommand.autoExecute(taskArrayList, UI);
    }

    /**
     * Check whether the task line saved in the input file is marked and mark accordingly
     */
    private static void markExtractedTask(String taskLine) throws YqException {
        final int MARK_OR_UNMARK_INDEX = 4; // The index whereby it shows the task is marked or unmarked.
        final char MARK_SYMBOL = 'X';
        if (taskLine.charAt(MARK_OR_UNMARK_INDEX) == MARK_SYMBOL) {
            autoMark(taskArrayList);
        }
    }

    private static String getFinalCommand(String taskLine) throws YqException {
        String finalizedCommand = "";
        if (!getFinalTodoCommand(taskLine).isEmpty()) {
            finalizedCommand = getFinalTodoCommand(taskLine);
        } else if (!getFinalDeadlineCommand(taskLine).isEmpty()) {
            finalizedCommand = getFinalDeadlineCommand(taskLine);
        } else if (!getFinalEventCommand(taskLine).isEmpty()) {
            finalizedCommand = getFinalEventCommand(taskLine);
        }
        return finalizedCommand;
    }

    /**
     * Check the task line is a valid todo task line and process it into a Todo command that can be parsed while
     * ignoring other invalid task line.
     * Since the task line contains only the todo task description and the format of the accepted command to be parsed
     * consists of 'todo' word followed by the todo task description, the 'todo' word is added before the todo task
     * description.
     *
     * @param taskLine Line of the task extracted from the input file.
     * @return Finalized Todo Command.
     */
    private static String getFinalTodoCommand(String taskLine) {
        final char TODO_CHARACTER = 'T';
        final String TODO_WORD = "todo ";
        String finalizedCommand = "";
        if (taskLine.charAt(TASK_TYPE_INDEX) == TODO_CHARACTER) {
            finalizedCommand = TODO_WORD + taskLine.substring(TASK_DESCRIPTION_INDEX).trim();
        }
        return finalizedCommand;
    }

    /**
     * Check the task line is a valid deadline task line and process it into a Deadline command that can be parsed while
     * ignoring other invalid task line.
     * Since the task line contains only the deadline task description and by description while the format of the
     * accepted command to be parsed consists of 'deadline' word followed by the deadline task description and lastly
     * the by description, the 'deadline' word is added before the deadline task description.
     *
     * @param taskLine Line of the task extracted from the input file.
     * @return Finalized Deadline Command.
     */
    private static String getFinalDeadlineCommand(String taskLine) throws YqException {
        final char DEADLINE_CHARACTER = 'D';
        final String DEADLINE_WORD = "deadline ";
        String modifiedTaskLine;
        String finalizedCommand = "";
        if (taskLine.charAt(TASK_TYPE_INDEX) == DEADLINE_CHARACTER) {
            String trimmedTaskLine= taskLine.substring(TASK_DESCRIPTION_INDEX).trim();
            modifiedTaskLine = modifyDeadlineFormat(trimmedTaskLine);
            finalizedCommand = DEADLINE_WORD + modifiedTaskLine.trim();
        }
        return finalizedCommand;
    }

    /**
     * Check the task line is a valid event task line and process it into an Event command that can be parsed while
     * ignoring other invalid task line.
     * Since the task line contains only the event task description, from description and to description while the
     * format of the accepted command to be parsed consists of the 'event' word followed by the event task description,
     * then the from description and lastly the to description, the 'event' word is added before the event task
     * description.
     *
     * @param taskLine Line of the task extracted from the input file.
     * @return Finalized Deadline Command.
     */
    private static String getFinalEventCommand(String taskLine) throws YqException {
        final char EVENT_CHARACTER = 'E';
        final String EVENT_WORD = "event ";
        String modifiedTaskLine;
        String finalizedCommand = "";
        if (taskLine.charAt(TASK_TYPE_INDEX) == EVENT_CHARACTER) {
            String trimmedTaskLine = taskLine.substring(TASK_DESCRIPTION_INDEX).trim();
            modifiedTaskLine = modifyEventFormat(trimmedTaskLine);
            finalizedCommand = EVENT_WORD + modifiedTaskLine.trim();
        }
        return finalizedCommand;
    }

    /**
     * Parse and execute the mark command so the respective task could de marked as done
     */
    private static void autoMark(ArrayList<Task> taskArrayList) throws YqException {
        final String MARK_WORD = "mark ";
        Command parsedMarkCommand;
        String markCommand = MARK_WORD + taskArrayList.size();
        parsedMarkCommand = Parser.parse(markCommand);
        UI.printStraightLine();
        UI.processForOneSecond();
        parsedMarkCommand.autoExecute(taskArrayList, UI);
    }

    /**
     * Modify the deadline task line with 'by:' into /by and remove the brackets because the accepted Deadline command
     * input format consists of the 'deadline', task description, '/by' and by description.
     *
     * @param deadlineTaskLine Line of the deadline task extracted from the input file.
     * @return Modified deadline task line.
     */
    private static String modifyDeadlineFormat(String deadlineTaskLine) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        final String ORIGINAL_BY = "by:";
        final String EDITED_BY = "/by";
        final String OPEN_BRACKET = "(";
        final String CLOSE_BRACKET = ")";
        final int ZERO_INDEX = 0;
        int openBracketIndex = deadlineTaskLine.indexOf(OPEN_BRACKET);
        int byIndex = deadlineTaskLine.indexOf(ORIGINAL_BY) + ORIGINAL_BY.length();
        int closeBracketIndex = deadlineTaskLine.indexOf(CLOSE_BRACKET);
        String dlDescriptionPart = deadlineTaskLine.substring(ZERO_INDEX, openBracketIndex);
        String byDescription = deadlineTaskLine.substring(byIndex, closeBracketIndex);
        dateTimeHandler.revertDateTime(byDescription);
        return dlDescriptionPart + " " + EDITED_BY + " " + dateTimeHandler.getFinalDateTimeString();
    }

    /**
     * Modify the event task line with 'from:' into /from, 'to:' into '/to' and remove the brackets because the
     * accepted Event command input format consists of the 'event', task description, '/from', from description,
     * '/to' and to description.
     *
     * @param eventTaskLine Line of the deadline task extracted from the input file.
     * @return Modified event task line.
     */
    private static String modifyEventFormat(String eventTaskLine) throws YqException {
        final String ORIGINAL_FROM = "from:";
        final String EDITED_FROM = "/from";
        final String ORIGINAL_TO = "to:";
        final String EDITED_TO = "/to";
        final String OPEN_BRACKET = "(";
        final String CLOSE_BRACKET = ")";
        final int ZERO_INDEX = 0;
        int openBracketIndex = eventTaskLine.indexOf(OPEN_BRACKET);
        int fromIndex = eventTaskLine.indexOf(ORIGINAL_FROM) + ORIGINAL_FROM.length();
        int beforeToIndex = eventTaskLine.indexOf(ORIGINAL_TO);
        int toIndex = beforeToIndex + ORIGINAL_TO.length();
        int closeBracketIndex = eventTaskLine.indexOf(CLOSE_BRACKET);
        String eventDescriptionPart = eventTaskLine.substring(ZERO_INDEX, openBracketIndex);
        String finalFromDescription = getEditedDescription(eventTaskLine, fromIndex, beforeToIndex);
        String finalToDescription = getEditedDescription(eventTaskLine, toIndex, closeBracketIndex);
        if (checkValidTimeInterval(finalFromDescription, finalToDescription)) {
            return eventDescriptionPart + EDITED_FROM + " " + finalFromDescription + " "
                    + EDITED_TO + finalToDescription;
        }
        throw new InvalidTimeIntervalException();
    }

    private static boolean checkValidTimeInterval(String from, String to) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        return dateTimeHandler.compareDates(from, to);
    }
    private static String getEditedDescription(String eventDescription, int startIndex, int endIndex)
            throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        String editedDescription = eventDescription.substring(startIndex, endIndex);
        dateTimeHandler.revertDateTime(editedDescription);
        return dateTimeHandler.getFinalDateTimeString();
    }

    /**
     * Check for the file name existence before the tasks are being saved once the program stops running. If the
     * original file exists, it will be deleted before a new file of the same name is created. The valid task line in
     * the original file will not be lost as they have already been transferred into the taskArrayList before deletion.
     * Only the invalid task lines, which the program is unable to process, will be lost. After the new file of the same
     * name is created, the tasks in the taskArrayList will be transferred into the new file.
     *
     * @throws IOException If the file cannot be found.
     */
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
