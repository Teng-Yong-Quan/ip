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
        deleteTask(commandInput, taskArrayList, ui);
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    private static void deleteTask(String substringOfDeleteCmd, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        checkValidDeleteCmd(substringOfDeleteCmd, taskArrayList);
        checkValidDeleteNumber(substringOfDeleteCmd, taskArrayList, ui);
    }

    private static void checkValidDeleteCmd(String substringOfDeleteCmd, ArrayList<Task> taskArrayList)
            throws YqException {
        if (taskArrayList.isEmpty()) {
            throw new NothingToDeleteException();
        } else if (substringOfDeleteCmd.isEmpty()) {
            throw new MissingDeleteNumberException();
        }
    }

    private static void checkValidDeleteNumber(String deleteNumber, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        try {
            deletingTask(deleteNumber, taskArrayList, ui);
        } catch (NumberFormatException numberFormatException) {
            throw new DeleteNumberFormatException();
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new DeleteIndexOutOfBoundsException();
        }
    }

    private static void deletingTask(String deleteNumber, ArrayList<Task> taskArrayList, Ui ui) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        int chosenDeleteIndex = Integer.parseInt(deleteNumber);
        Task deletedTask = taskArrayList.remove(chosenDeleteIndex - LIST_INDEX_ADJUSTMENT);
        ui.printDeletedTaskMessage(deletedTask, taskArrayList);
    }
}
