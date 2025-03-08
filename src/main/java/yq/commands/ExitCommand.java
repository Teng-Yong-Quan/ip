package yq.commands;

import yq.exceptions.YqException;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class ExitCommand extends Command {
    public ExitCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(true);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        storage.saveTaskArraylist(taskList,taskArrayList);
        checkValidWriteToFile(storage);
        ui.printGoodByeMessage();
    }

    /**
     * Ensure that the file is being created if it does not exist, so the tasks could be saved in the file
     */
    public void checkValidWriteToFile(Storage storage) {
        try {
            storage.writeToFile();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("    " + storage.getFileName() + " file is not found." + "\n");
        } catch (IOException ioException) {
            System.out.println("    Something went wrong: " + ioException.getMessage() + "\n");
        }
    }
}
