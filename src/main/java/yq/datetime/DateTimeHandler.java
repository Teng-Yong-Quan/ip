package yq.datetime;

import yq.exceptions.InvalidDateException;
import yq.exceptions.InvalidFormattedDateException;
import yq.exceptions.InvalidFormattedTimeException;
import yq.exceptions.InvalidTimeException;
import yq.exceptions.InvalidTimeIntervalException;
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

    private String checkValidDayDate(String inputDate) throws YqException {
        try {
            return formatDate(inputDate);
        } catch (DateTimeParseException dateTimeParseException) {
            UI.printStraightLine();
            UI.processForOneSecond();
            throw new InvalidDateException();
        }
    }

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

    public String checkValidTime(String timeSubstring) throws YqException {
        try {
            return formatTime(timeSubstring);
        } catch (DateTimeParseException dateTimeParseException) {
            UI.printStraightLine();
            UI.processForOneSecond();
            throw new InvalidTimeException();
        }
    }

    public String findTime(String dateTime) {
        String trimmedTime = dateTime.trim();
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

    private String checkValidRevertedDate(String formattedDate) throws YqException {
        try {
            return revertDate(formattedDate);
        } catch (DateTimeParseException dateTimeParseException) {
            UI.printStraightLine();
            UI.processForOneSecond();
            throw new InvalidFormattedDateException();
        }
    }

    private String revertDate(String formattedDate) {
        formattedDate = editSymbolInDate(formattedDate);
        String originalDateFormat = "dd-MMM-yyyy";
        String newDateFormat = "yyyy-MM-dd";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(originalDateFormat);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(newDateFormat);
        LocalDate date = LocalDate.parse(formattedDate, inputFormatter);
        return date.format(outputFormatter);
    }


    private String checkValidRevertedTime(String formattedTime) throws YqException {
        try {
            return revertTime(formattedTime);
        } catch (DateTimeParseException dateTimeParseException) {
            throw new InvalidFormattedTimeException();
        }
    }

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
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("h.mma");
        LocalTime time = LocalTime.parse(trimmedTime, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HHmm");
        return time.format(outputFormatter);
    }

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

    public boolean compareDates(String from, String to) throws YqException {
        LocalDateTime fromDateTime = extractDateTime(from);
        LocalDateTime toDateTime = extractDateTime(to);
        return fromDateTime.isBefore(toDateTime);
    }

    private LocalDateTime extractDateTime(String dateTime) throws YqException {
        String time = getTimeSubstring(dateTime);
        LocalDateTime fromDateTime;
        if (!time.isEmpty()) {
            fromDateTime = parseDateTime(dateTime);
        } else {
            String midnight = "0000";
            String updatedDateTime = dateTime + " " + midnight;
            fromDateTime = parseDateTime(updatedDateTime);
        }
        return fromDateTime;
    }

    private String getTimeSubstring(String dateTime) throws YqException {
        String date = findDate(dateTime);
        int dateStartIndex = dateTime.indexOf(date);
        int dateEndIndex = dateStartIndex + date.length();
        String timeSubstring = dateTime.substring(dateEndIndex).trim();
        return findTime(timeSubstring);
    }
}


