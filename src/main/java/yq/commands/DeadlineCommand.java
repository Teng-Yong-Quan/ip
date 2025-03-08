package yq.commands;

import yq.exceptions.DuplicateDeadlineTaskException;
import yq.exceptions.EmptyDeadlineCommandException;
import yq.exceptions.MissingByKeywordException;
import yq.exceptions.MissingDeadlineDescriptionException;
import yq.exceptions.YqException;
import yq.tasks.Deadline;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.util.ArrayList;

public class DeadlineCommand extends Command {
    public DeadlineCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        autoExecute(taskArrayList, ui);
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    /**
     * Conduct various checks to ensure that the deadline command input is valid and can be processed into a Deadline
     * Task. After that, it is then checked for any duplicate Deadline task that has already been present in the
     * taskArrayList. Once it passes all the tests, it is added into the taskArrayList.
     *
     * @param taskArrayList ArrayList that stores the tasks.
     * @param ui            User Interface class for printing relevant statements.
     * @throws YqException If the deadline command input fails any of the test,
     */
    @Override
    public void autoExecute(ArrayList<Task> taskArrayList, Ui ui) throws YqException {
        final int START_INDEX_STRING_CMD = 0;
        String commandInput = getCommandInput();
        String lcCommandInput = commandInput.toLowerCase();
        conductPrimaryDlCheck(commandInput, lcCommandInput);

        int byIndex = lcCommandInput.indexOf("/by");
        String deadlineDescription = commandInput.substring(START_INDEX_STRING_CMD, byIndex).trim();

        int indexAfterByWord = byIndex + "/by".length();
        String by = commandInput.substring(indexAfterByWord).trim();

        checkEmptyDlInput(deadlineDescription, by);
        Deadline newDeadline = new Deadline(deadlineDescription, by);

        checkDuplicateDeadline(taskArrayList, newDeadline);
        taskArrayList.add(newDeadline);
        ui.printAddedDeadlineMessage(taskArrayList, newDeadline);
    }

    /**
     * Prevent the Deadline command with empty deadline description or by description from being processed into a
     * Deadline task
     */
    private static void checkEmptyDlInput(String deadlineDescription, String by) throws YqException {
        if (deadlineDescription.isEmpty() || by.isEmpty()) {
            throw new MissingDeadlineDescriptionException();
        }
    }

    /**
     * Prevent the empty deadline command whereby both the deadline description and by description are missing from
     * being processed into a Deadline task.
     * Also, it checks for the presence of "/by" keyword.
     *
     * @param commandInput   Deadline command input.
     * @param lcCommandInput Lowercase Deadline command input.
     * @throws YqException If the deadline command is empty or the 'by' keyword is missing.
     */
    private static void conductPrimaryDlCheck(String commandInput, String lcCommandInput) throws YqException {
        if (commandInput.isEmpty()) {
            throw new EmptyDeadlineCommandException();
        } else if (!lcCommandInput.contains("/by")) {
            throw new MissingByKeywordException();
        }
    }

    private static void checkDuplicateDeadline(ArrayList<Task> taskArrayList, Deadline newDeadline)
            throws YqException {
        for (Task task : taskArrayList) {
            if (task.equals(newDeadline)) {
                throw new DuplicateDeadlineTaskException();
            }
        }
    }
}
