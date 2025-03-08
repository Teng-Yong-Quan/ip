package yq.datetime;

import yq.exceptions.InvalidDateException;
import yq.exceptions.InvalidFormattedDateException;
import yq.exceptions.InvalidFormattedTimeException;
import yq.exceptions.InvalidTimeException;
import yq.exceptions.MissingDateException;
import yq.exceptions.MissingFormattedDateException;
import yq.exceptions.YqException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yq.ui.Ui;

public class DateTimeHandler {

    private String finalDateTimeString = "";
    private final Ui UI = new Ui();

    /**
     * Convert Date and Time (although time is optional) from 'yyyy-MM-dd' and 'HHmm' to day, 'dd-MMM-yyyy' and 'h.mma'
     * via after passing a series of checks. For example, '2025-03-08 1448' becomes 'SAT 08-Mar-2025 2.48pm'. The checks
     * ensure only the valid date and time input will be processed.
     *
     * @param dateTime String which contains the date followed by the time.
     * @throws YqException If the date time input violates any of the checks.
     */
    public void convertDateTime(String dateTime) throws YqException {
        String inputDate = findDate(dateTime);
        String finalizedDateTime;
        String finalizedDayDate = checkValidDayDate(inputDate);
        int dateStartIndex = dateTime.indexOf(inputDate);
        int dateEndIndex = dateStartIndex + inputDate.length();
        String time = dateTime.substring(dateEndIndex);
        String inputTime = findTime(time);
        String finalizedTime;
        if (!inputTime.isEmpty()) {
            finalizedTime = checkValidTime(inputTime);
            finalizedDateTime = finalizedDayDate + " " + finalizedTime;
        } else {
            finalizedDateTime = finalizedDayDate;
        }
        setFinalDateTimeString(finalizedDateTime);
    }

    /**
     * Prevent invalid date input having a different format or non-existent date from being processed.
     *
     * @param inputDate Date input string in 'yyyy-MM-dd' format.
     * @return Formatted date in 'DAY dd-MMM-yyyy' format.
     * @throws YqException If the date input cannot be parsed.
     */
    private String checkValidDayDate(String inputDate) throws YqException {
        try {
            return formatDate(inputDate);
        } catch (DateTimeParseException dateTimeParseException) {
            UI.printStraightLine();
            UI.processForOneSecond();
            throw new InvalidDateException();
        }
    }

    /**
     * Convert the day and the newly formatted date into a string after the date has been formatted into 'dd-MMM-yyyy'
     * from 'yyyy-MM-dd'
     */
    public String formatDate(String inputDate) {
        inputDate = editSymbolInDate(inputDate);
        LocalDate date = parseDate(inputDate);
        String newDateFormat = "dd-MMM-yyyy";
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(newDateFormat);
        String formattedDate = date.format(outputFormatter);
        String extractedDay = extractDay(date);
        return extractedDay + " " + formattedDate;
    }

    private static LocalDate parseDate(String inputDate) {
        String originalDateFormat = "yyyy-MM-dd";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(originalDateFormat);
        return LocalDate.parse(inputDate, inputFormatter);
    }

    private static String extractDay(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().toString();
        int startIndex = 0;
        int endIndex = 3;
        return dayOfWeek.substring(startIndex, endIndex);
    }

    private static String editSymbolInDate(String inputDate) {
        String forwardSlash = "/";
        String backSlash = "\\";
        String hyphen = "-";
        if (inputDate.contains(forwardSlash)) {
            inputDate = inputDate.replace(forwardSlash, hyphen);
        } else if (inputDate.contains(backSlash)) {
            inputDate = inputDate.replace(backSlash, hyphen);
        }
        return inputDate;
    }

    /**
     * Find the date with the matching expression of 'yyyy-MM-dd' and ignores date with invalid expression.
     *
     * @param dateTime Date time input string.
     * @return Date with the matching format.
     * @throws YqException If a date of the matching format cannot be detected.
     */
    public String findDate(String dateTime) throws YqException {
        int yearDigits = 4;
        int monthDigits = 2;
        int dayDigits = 2;
        String dateRegex = "\\d{%d}[-/\\\\]\\d{%d}[-/\\\\]\\d{%d}".formatted(yearDigits, monthDigits, dayDigits);
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(dateTime);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new MissingDateException();
    }

    /**
     * Prevent invalid time input having a different format or non-existent time from being processed.
     *
     * @param timeSubstring Time input string in 'HHmm' format.
     * @return Formatted time in 'H.mma' format.
     * @throws YqException If the time input cannot be parsed.
     */
    public String checkValidTime(String timeSubstring) throws YqException {
        try {
            return formatTime(timeSubstring);
        } catch (DateTimeParseException dateTimeParseException) {
            UI.printStraightLine();
            UI.processForOneSecond();
            throw new InvalidTimeException();
        }
    }

    /**
     * Find the time with the matching expression of 'HHmm' and ignores time with invalid expression by replacing it
     * with an empty string since the time is optional in the command input.
     *
     * @param time Time substring from the date time input.
     * @return String of the matching format "HHmm" if it is present else an empty string will be returned instead.
     */
    public String findTime(String time) {
        String trimmedTime = time.trim();
        int timeDigits = 4;
        String timeRegex = "\\d{%d}".formatted(timeDigits);
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(trimmedTime);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public String formatTime(String extractedTime) {
        LocalTime time = parseTime(extractedTime);
        String newTimeFormat = "h.mma";
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(newTimeFormat);
        return time.format(outputFormatter);
    }

    private static LocalTime parseTime(String extractedTime) {
        String oldTimeFormat = "HHmm";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(oldTimeFormat);
        return LocalTime.parse(extractedTime, inputFormatter);
    }

    private static LocalDateTime parseDateTime(String extractedDateTime) {
        String dateTimeFormat = "yyyy-MM-dd HHmm";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        return LocalDateTime.parse(extractedDateTime, inputFormatter);
    }

    public String getFinalDateTimeString() {
        return finalDateTimeString;
    }

    public void setFinalDateTimeString(String finalDateTimeString) {
        this.finalDateTimeString = finalDateTimeString;
    }

    /**
     * Revert Date and Time (although time is optional) from 'dd-MMM-yyyy' and 'h.mma' to 'yyyy-MM-dd' and 'HHmm'
     * via after passing a series of checks. For example, '08-Mar-2025 2.48pm' becomes '2025-03-08 1448'. The checks
     * ensure only the valid date and time input will be processed. This is only performed when the tasks are being
     * extracted from the input file as some of the tasks may contain the DAY, 'dd-MMM-yyyy' and 'h.mma'. The DAY can
     * be ignored as it is dependent on the date.
     *
     * @param finalizedDayDateTime String which contains the day, followed by the date and time.
     * @throws YqException If the day date time input violates any of the checks.
     */
    public void revertDateTime(String finalizedDayDateTime) throws YqException {
        String formattedDate = findFormattedDate(finalizedDayDateTime);
        String revertedDate = checkValidRevertedDate(formattedDate);
        int dateStartIndex = finalizedDayDateTime.indexOf(formattedDate);
        int dateEndIndex = dateStartIndex + formattedDate.length();
        String formattedTimeSubstring = finalizedDayDateTime.substring(dateEndIndex).trim();
        String formattedTime = findFormattedTime(formattedTimeSubstring);
        String revertedDateTime;
        if (!formattedTime.isEmpty()) {
            String revertedTime = checkValidRevertedTime(formattedTime);
            revertedDateTime = revertedDate + " " + revertedTime;
        } else {
            revertedDateTime = revertedDate;
        }
        setFinalDateTimeString(revertedDateTime);
    }

    /**
     * Prevent invalid date input having a different format or non-existent date from being processed.
     *
     * @param formattedDate Formatted date input string in 'dd-MMM-yyyy' format.
     * @return Date in 'yyyy-MM-dd' format.
     * @throws YqException If the date input cannot be parsed.
     */
    private String checkValidRevertedDate(String formattedDate) throws YqException {
        try {
            return revertDate(formattedDate);
        } catch (DateTimeParseException dateTimeParseException) {
            UI.printStraightLine();
            UI.processForOneSecond();
            throw new InvalidFormattedDateException();
        }
    }

    /**
     * Revert the day and the newly formatted date into a string after the date has been formatted into 'yyyy-MM-dd'
     * from 'dd-MMM-yyyy'
     */
    private String revertDate(String formattedDate) {
        formattedDate = editSymbolInDate(formattedDate);
        String originalDateFormat = "dd-MMM-yyyy";
        String newDateFormat = "yyyy-MM-dd";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(originalDateFormat);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(newDateFormat);
        LocalDate date = LocalDate.parse(formattedDate, inputFormatter);
        return date.format(outputFormatter);
    }

    /**
     * Prevent invalid time input having a different format or non-existent time  from being processed.
     *
     * @param formattedTime Time input string in 'h.mma' format.
     * @return Formatted time in 'HHmm' format.
     * @throws YqException If the time input cannot be parsed.
     */
    private String checkValidRevertedTime(String formattedTime) throws YqException {
        try {
            return revertTime(formattedTime);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new InvalidFormattedTimeException();
        }
    }

    /**
     * Find the time with the matching expression of 'h.mma' and ignores time with invalid expression by replacing it
     * with an empty string since the time is optional in the command input.
     *
     * @param formattedTime Time substring from the date time input.
     * @return String of the matching format "h.mma" if it is present else an empty string will be returned instead.
     */
    private String findFormattedTime(String formattedTime) {
        String trimmedTime = formattedTime.trim();
        int hourDoubleDigits = 2;
        int hourSingleDigit = 1;
        int minuteDigits = 2;
        String timeRegex = "\\b\\d{%d,%d}\\.\\d{%d}[ap]m\\b".formatted(hourSingleDigit,
                hourDoubleDigits, minuteDigits);
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(trimmedTime);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private String revertTime(String trimmedTime) {
        String oldTimeFormat = "h.mma";
        String newTimeFormat = "HHmm";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(oldTimeFormat);
        LocalTime time = LocalTime.parse(trimmedTime, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(newTimeFormat);
        return time.format(outputFormatter);
    }

    /**
     * Find the date with the matching expression of 'dd-MMM-yyyy' and ignores date with invalid expression.
     *
     * @param finalizedDayDateTime The string which contains the day, date and time.
     * @return Date with the matching format.
     * @throws YqException If a date of the matching format cannot be detected.
     */
    public String findFormattedDate(String finalizedDayDateTime) throws YqException {
        int yearDigits = 4;
        int monthDigits = 3;
        int dayDigits = 2;
        String formattedDateRegex = "\\b\\d{%d}[-/\\\\][A-Za-z]{%d}[-/\\\\]\\d{%d}\\b".formatted(dayDigits,
                monthDigits, yearDigits);
        Pattern pattern = Pattern.compile(formattedDateRegex);
        Matcher matcher = pattern.matcher(finalizedDayDateTime);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new MissingFormattedDateException();
    }

    /**
     * Ensure that the To date and time is always after the From date and time before processing the Event command input
     * into an Event task.
     *
     * @param from String that contains the start date and time.
     * @param to   String that contains the end date and time.
     * @return True if the start date and time is indeed before the end date and time else it is false.
     * @throws YqException If the input date time string cannot be parsed.
     */
    public boolean compareDates(String from, String to) throws YqException {
        LocalDateTime fromDateTime = extractDateTime(from);
        LocalDateTime toDateTime = extractDateTime(to);
        return fromDateTime.isBefore(toDateTime);
    }

    /**
     * Add the time factor in 'HHmm' format into the date time string and parse date time string so it is easier to
     * compare between 2 different dates of the same LocalDateTime type.
     *
     * @param dateTime Date time string.
     * @return The date time of LocalDateTime type.
     * @throws YqException If the date time string cannot be parsed.
     */
    private LocalDateTime extractDateTime(String dateTime) throws YqException {
        String time = getTimeSubstring(dateTime);
        LocalDateTime localDateTime;
        if (!time.isEmpty()) {
            localDateTime = parseDateTime(dateTime);
        } else {
            String midnight = "0000";
            String updatedDateTime = dateTime + " " + midnight;
            localDateTime = parseDateTime(updatedDateTime);
        }
        return localDateTime;
    }

    private String getTimeSubstring(String dateTime) throws YqException {
        String date = findDate(dateTime);
        int dateStartIndex = dateTime.indexOf(date);
        int dateEndIndex = dateStartIndex + date.length();
        String timeSubstring = dateTime.substring(dateEndIndex).trim();
        return findTime(timeSubstring);
    }
}


