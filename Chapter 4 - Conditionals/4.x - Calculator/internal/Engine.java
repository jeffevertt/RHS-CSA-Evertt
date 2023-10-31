package internal;

import java.math.BigDecimal;

import javax.swing.SwingUtilities;

import constants.Buttons;

class Engine {

    private static boolean isEntryIncomplete = false;
    private static double previousResult = Double.NaN;
    private static String previousOperationButton = "";
    private static boolean wasPreviousButtonEquals = false;

    static void run() {
        SwingUtilities.invokeLater(UI::create);
    }

    private static String convertDoubleToString(Double doubleValue) {
        BigDecimal big = BigDecimal.valueOf(doubleValue).stripTrailingZeros();
        String stringValue = big.toPlainString();
        return stringValue;
    }

    private static double convertStringToDouble(String stringValue) {
        double doubleValue = Double.parseDouble(stringValue);
        return doubleValue;
    }

    static boolean isEntryIncomplete() {
        return isEntryIncomplete;
    }

    static void setEntryIncomplete(boolean isEntryIncomplete) {
        Engine.isEntryIncomplete = isEntryIncomplete;
    }

    static double getPreviousResult() {
        if (!hasPreviousResult()) {
            throw new IllegalStateException("No previous result");
        }
        return previousResult;
    }

    static boolean hasPreviousResult() {
        return !Double.isNaN(previousResult);
    }

    static void setPreviousResult(double previousResult) {
        if (Double.isNaN(previousResult)) {
            throw new IllegalArgumentException("Not a number");
        }
        Engine.previousResult = previousResult;
    }

    static void clearPreviousResult() {
        Engine.previousResult = Double.NaN;
    }

    static String getPreviousOperationButton() {
        return previousOperationButton;
    }

    static boolean hasPreviousOperationButton() {
        return !previousOperationButton.isEmpty();
    }

    static void setPreviousOperationButton(String previousOperationButton) {
        if (previousOperationButton == null || previousOperationButton.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "previousOperationButton cannot be empty. Use clearPreviousOperationButton() instead");
        }
        if (previousOperationButton.equals(Buttons.EQUALS)) {
            throw new IllegalArgumentException(
                    "The = operator should not be used here. Call the setWasPreviousButtonEquals method instead");
        }
        Engine.previousOperationButton = previousOperationButton;
    }

    static void clearPreviousOperationButton() {
        Engine.previousOperationButton = "";
    }

    static boolean wasPreviousButtonEquals() {
        return wasPreviousButtonEquals;
    }

    static void setWasPreviousButtonEquals(boolean wasPreviousButtonEquals) {
        Engine.wasPreviousButtonEquals = wasPreviousButtonEquals;
    }

    static void swapSecondaryButtons() {
        UI.swapSecondaryButtons();
    }

    static void swapClearButton() {
        UI.swapClearButton();
    }

    static String getExpression() {
        return UI.getExpressionText();
    }

    static void setExpression(String expressionText) {
        UI.setExpressionText(expressionText);
    }

    static void clearExpression() {
        setExpression("");
    }

    static void appendToExpression(String textToAppendToExpression) {
        setExpression(getExpression() + textToAppendToExpression);
    }

    static void prependToExpression(String textToPrependToExpression) {
        setExpression(textToPrependToExpression + getExpression());
    }

    static void deleteTrailingEqualsFromExpression() {
        String expression = getExpression();
        if (expression.endsWith("=")) {
            setExpression(expression.substring(0, expression.length() - 1));
        }
    }

    static String getEntryAsString() {
        return UI.getEntryText();
    }

    static double getEntryAsDouble() {
        String entryString = getEntryAsString();
        if (entryString.endsWith(".")) {
            entryString = entryString.substring(0, entryString.length() - 1);
        }
        double entryDouble = convertStringToDouble(entryString);
        return entryDouble;
    }

    static boolean entryHasADecimalPoint() {
        return UI.getEntryText().contains(".");
    }

    static void setEntry(String entry) {
        UI.setEntryText(entry);
    }

    static void setEntry(double entry) {
        String entryString = convertDoubleToString(entry);
        setEntry(entryString);
    }

    static void setEntry(char entry) {
        setEntry(Character.toString(entry));
    }

    static void appendDecimalPointToEntry() {
        if (entryHasADecimalPoint()) {
            throw new IllegalStateException("The entry already has a decimal point");
        }
        setEntry(getEntryAsString() + '.');
    }

    static void appendDigitToEntry(char digit) {
        if (digit == '.') {
            appendDecimalPointToEntry();
            return;
        }
        if (digit < '0' || digit > '9') {
            throw new IllegalArgumentException("'" + digit + "' is not a digit");
        }
        setEntry(getEntryAsString() + digit);
    }

    static void appendDigitToEntry(String digit) {
        if (digit == null) {
            throw new IllegalArgumentException("Digit cannot be null");
        }
        if (digit.length() != 1) {
            throw new IllegalArgumentException("Digit string must have exactly 1 character in it");
        }
        appendDigitToEntry(digit.charAt(0));
    }

    static void deleteLastDigitFromEntry() {
        String entry = getEntryAsString();
        setEntry(entry.substring(0, entry.length() - 1));
    }

    static double getLatestMemoryValue() {
        return convertStringToDouble(UI.getLatestMemoryValue());
    }

    static void replaceLatestMemoryValue(double valueToStore) {
        UI.replaceLatestMemoryValue(convertDoubleToString(valueToStore));
    }

    static void storeNewMemoryValue(double valueToStore) {
        UI.storeNewMemoryValue(convertDoubleToString(valueToStore));
    }

    static void clearMemoryValues() {
        UI.clearMemoryValues();
    }

    static void toggleMemoryHistoryMenu() {
        UI.toggleMemoryHistoryMenu();
    }

}
