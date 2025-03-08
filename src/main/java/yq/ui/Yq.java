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
        } catch (YqException yqException) {
            UI.showError(yqException.getMessage());
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
            } catch (YqException yqException) {
                UI.showError(yqException.getMessage());
            } finally {
                if (!isExit) {
                    UI.printPrompt();
                }
            }
        }
    }
}
