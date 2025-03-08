package yq.commands;

import yq.datetime.DateTimeHandler;
import yq.exceptions.DuplicateEventTaskException;
import yq.exceptions.EmptyEventCommandException;
import yq.exceptions.InvalidToFromException;
import yq.exceptions.InvalidTimeIntervalException;
import yq.exceptions.MissingEventDescriptionException;
import yq.exceptions.MissingFromKeywordException;
import yq.exceptions.MissingToKeywordException;
import yq.exceptions.YqException;
import yq.tasks.Event;
import yq.tasks.Task;
import yq.ui.Storage;
import yq.ui.TaskList;
import yq.ui.Ui;

import java.util.ArrayList;

public class EventCommand extends Command {
    public EventCommand(String commandInput) {
        setCommandInput(commandInput);
        setExit(false);
    }

    public void execute(TaskList taskList, Ui ui, Storage storage) throws YqException {
        ArrayList<Task> taskArrayList = taskList.getTaskArrayList();
        autoExecute(taskArrayList, ui);
        storage.saveTaskArraylist(taskList, taskArrayList);
    }

    /**
     * Conduct various checks to ensure that the event command input is valid and can be processed into an Event
     * Task. After that, it is then checked for any duplicate Event task that has already been present in the
     * taskArrayList. Once it passes all the tests, it is added into the taskArrayList.
     *
     * @param taskArrayList ArrayList that stores the tasks.
     * @param ui            User Interface class for printing relevant statements.
     * @throws YqException If the event command input fails any of the test,
     */
    @Override
    public void autoExecute(ArrayList<Task> taskArrayList, Ui ui) throws YqException {
        String commandInput = getCommandInput();
        String lcCommandInput = commandInput.toLowerCase();
        final int START_INDEX_STRING_CMD = 0;
        conductPrimaryEventCheck(commandInput, lcCommandInput);

        int fromIndex = lcCommandInput.indexOf("/from");
        int toIndex = lcCommandInput.indexOf("/to");
        int indexAfterFromWord = fromIndex + "/from".length();
        int indexAfterToWord = toIndex + "/to".length();
        checkValidFromToIndex(fromIndex, toIndex);

        String eventDescription = commandInput.substring(START_INDEX_STRING_CMD, fromIndex).trim();
        String from = commandInput.substring(indexAfterFromWord, toIndex).trim();
        String to = commandInput.substring(indexAfterToWord).trim();
        checkEmptyEventInput(eventDescription, from, to);
        String validFrom = checkValidFrom(from);
        String validTo = checkValidTo(to);
        if (checkValidEventInterval(validFrom, validTo)) {
            Event newEvent = new Event(eventDescription, validFrom, validTo);
            checkDuplicateEvent(taskArrayList, newEvent);
            taskArrayList.add(newEvent);
            ui.printAddedEventMessage(taskArrayList, newEvent);
        } else {
            throw new InvalidTimeIntervalException();
        }
    }

    private boolean checkValidEventInterval(String validFrom, String validTo) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        dateTimeHandler.revertDateTime(validFrom);
        String revertedFrom = dateTimeHandler.getFinalDateTimeString();
        dateTimeHandler.revertDateTime(validTo);
        String revertedTo = dateTimeHandler.getFinalDateTimeString();
        return dateTimeHandler.compareDates(revertedFrom, revertedTo);
    }

    private String checkValidTo(String to) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        dateTimeHandler.convertDateTime(to);
        return dateTimeHandler.getFinalDateTimeString();
    }

    private String checkValidFrom(String from) throws YqException {
        DateTimeHandler dateTimeHandler = new DateTimeHandler();
        dateTimeHandler.convertDateTime(from);
        return dateTimeHandler.getFinalDateTimeString();

    }

    private static void checkDuplicateEvent(ArrayList<Task> taskArrayList, Event newEvent)
            throws YqException {
        for (Task task : taskArrayList) {
            if (task.equals(newEvent)) {
                throw new DuplicateEventTaskException();
            }
        }
    }

    /**
     * Prevent the Event command with empty event description, from description or to description from being processed
     * into an Event task
     */
    private static void checkEmptyEventInput(String eventDescription, String from, String to)
            throws YqException {
        if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new MissingEventDescriptionException();
        }
    }

    /**
     * Prevent the event command input with '/to' keyword existing before '/from' keyword from being processed into an
     * Event task.
     *
     * @param fromIndex Index of the '/from' keyword.
     * @param toIndex   Index of the '/to' keyword.
     * @throws YqException If the index of the '/to' keyword < the index of the '/from' keyword.
     */
    private static void checkValidFromToIndex(int fromIndex, int toIndex)
            throws YqException {
        if (fromIndex >= toIndex) {
            throw new InvalidToFromException();
        }
    }

    /**
     * Prevent the empty event command whereby all event, from and to descriptions are missing from being processed into
     * an Event task.
     * Also, it checks for the presence of '/from' keyword and '/to' keyword.
     *
     * @param commandInput   Event command input.
     * @param lcCommandInput Lowercase Event command input.
     * @throws YqException If the Event command is empty or the 'from' keyword is missing or the '/to' keyword is
     *                     missing.
     */
    private static void conductPrimaryEventCheck(String commandInput, String lcCommandInput)
            throws YqException {
        if (commandInput.isEmpty()) {
            throw new EmptyEventCommandException();
        } else if (!lcCommandInput.contains("/from")) {
            throw new MissingFromKeywordException();
        } else if (!lcCommandInput.contains("/to")) {
            throw new MissingToKeywordException();
        }
    }
}
