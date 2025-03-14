package yq.ui;

import yq.commands.Command;
import yq.exceptions.YqException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Yq {
    private final Storage STORAGE;
    private TaskList taskList;
    private final Ui UI;
    private static final String FILENAME = "saved_task_arraylist.txt";

    /**
     * Load the saved tasks in the file into the task list if the file exists. If the file does not exist, the user will
     * be notified and a new empty task list will be created.
     *
     * @param fileName Name of the file input.
     */
    private Yq(String fileName) {
        UI = new Ui();
        STORAGE = new Storage(fileName);
        try {
            taskList = new TaskList(STORAGE.load());
        } catch (FileNotFoundException fileNotFoundException) {
            STORAGE.showFileNotFoundError();
            taskList = new TaskList();
        } catch (IOException ioException) {
            System.out.println("Something went wrong: " + ioException.getMessage() + "\n");
        } catch (Exception exception) {
            printExceptionMessage(exception);
        }
    }

    /** Print message accordingly after checking whether the exception is an instance of YqException. */
    private void printExceptionMessage(Exception exception) {
        if (exception instanceof YqException) {
            UI.showError(exception.getMessage());
        } else {
            UI.printStraightLine();
            UI.processForOneSecond();
            UI.showError("    Unable to carry out the operation. It will be skipped.");
        }
    }

    public static void main(String[] args) {
        new Yq(FILENAME).run();
    }

    private void run() {
        UI.printWelcomeMessage();
        boolean isExit = false;
        while (!isExit) {
            try {
                UI.printCommandOptions();
                String fullCommand = UI.readCommand();
                UI.printStraightLine();
                UI.processForOneSecond();
                Command parsedCommand = Parser.parse(fullCommand);
                parsedCommand.execute(taskList, UI, STORAGE);
                isExit = parsedCommand.isExit();
            } catch (Exception exception) {
                printExceptionMessage(exception);
            } finally {
                if (!isExit) {
                    UI.printPrompt();
                }
            }
        }
    }
}
