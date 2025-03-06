package yq.commands;

import yq.datetime.DateTimeHandler;
import yq.exceptions.DuplicateEventTaskException;
import yq.exceptions.EmptyEventCommandException;
import yq.exceptions.InvalidFromToIndexesException;
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

    private static void checkEmptyEventInput(String eventDescription, String from, String to)
            throws YqException {
        if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new MissingEventDescriptionException();
        }
    }

    private static void checkValidFromToIndex(int fromIndex, int toIndex)
            throws YqException {
        if (fromIndex >= toIndex) {
            throw new InvalidFromToIndexesException();
        }
    }

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
