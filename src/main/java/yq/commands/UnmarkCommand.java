package yq.commands;

import yq.exceptions.MissingUnmarkNumberException;
import yq.exceptions.NothingToUnmarkException;
import yq.exceptions.UnmarkIndexOutOfBoundsException;
import yq.exceptions.UnmarkNumberFormatException;
import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;


import java.util.ArrayList;

public class UnmarkCommand extends Command {
    public UnmarkCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        String commandInput = getCommandInput();
        checkValidUnmarkTask(commandInput, taskArrayList, ui);
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    /**
     * Conduct various checks to ensure that the unmark command input is valid and can be processed.
     * Once it passes all the tests, it is executed and the task with the respective index in the
     * taskArrayList will be unmarked.
     *
     * @param substringOfUnmarkCmd Substring of the unmark command input.
     * @param taskArrayList        ArrayList that stores the tasks.
     * @param ui                   User Interface class for printing relevant statements.
     * @throws YqException If the unmark command input fails any of the test.
     */
    private static void checkValidUnmarkTask(String substringOfUnmarkCmd, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        checkValidUnmarkCmd(substringOfUnmarkCmd, taskArrayList);
        checkValidUnmarkNumber(substringOfUnmarkCmd, taskArrayList, ui);
    }

    /**
     * Prevent the unmark command from processing if it is empty or when the taskArrayList is empty
     */
    private static void checkValidUnmarkCmd(String substringOfUnmarkCmd, ArrayList<Task> taskArrayList)
            throws YqException {
        if (taskArrayList.isEmpty()) {
            throw new NothingToUnmarkException();
        } else if (substringOfUnmarkCmd.isEmpty()) {
            throw new MissingUnmarkNumberException();
        }
    }

    /**
     * Prevent the unmark command input which contains other invalid characters besides an integer or an integer that
     * are out of bounds of the taskArrayList range from being processed and executed.
     *
     * @param unmarkNumber  Substring which contains the index of the task to be unmarked.
     * @param taskArrayList ArrayList that stores the tasks.
     * @param ui            User Interface class for printing relevant statements.
     * @throws YqException If the unmark command input fails any of the test.
     */
    private static void checkValidUnmarkNumber(String unmarkNumber, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        try {
            unmarkTask(unmarkNumber, taskArrayList, ui);
        } catch (NumberFormatException numberFormatException) {
            throw new UnmarkNumberFormatException();
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new UnmarkIndexOutOfBoundsException();
        }
    }

    private static void unmarkTask(String unmarkNumber, ArrayList<Task> taskArrayList, Ui ui) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        int chosenUnmarkIndex = Integer.parseInt(unmarkNumber);
        Task selectedTask = taskArrayList.get(chosenUnmarkIndex - LIST_INDEX_ADJUSTMENT);
        ui.printTaskUnmarked(selectedTask);
    }
}
