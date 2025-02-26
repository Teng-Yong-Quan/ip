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
        markTask(commandInput, taskArrayList, ui);
    }

    private static void markTask(String substringOfMarkCmd, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        checkValidMarkCmd(substringOfMarkCmd, taskArrayList);
        checkValidMarkNumber(substringOfMarkCmd, taskArrayList, ui);
    }

    private static void checkValidMarkCmd(String substringOfMarkCmd, ArrayList<Task> taskArrayList)
            throws YqException {
        if (taskArrayList.isEmpty()) {
            throw new NothingToMarkException();
        } else if (substringOfMarkCmd.isEmpty()) {
            throw new MissingMarkNumberException();
        }
    }

    private static void checkValidMarkNumber(String markNumber, ArrayList<Task> taskArrayList, Ui ui)
            throws YqException {
        try {
            markingTask(markNumber, taskArrayList, ui);
        } catch (NumberFormatException numberFormatException) {
            throw new MarkNumberFormatException();
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new MarkIndexOutOfBoundsException();
        }
    }

    private static void markingTask(String markNumber, ArrayList<Task> taskArrayList, Ui ui) {
        final int LIST_INDEX_ADJUSTMENT = 1;
        int chosenMarkIndex = Integer.parseInt(markNumber);
        Task selectedTask = taskArrayList.get(chosenMarkIndex - LIST_INDEX_ADJUSTMENT);
        ui.printTaskMarkedAsDone(selectedTask);
    }
}
