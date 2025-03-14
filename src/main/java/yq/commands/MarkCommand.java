package yq.commands;

import yq.exceptions.MarkIndexOutOfBoundsException;
import yq.exceptions.MarkNumberFormatException;
import yq.exceptions.MissingMarkNumberException;
import yq.exceptions.NothingToMarkException;
import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;


import java.util.ArrayList;

public class MarkCommand extends Command {
    public MarkCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        autoExecute(taskArrayList, ui);
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    @Override
    public void autoExecute(ArrayList<Task> taskArrayList, Ui ui) throws YqException {
        String commandInput = getCommandInput();
        checkValidMarkTask(commandInput, taskArrayList, ui);
    }

    /**
     * Conduct various checks to ensure that the mark command input is valid and can be processed.
     * Once it passes all the tests, it is executed and the task with the respective index in the
     * taskArrayList will be marked.
     *
     * @param substringOfMarkCmd Substring of the mark command input.
     * @param taskArrayList      ArrayList that stores the tasks.
     * @param ui                 User Interface class for printing relevant statements.
     * @throws YqException If the mark command input fails any of the test.
     */
    private static void checkValidMarkTask(String substringOfMarkCmd, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        checkValidMarkCmd(substringOfMarkCmd, taskArrayList);
        checkValidMarkNumber(substringOfMarkCmd, taskArrayList, ui);
    }

    /**
     * Prevent the mark command from processing if it is empty or when the taskArrayList is empty
     */
    private static void checkValidMarkCmd(String substringOfMarkCmd, ArrayList<Task> taskArrayList)
            throws YqException {
        if (taskArrayList.isEmpty()) {
            throw new NothingToMarkException();
        } else if (substringOfMarkCmd.isEmpty()) {
            throw new MissingMarkNumberException();
        }
    }

    /**
     * Prevent the mark command input which contains other invalid characters besides an integer or an integer that are
     * out of bounds of the taskArrayList range from being processed and executed.
     *
     * @param markNumber    Substring which contains the index of the task to be marked.
     * @param taskArrayList ArrayList that stores the tasks.
     * @param ui            User Interface class for printing relevant statements.
     * @throws YqException If the mark command input fails any of the test.
     */
    private static void checkValidMarkNumber(String markNumber, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        try {
            markTask(markNumber, taskArrayList, ui);
        } catch (NumberFormatException numberFormatException) {
            throw new MarkNumberFormatException();
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new MarkIndexOutOfBoundsException();
        }
    }

    private static void markTask(String markNumber, ArrayList<Task> taskArrayList, Ui ui) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        int chosenMarkIndex = Integer.parseInt(markNumber);
        Task selectedTask = taskArrayList.get(chosenMarkIndex - LIST_INDEX_ADJUSTMENT);
        ui.printTaskMarkedAsDone(selectedTask);
    }
}
