package yq.commands;

import yq.exceptions.DeleteIndexOutOfBoundsException;
import yq.exceptions.DeleteNumberFormatException;
import yq.exceptions.MissingDeleteNumberException;
import yq.exceptions.NothingToDeleteException;
import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.util.ArrayList;

public class DeleteCommand extends Command {
    public DeleteCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        String commandInput = getCommandInput();
        checkValidDeleteTask(commandInput, taskArrayList, ui);
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    /**
     * Conduct various checks to ensure that the delete command input is valid and can be processed.
     * Once it passes all the tests, it is executed and the task with the respective index is removed from
     * the taskArrayList.
     *
     * @param substringOfDeleteCmd Substring of the delete command input.
     * @param taskArrayList        ArrayList that stores the tasks.
     * @param ui                   User Interface class for printing relevant statements.
     * @throws YqException If the delete command input fails any of the test.
     */
    private static void checkValidDeleteTask(String substringOfDeleteCmd, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        checkValidDeleteCmd(substringOfDeleteCmd, taskArrayList);
        checkValidDeleteNumber(substringOfDeleteCmd, taskArrayList, ui);
    }

    /**
     * Prevent the delete command from processing if it is empty or when the taskArrayList is empty
     */
    private static void checkValidDeleteCmd(String substringOfDeleteCmd, ArrayList<Task> taskArrayList)
            throws YqException {
        if (taskArrayList.isEmpty()) {
            throw new NothingToDeleteException();
        } else if (substringOfDeleteCmd.isEmpty()) {
            throw new MissingDeleteNumberException();
        }
    }

    /**
     * Prevent the delete command input which contains other invalid characters besides an integer or an integer that
     * are out of bounds of the taskArrayList range from being processed and executed.
     *
     * @param deleteNumber  Substring which contains the index of the task to be deleted.
     * @param taskArrayList ArrayList that stores the tasks.
     * @param ui            User Interface class for printing relevant statements.
     * @throws YqException If the delete command input fails any of the test.
     */
    private static void checkValidDeleteNumber(String deleteNumber, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        try {
            deleteTask(deleteNumber, taskArrayList, ui);
        } catch (NumberFormatException numberFormatException) {
            throw new DeleteNumberFormatException();
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new DeleteIndexOutOfBoundsException();
        }
    }

    private static void deleteTask(String deleteNumber, ArrayList<Task> taskArrayList, Ui ui) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        int chosenDeleteIndex = Integer.parseInt(deleteNumber);
        Task deletedTask = taskArrayList.remove(chosenDeleteIndex - LIST_INDEX_ADJUSTMENT);
        ui.printDeletedTaskMessage(deletedTask, taskArrayList);
    }
}
